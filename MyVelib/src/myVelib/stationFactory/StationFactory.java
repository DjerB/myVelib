package myVelib.stationFactory;

import myVelib.utilities.*;
import myVelib.station.*;

public abstract class StationFactory {
	
	public abstract Station createStation(Position position);
	
}
