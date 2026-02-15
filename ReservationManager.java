import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class ReservationManager {

    public static boolean isSlotAvailable(String stationID, LocalDateTime start, LocalDateTime end) {
        if (start == null || end == null ||
                start.isBefore(LocalDateTime.now().minusSeconds(1)) ||
                end.isBefore(start)) {

            System.err.println("Error: Invalid date/time input (e.g., in the past or end before start).");
            return false;
        }

        for (Reservation res : Reservation.allReservations) {
            if (res.getStationID().equals(stationID) &&
                    (res.getStatus().equals("Confirmed") || res.getStatus().equals("Pending"))) {

                if (start.isBefore(res.getEndTime()) && end.isAfter(res.getStartTime())) {
                    System.out.println("Conflict found at Station " + stationID + " with existing reservation ID: "
                            + res.getReservationID());
                    return false;
                }
            }
        }
        return true;
    }

    public static List<Reservation> getHistory(String userID) {
        return Reservation.allReservations.stream()
                .filter(r -> r.getUserID().equals(userID))
                .collect(Collectors.toList());
    }
}