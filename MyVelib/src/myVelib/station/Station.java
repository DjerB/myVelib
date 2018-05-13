package myVelib.station;

import java.util.ArrayList;
import java.util.Iterator;

import myVelib.bike.Bike;
import myVelib.bike.ElectricalBike;
import myVelib.bike.MechanicalBike;
import myVelib.exceptions.FreeException;
import myVelib.exceptions.MissingDataException;
import myVelib.exceptions.NoDestinationAssignedException;
import myVelib.exceptions.NotAnAnswerException;
import myVelib.exceptions.NotAvailableBikeException;
import myVelib.exceptions.OccupiedException;
import myVelib.exceptions.OfflineException;
import myVelib.system.Network;
import myVelib.userAndCard.User;
import myVelib.utilities.Position;
import myVelib.utilities.PrintConsole;
import myVelib.utilities.Time;
import myVelib.utilities.typeBike;
import myVelib.exceptions.*;

public class Station {
	
	private int nbSlots=0;
	private final int id;
	private boolean online = true;
	private Terminal terminal=new Terminal();
	protected ArrayList<Slot> slots=new ArrayList<Slot>();
	protected Position position;
	private int nbMechanicalBikes=0;
	private int nbElectricalBikes=0;
	private ArrayList<User> users=new ArrayList<User>();
	private StationType stationType;
	
	public Station(Position position, StationType stationType) {
		this.position = position;
		this.stationType=stationType;
		this.id = StationIdGenerator.getNextStationId();
	}
	
	public int getId() {
		return(this.id);
	}
	
	public Terminal getTerminal() {
		return(terminal);
	}
	
	public ArrayList<Slot> getSlots(){
		return(slots);
	}
	
	public int getNbElectricalBikes() {
		return this.nbElectricalBikes;
	}
	
	public int getNbMechanicalBikes() {
		return this.nbMechanicalBikes;
	}
	
	public Position getPosition() {
		return this.position;
	}
	
	public StationType getStationType(){
		return(stationType);
	}
	
	public int getNbFreeSlots() {
		int nbFreeSlots = 0;
		for (Station.Slot slot : this.slots) {
			if (slot.getState() == SlotState.free) {
				nbFreeSlots += 1;
			}
		}
		
		return nbFreeSlots;
	}
	
	
	public void resetNbSlots() {
		this.nbSlots = 0;
	}
	
	/**
	 * @author Romeo
	 * updates the informations about the station	
	 */
	 
	private void update() {
		
		// we check if at least one slot is online
		this.online=false;
		for (Slot s : slots) {
			if(s.getState()!=SlotState.offline) //if at least one is online, then station is too
				this.online = true;
		}
		
		//we check if terminal is online
		if(!terminal.online) {
			this.setOnline(false);
		}
		
		//we update numbers of available bikes
		nbElectricalBikes=0;
		nbMechanicalBikes=0;
		for (Station.Slot s : slots) {
			if(s.state.equals(SlotState.occupied) && s.bike != null) {
				switch(s.bike.getTypeBike()) {
				case Electrical :  nbElectricalBikes++;break;
				case Mechanical : nbMechanicalBikes++;break;
				}
			}
		}
		
		//we update the lists to compute statistic
		terminal.changingTimes.add(new Time());
		terminal.occupationList.add(nbBikes());
	}
	
	private void notifyUsers() {
		for (User user : users) {
			try { user.updateRide(); }
			catch (NotAnAnswerException e) { e.printErrorMessage(); }
			catch (NoDestinationAssignedException e) { e.printErrorMessage(); }
		}
	}
	
	public void attach(User user) {
		users.add(user);
	}
	
	private void detach(User user) {
		users.remove(user);
	}
	
	public void addSlot() {
		slots.add(new Slot());
	}
	
	/**
	 * add bike to a free slot of this station
	 * @param bike : bike to be added
	 * @throws FullStationException
	 */
	public void initializeElecBike(ElectricalBike bike) throws FullStationException {
		boolean found = false;
		for (Slot slot : slots) {
			if (slot.getState() == SlotState.free && !found) {
				slot.bike = bike;
				slot.setState(SlotState.occupied);
				found = true;
			}
		}
		if(!found) {
			throw new FullStationException();
		}
		update();
	}
	
	/**
	 * add bike to a free slot of this station
	 * @param bike : bike to be added
	 * @throws FullStationException
	 */
	public void initializeMechaBike(MechanicalBike bike)  throws FullStationException {
		boolean found = false;
		for (Slot slot : slots) {
			if (slot.getState() == SlotState.free  && !found) {
				slot.bike = bike;
				slot.setState(SlotState.occupied);
				found = true;
			}
		}
		if(!found) {
			throw new FullStationException();
		}
		update();
	}
	
	/**
	 * add bike to a free slot of this station
	 * @param bike : bike to be added
	 * @throws FullStationException
	 */
	public void initializeBike(Bike bike)  throws FullStationException {
		boolean found = false;
		for (Slot slot : slots) {
			if (slot.getState() == SlotState.free  && !found) {
				slot.bike = bike;
				slot.setState(SlotState.occupied);
				found = true;
			}
		}
		if(!found) {
			throw new FullStationException();
		}
		update();
	}
	
	public int nbBikes() {
		return(nbElectricalBikes + nbMechanicalBikes);
	}
	
	public Station.Slot getFreeSlot() throws FullStationException {
		for(Station.Slot slot : slots) {
			if(slot.getState() == SlotState.free) {
				return slot;
			}
		}
		throw new FullStationException();
	}
	
	/**
	 * called by the slot receiveBike method when it receives a bike
	 * @param bike
	 */
	public void receiveBike(Bike bike, Network network) throws NullPointerException { //could take the id of the bike as an argument and search for the bike instance via the hashMap in MyVelib

			try {
				User user = bike.getUser(network);
				user.returnBike(stationType.giveTimeCredit(), this);
				detach(user);
		 		terminal.returnTimes.add(new Time());
				update();
			} catch(NullPointerException e ) {
				
			}
		
	}
	
	/**
	 * finds a slot with a bike of the right type, and makes the slot release its bike
	 * @param bikeType
	 * @return
	 * @throws NotAvailableBikeException
	 */
	public Bike releaseBike(typeBike bikeType) throws NotAvailableBikeException {
		Bike bike = null;
		Slot slot = slots.get(0);
		boolean notAvailableBike=true;
		for (Slot sl : slots) {
			if(sl.getState()==SlotState.occupied) {
				if(sl.bike.getTypeBike()==bikeType) {
					notAvailableBike=false;
					slot=sl;
				}
			}
		}
		
			if (notAvailableBike) throw new NotAvailableBikeException();
		 	else {
		 		bike = slot.bike;
		 		try{slot.releaseBike();} 
		 		catch(Exception e) {}
		 		terminal.rentTimes.add(new Time());		 	
		}
		
		update();
		return bike;
	}
	
	public String toString() {
		
		return("STATION : "+"Id "+id+" Position "+ position+" nb slots "+slots.size()+ (online ? " online \n":" offline \n"));
	}

	public String displayStats() {
		
		try{
			return("Station " + this.id + " : \n" + "Number of bikes rented : "
		+ this.terminal.nbRents(this.terminal.changingTimes.get(0),new Time())
		+ "\nNumber of bikes returned : " + this.terminal.nbReturns(new Time(0), new Time())
		+ "\nRate of occupation : " + this.terminal.occupationRate(new Time(0), new Time())
		+ "\n");
			}
		catch(MissingDataException e) {return("");}
		}

	public String displayStats(Time startTime,Time endTime) {
		
		try{
			return("Number of bikes rented : "
		+ this.terminal.nbRents(startTime,endTime)
		+ "\nNumber of bikes returned : "+this.terminal.nbReturns(startTime, endTime)
		+ "\nRate of occupation : "+this.terminal.occupationRate(startTime, endTime))
		+ "\n";
	
		} catch(MissingDataException e) {
			return "We lack data to compute this !";
		}
	}
	
	
	public boolean isOnline() {
		return online;
	}
	
	public void setOnline(boolean online) {
		this.online = online;
		if(online == false) {
			notifyUsers();
			System.out.println("Station " + this.id + " is offline.");
			PrintConsole.updateScreenMessage("Station " + this.id + " is offline.");
		}
		else {
			System.out.println("Station " + this.id + " is online.");
			PrintConsole.updateScreenMessage("Station " + this.id + " is online.");
		}
	}

	
	
	public class Terminal{
		
		private boolean online=true;
		private ArrayList<Integer> occupationList = new ArrayList<Integer>(); //nb of bikes, whenever there is a return/rent of bike, it is updated, and so is changingTimes, which means we can find the number of bikes at a certain time
		private ArrayList<Time> changingTimes = new ArrayList<Time>(); //if changingTimes[k]=1h32 and changingTimes[k+1]=1h35, then occupationList[k]=nb of bikes between 1h32 and 1h35
		private ArrayList<Time> rentTimes = new ArrayList<Time>();
		private ArrayList<Time> returnTimes = new ArrayList<Time>();
		
		/**
		 * computes the number of bikes rented from this station between two given times
		 * @param start
		 * @param end
		 * @return
		 * @throws MissingDataException
		 */
		public int nbRents(Time start, Time end) throws MissingDataException {
			
			int nbOfRents = 0;
			for (Time t : rentTimes) {
			
				if (end.greaterThan(t) == true && t.greaterThan(start) == true) {
					
					nbOfRents++;
					
				}
			}
			return(nbOfRents);
		}
		
	/**
	 * computes the number of bikes returned to this station between two given times
	 * @param start
	 * @param end
	 * @return
	 * @throws MissingDataException
	 */
		public int nbReturns(Time start, Time end) throws MissingDataException {
			int nbOfReturns=0;
			
			for (Time t : returnTimes) {
				
				if(t.greaterThan(start) && end.greaterThan(t)) {
					nbOfReturns++;
				}
			}
			
			return(nbOfReturns);
		}
		/**
		 * computes the occupation rate of the station between two given times 
		 * @param start
		 * @param end
		 * @return occupation rate : average number of bikes per minute per slot in this station
		 * @throws MissingDataException
		 */
		public double occupationRate(Time start, Time end) {
			
			
			int idx=1;
			double totalOccupation=0;
			if (changingTimes.size() >= 1) {
				Time t=changingTimes.get(0);
		
				Time timePrecedent;
				/*if(t.greaterThan(start)) {
					throw(new MissingDataException());
				}*/
				while(t.greaterThan(start) == false) {
					t = changingTimes.get(idx);
					idx++;
				}
				if(!t.greaterThan(start)) {
					return(0);
				}
				
				totalOccupation=0;
				timePrecedent=start;
				while(!t.greaterThan(end) == false) {
					totalOccupation+=(occupationList.get(idx-1))*(t.computeRidingTime(timePrecedent));				
					timePrecedent=t;
					idx++;
					t = changingTimes.get(idx);
				}
				totalOccupation+=(occupationList.get(idx-1))*(end.computeRidingTime(timePrecedent));
				
				return(totalOccupation/(nbSlots*end.computeRidingTime(start)));
			}
			return 0;
			
		}
		
		public void setOnline(boolean online) {
			this.online=online;
			update();
		}
		
	}
	
	
	public class Slot {
		/**
		 * Slot object, where you can park bikes
		 * attribute bike is the bike currently parked on this slot, null if slot is free
		 */
		protected SlotState state;
		protected int id;
		protected Bike bike;
		
		public Slot() {
			nbSlots++;
			id = nbSlots;
			bike = null;
			state = SlotState.free;
		}

		/**
		 * @author Ro
		 * @param bike the bike received
		 * @throws OccupiedException
		 * @throws OfflineException
		 * used when a user returns its bike to this slot
		 */
		public void receiveBike(Bike bike, Network network) throws OccupiedException, OfflineException {
			
			
			if(this.state == SlotState.occupied) {
				throw (new OccupiedException());
			}
			if(this.state == SlotState.offline) {
				throw (new OfflineException());
			}
			
			else {
				String message = "Bike parked";
				System.out.println(message);
				PrintConsole.updateScreenMessage(message);
				this.bike = bike;
				state = SlotState.occupied;
				Station.this.receiveBike(bike, network);
			}
			
		}
		/**
		 * @author Romeo
		 * @throws OfflineException
		 * @throws FreeException
		 * used when the slot should release the bike (when a user has ordered it)
		 */
		public synchronized void releaseBike() throws OfflineException, FreeException {
			
			String message = "Bike released";
			System.out.println(message);
			PrintConsole.updateScreenMessage(message);;
			
			if(this.state == SlotState.offline) {
				throw new OfflineException ();
			}
			
			if(this.state == SlotState.free) {
				throw new FreeException();
			}

			bike = null;
			state = SlotState.free;
			
		}
		

		public void setState(SlotState state) {
			this.state = state;
			Station.this.update();
		}
		
		public SlotState getState() {
			return this.state;
		}
		
		public String toString() {
			
			String slotState="";
			switch (this.getState()) {
				case free: slotState="free"; break;
				case occupied: slotState="occupied"; break;
				case offline: slotState="offline"; break;
			}
			return("      SLOT : "+"Id "+ this.id + " " + slotState + "\n");
		}
	}
}