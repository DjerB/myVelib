package myVelib.stationFactory;

import myVelib.station.StandardStation;
import myVelib.station.Station;
import myVelib.utilities.*;

/**
 * Creates automatically a station of type standard
 * @author Ahmed Djermani
 *
 */
public class StandardStationFactory extends StationFactory {
	
	public Station createStation(Position position) {
		Station station = new Station(position, new StandardStation());
		return station;
	}
}
