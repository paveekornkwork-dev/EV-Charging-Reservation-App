
import javax.swing.*;
import java.awt.*;

/**
 * PaymentPage - Modern payment confirmation using ModernTheme
 */
public class PaymentPage extends JFrame {

    private Reservation reservation;

    public PaymentPage(Reservation res) {
        this.reservation = res;
        initComponents();
    }

    private void initComponents() {
        setTitle("Payment - " + AppConstants.APP_NAME);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 750);
        setResizable(false);

        // Apply frameless style
        ModernTheme.applyCommonFrameStyle(this);
        setLocationRelativeTo(null);

        // Main Panel
        JPanel mainPanel = ModernTheme.createMainPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Window Controls
        JPanel controlsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        controlsPanel.setOpaque(false);
        JButton closeBtn = ModernTheme.createControlButton("X", Color.RED, () -> System.exit(0));
        controlsPanel.add(closeBtn);
        mainPanel.add(controlsPanel, BorderLayout.NORTH);

        // Content Panel (centered)
        JPanel contentWrapper = new JPanel();
        contentWrapper.setOpaque(false);
        contentWrapper.setLayout(new BoxLayout(contentWrapper, BoxLayout.X_AXIS));

        contentWrapper.add(Box.createHorizontalGlue()); // Add glue to center

        JPanel contentPanel = new JPanel();
        contentPanel.setOpaque(false);
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));

        // Header
        JLabel titleLabel = new JLabel("Confirm Reservation");
        titleLabel.setFont(ModernTheme.FONT_TITLE);
        titleLabel.setForeground(ModernTheme.COLOR_NEON_GREEN);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subtitleLabel = new JLabel("Review and complete payment");
        subtitleLabel.setFont(ModernTheme.FONT_BODY);
        subtitleLabel.setForeground(ModernTheme.COLOR_TEXT_SECONDARY);
        subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        contentPanel.add(titleLabel);
        contentPanel.add(Box.createVerticalStrut(10));
        contentPanel.add(subtitleLabel);
        contentPanel.add(Box.createVerticalStrut(30));

        // Details Card
        JPanel detailsCard = ModernTheme.createGlassPanel();
        detailsCard.setLayout(new BoxLayout(detailsCard, BoxLayout.Y_AXIS));
        detailsCard.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));
        detailsCard.setMaximumSize(new Dimension(500, 400));

        long minutes = java.time.Duration.between(reservation.getStartTime(), reservation.getEndTime()).toMinutes();

        addDetail(detailsCard, "Reservation ID:", reservation.getReservationID());
        addDetail(detailsCard, "Station:", reservation.getStationID());
        addDetail(detailsCard, "Date:", reservation.getStartTime().toLocalDate().toString());
        addDetail(detailsCard, "Start Time:", reservation.getStartTime().toLocalTime().toString());
        addDetail(detailsCard, "End Time:", reservation.getEndTime().toLocalTime().toString());
        addDetail(detailsCard, "Duration:", minutes + " minutes");

        detailsCard.add(Box.createVerticalStrut(10));
        JPanel pricePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        pricePanel.setOpaque(false);
        JLabel priceLabel = new JLabel("Total: " + String.format("%.2f", reservation.getPrice()) + " baht");
        priceLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        priceLabel.setForeground(ModernTheme.COLOR_NEON_GREEN);
        pricePanel.add(priceLabel);
        detailsCard.add(pricePanel);

        contentPanel.add(detailsCard);
        contentPanel.add(Box.createVerticalStrut(20));

        // QR Code Placeholder
        JPanel qrCard = ModernTheme.createGlassPanel();
        qrCard.setLayout(new GridBagLayout());
        qrCard.setMaximumSize(new Dimension(280, 280));
        qrCard.setPreferredSize(new Dimension(280, 280));

        JLabel qrLabel = new JLabel("<html><center>QR CODE<br/>PAYMENT<br/><br/>Scan to pay<br/>" +
                String.format("%.2f baht", reservation.getPrice()) +
                "</center></html>");
        qrLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        qrLabel.setForeground(Color.WHITE);
        qrCard.add(qrLabel);

        contentPanel.add(qrCard);
        contentPanel.add(Box.createVerticalStrut(30));

        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        buttonPanel.setOpaque(false);

        JButton cancelButton = ModernTheme.createNeonButton("Cancel", ModernTheme.COLOR_NEON_RED);
        JButton confirmButton = ModernTheme.createNeonButton("Confirm Payment", ModernTheme.COLOR_NEON_GREEN);
        confirmButton.setPreferredSize(new Dimension(200, 45));

        cancelButton.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this,
                    "Cancel this reservation?",
                    "Confirm Cancellation",
                    JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION) {
                try {
                    reservation.cancelReservation();
                    JOptionPane.showMessageDialog(this, "Reservation cancelled");
                    new MenuUser().setVisible(true);
                    this.dispose();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
                }
            }
        });

        confirmButton.addActionListener(e -> {
            // Confirm payment - set status and schedule notification
            reservation.setStatus("Confirmed");
            NotificationManager.scheduleReservationReminder(reservation);

            JOptionPane.showMessageDialog(this,
                    "Payment confirmed!\n\n" +
                            "Reservation ID: " + reservation.getReservationID() + "\n" +
                            "Amount: " + String.format("%.2f", reservation.getPrice()) + " baht\n\n" +
                            "You will receive a reminder 10 minutes before your reservation.\n\n" +
                            "Thank you for using our service!",
                    "Payment Success",
                    JOptionPane.INFORMATION_MESSAGE);

            new MenuUser().setVisible(true);
            this.dispose();
        });

        buttonPanel.add(cancelButton);
        buttonPanel.add(confirmButton);

        contentPanel.add(buttonPanel);
        contentPanel.add(Box.createVerticalGlue());

        contentWrapper.add(contentPanel);
        contentWrapper.add(Box.createHorizontalGlue()); // Add glue to center

        mainPanel.add(contentWrapper, BorderLayout.CENTER);

        setContentPane(mainPanel);
    }

    private void addDetail(JPanel panel, String label, String value) {
        JPanel detailPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        detailPanel.setOpaque(false);
        detailPanel.setMaximumSize(new Dimension(450, 30));

        JLabel labelComp = new JLabel(label);
        labelComp.setFont(ModernTheme.FONT_BODY);
        labelComp.setForeground(ModernTheme.COLOR_TEXT_SECONDARY);
        labelComp.setPreferredSize(new Dimension(140, 25));

        JLabel valueComp = new JLabel(value);
        valueComp.setFont(new Font("Segoe UI", Font.BOLD, 14));
        valueComp.setForeground(Color.WHITE);

        detailPanel.add(labelComp);
        detailPanel.add(valueComp);
        detailPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        panel.add(detailPanel);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Reservation testRes = new Reservation(
                    "TEST123",
                    "testuser",
                    "Station A - Central World",
                    java.time.LocalDateTime.now().plusHours(1),
                    java.time.LocalDateTime.now().plusHours(2),
                    "Pending");
            new PaymentPage(testRes).setVisible(true);
        });
    }
}
