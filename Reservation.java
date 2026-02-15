
import java.time.LocalDateTime;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class Reservation {
    public static List<Reservation> allReservations = new ArrayList<>();

    private String reservationID;
    private String userID;
    private String stationID;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String status;
    private double price; // Price in baht

    public Reservation(String resId, String userId, String stationId, LocalDateTime start, LocalDateTime end,
            String status) {
        this.reservationID = resId;
        this.userID = userId;
        this.stationID = stationId;
        this.startTime = start;
        this.endTime = end;
        this.status = status;

        // Calculate price: 5 baht per minute
        long minutes = Duration.between(start, end).toMinutes();
        this.price = minutes * 5.0;
    }

    public Reservation() {
    }

    // Getters
    public String getReservationID() {
        return reservationID;
    }

    public String getUserID() {
        return userID;
    }

    public String getStationID() {
        return stationID;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public String getStatus() {
        return status;
    }

    public double getPrice() {
        return price;
    }

    // Setters
    public void setReservationID(String id) {
        reservationID = id;
    }

    public void setUserID(String id) {
        userID = id;
    }

    public void setStationID(String id) {
        stationID = id;
    }

    public void setStartTime(LocalDateTime start) {
        this.startTime = start;
    }

    public void setEndTime(LocalDateTime end) {
        this.endTime = end;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void createReservation() throws Exception {
        // Check availability
        if (ReservationManager.isSlotAvailable(this.stationID, this.startTime, this.endTime)) {
            // Save reservation
            allReservations.add(this);
            this.status = "Pending"; // Changed to Pending until payment confirmed

            System.out.println(
                    "Reservation Created (Pending): " + this.reservationID + " - Price: " + this.price + " baht");
        } else {
            throw new Exception("The selected slot is not available.");
        }
    }

    public void cancelReservation() {
        this.status = "Cancelled";
        System.out.println("Reservation Cancelled: " + this.reservationID);
    }

    public void checkReservationStatus() {
        System.out.println("Reservation ID " + reservationID + " Status: " + status);
    }
}
