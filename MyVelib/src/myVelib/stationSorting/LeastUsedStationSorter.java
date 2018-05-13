package myVelib.stationSorting;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


import myVelib.system.*;
import myVelib.station.Station;
import myVelib.utilities.*;
import myVelib.exceptions.*;

public class LeastUsedStationSorter implements StationSorter {
	
	Network network;
	Time start;
	Time end =  new Time();
	
	public LeastUsedStationSorter(Network network) {
		this.network = network;
		this.start = network.getBeginningTime();
	}
	

	
	class LeastUsedComparator implements Comparator<Station>{

		@Override
		public int compare(Station o1, Station o2) {
			
				if(o1.getTerminal().occupationRate(start,end)==o2.getTerminal().occupationRate(start, end)) {
					return(0);
				}
				if(o1.getTerminal().occupationRate(start,end)<o2.getTerminal().occupationRate(start, end)) { //if least occupied, o1 is superior to o2
					return(+1);
				}
				else {
					return(-1);
				}
			
		}
		
	}
	
	@Override
	public List<Station> sort() {
		
		List<Station> stations = new ArrayList<Station>(this.network.getStations().values());
		Collections.sort(stations, new LeastUsedComparator());
		
		return(stations);
	}
	
	

}
