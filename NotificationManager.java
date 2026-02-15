
import javax.swing.*;
import java.time.LocalDateTime;
import java.time.Duration;
import java.util.Timer;
import java.util.TimerTask;

/**
 * NotificationManager - Manages automatic notifications for reservations
 * Sends alerts 10 minutes before reservation time
 */
public class NotificationManager {

    private static Timer timer = new Timer(true); // Daemon thread

    /**
     * Schedule a notification for 10 minutes before reservation time
     */
    public static void scheduleReservationReminder(Reservation reservation) {
        LocalDateTime notificationTime = reservation.getStartTime().minusMinutes(10);
        LocalDateTime now = LocalDateTime.now();

        // Check if notification time is in the future
        if (notificationTime.isAfter(now)) {
            long delayMillis = Duration.between(now, notificationTime).toMillis();

            TimerTask task = new TimerTask() {
                @Override
                public void run() {
                    showNotification(reservation);
                }
            };

            timer.schedule(task, delayMillis);
            System.out.println("Notification scheduled for: " + notificationTime);
        } else {
            System.out.println("Notification time already passed, skipping.");
        }
    }

    /**
     * Show notification dialog
     */
    private static void showNotification(Reservation reservation) {
        SwingUtilities.invokeLater(() -> {
            String message = String.format(
                    "Reservation Reminder!\n\n" +
                            "Your reservation starts in 10 minutes:\n\n" +
                            "Station: %s\n" +
                            "Time: %s\n" +
                            "Duration: %d minutes\n" +
                            "Price: %.2f baht\n\n" +
                            "Please head to the station now!",
                    reservation.getStationID(),
                    reservation.getStartTime().toLocalTime(),
                    Duration.between(reservation.getStartTime(), reservation.getEndTime()).toMinutes(),
                    reservation.getPrice());

            JOptionPane.showMessageDialog(
                    null,
                    message,
                    "Reservation Reminder - 10 Minutes",
                    JOptionPane.INFORMATION_MESSAGE);
        });
    }

    /**
     * Cancel all scheduled notifications
     */
    public static void cancelAll() {
        timer.cancel();
        timer = new Timer(true);
    }

    /**
     * Check upcoming reservations for current user
     */
    public static void checkUpcomingReservations(String userId) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime checkUntil = now.plusHours(24); // Check next 24 hours

        for (Reservation r : Reservation.allReservations) {
            if (r.getUserID().equals(userId) &&
                    r.getStatus().equals("Confirmed") &&
                    r.getStartTime().isAfter(now) &&
                    r.getStartTime().isBefore(checkUntil)) {

                scheduleReservationReminder(r);
            }
        }
    }
}
