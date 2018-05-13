package myVelib.stationFactory;

import myVelib.station.PlusStation;
import myVelib.station.Station;
import myVelib.utilities.Position;

/**
 * Creates automatically a station of type plus
 * @author Ahmed Djermani
 *
 */
public class PlusStationFactory extends StationFactory {
	
	public Station createStation(Position position) {
		Station station = new Station(position, new PlusStation());
		return station;
	}
}
