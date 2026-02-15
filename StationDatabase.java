
import java.util.ArrayList;
import java.util.List;

public class StationDatabase {
    public static List<ChargingStationInfo> stations = new ArrayList<>();

    static {
        stations.add(new ChargingStationInfo("Station A - Central World", new String[] { "CCS Type 2", "CHAdeMO" }));
        stations.add(new ChargingStationInfo("Station B - Siam Paragon", new String[] { "Type 2", "CCS Type 2" }));
        stations.add(new ChargingStationInfo("Station C - Terminal 21", new String[] { "CHAdeMO", "Type 2" }));
        stations.add(new ChargingStationInfo("Station D - Mega Bangna", new String[] { "CCS Type 2", "Type 2" }));
        stations.add(new ChargingStationInfo("Station E - IconSiam", new String[] { "Type 2", "CHAdeMO" }));
    }

    public static List<ChargingStationInfo> getAllStations() {
        return new ArrayList<>(stations);
    }

    public static ChargingStationInfo getStationByName(String name) {
        for (ChargingStationInfo station : stations) {
            if (station.getName().equalsIgnoreCase(name.trim())) {
                return station;
            }
        }
        return null;
    }
}
