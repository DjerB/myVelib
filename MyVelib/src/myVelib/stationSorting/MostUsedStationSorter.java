package myVelib.stationSorting;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import myVelib.system.*;
import myVelib.station.Station;
import myVelib.utilities.*;
import myVelib.exceptions.*;


public class MostUsedStationSorter implements StationSorter {
	
	Network network;
	Time start;
	Time end = new Time();
	
	public MostUsedStationSorter(Network network) {
		this.network = network;
		start = this.network.getBeginningTime();
	}
	
	class MostUsedComparator implements Comparator<Station>{

		@Override
		public int compare(Station o1, Station o2) {
			try{
				if(o1.getTerminal().nbRents(start, end)+o1.getTerminal().nbReturns(start,end)==o2.getTerminal().nbRents(start, end)+o2.getTerminal().nbReturns(start, end)) {
					return(0);
				}
				if(o1.getTerminal().nbRents(start, end)+o1.getTerminal().nbReturns(start,end)>o2.getTerminal().nbRents(start, end)+o2.getTerminal().nbReturns(start, end)) {
					return(+1);
				}
				else {
					return(-1);
				}
			}
			catch(MissingDataException e) {return(0);}
		}		
	}

	@Override
	public List<Station> sort() {
		
		List<Station> stations = new ArrayList<Station>(this.network.getStations().values());
		Collections.sort(stations, new MostUsedComparator());
		
		return(stations);
		
	}

}
