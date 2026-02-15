
import javax.swing.*;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * ReservationPage - Modern reservation form using ModernTheme
 */
public class ReservationPage extends JFrame {

        private JComboBox<String> hourField, minuteField;
        private JTextField userField, stationField, chargingTimeField, endTimeField;
        private String stationName;

        public ReservationPage() {
                this("");
        }

        public ReservationPage(String selectedStation) {
                this.stationName = selectedStation;
                initComponents();
        }

        private void initComponents() {
                setTitle("Make Reservation - " + AppConstants.APP_NAME);
                setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                setSize(700, 800);
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

                // Content Wrapper
                JPanel contentWrapper = new JPanel();
                contentWrapper.setOpaque(false);
                contentWrapper.setLayout(new BoxLayout(contentWrapper, BoxLayout.Y_AXIS));

                // Header
                JLabel titleLabel = new JLabel("Reservation Form");
                titleLabel.setFont(ModernTheme.FONT_TITLE);
                titleLabel.setForeground(ModernTheme.COLOR_NEON_GREEN);
                titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

                JLabel subtitleLabel = new JLabel("Book your charging station");
                subtitleLabel.setFont(ModernTheme.FONT_BODY);
                subtitleLabel.setForeground(ModernTheme.COLOR_TEXT_SECONDARY);
                subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

                contentWrapper.add(titleLabel);
                contentWrapper.add(Box.createVerticalStrut(10));
                contentWrapper.add(subtitleLabel);
                contentWrapper.add(Box.createVerticalStrut(20));

                // Form Card
                JPanel formCard = ModernTheme.createGlassPanel();
                formCard.setLayout(new GridBagLayout());
                formCard.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));

                GridBagConstraints gbc = new GridBagConstraints();
                gbc.fill = GridBagConstraints.HORIZONTAL;
                gbc.gridx = 0;
                gbc.insets = new Insets(10, 0, 5, 0);

                // User field
                addFormField(formCard, gbc, "User:", userField = ModernTheme.createStyledTextField(), false);
                if (UserInfo.currentUser != null) {
                        userField.setText(UserInfo.currentUser.getUsername());
                }
                userField.setEditable(false);

                // Station field
                addFormField(formCard, gbc, "Charging Station:", stationField = ModernTheme.createStyledTextField(),
                                true);
                stationField.setText(stationName.isEmpty() ? "Station A - Central World" : stationName);

                // Charging duration
                addFormField(formCard, gbc, "Duration (min. 60 minutes):",
                                chargingTimeField = ModernTheme.createStyledTextField(), true);
                chargingTimeField.setText("60");

                // Date field
                JTextField dateField = ModernTheme.createStyledTextField();
                dateField.setText(java.time.LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
                addFormField(formCard, gbc, "Date (DD/MM/YYYY):", dateField, true);

                // Start time (Hour and Minute Dropdowns)
                JPanel timePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
                timePanel.setOpaque(false);

                String[] hours = new String[24];
                for (int i = 0; i < 24; i++)
                        hours[i] = String.format("%02d", i);
                hourField = ModernTheme.createStyledComboBox(hours);
                hourField.setPreferredSize(new Dimension(80, 40));

                String[] minutes = new String[60];
                for (int i = 0; i < 60; i++)
                        minutes[i] = String.format("%02d", i);
                minuteField = ModernTheme.createStyledComboBox(minutes);
                minuteField.setPreferredSize(new Dimension(80, 40));

                JLabel sepLabel = new JLabel(":");
                sepLabel.setFont(ModernTheme.FONT_HEADER);
                sepLabel.setForeground(Color.WHITE);

                timePanel.add(hourField);
                timePanel.add(sepLabel);
                timePanel.add(minuteField);

                addFormField(formCard, gbc, "Start Time (HH : MM):", timePanel, true);

                // End time (auto-calculated)
                addFormField(formCard, gbc, "End Time (auto):", endTimeField = ModernTheme.createStyledTextField(),
                                false);
                endTimeField.setEditable(false);

                contentWrapper.add(formCard);
                contentWrapper.add(Box.createVerticalStrut(20));

                // Buttons
                JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
                buttonPanel.setOpaque(false);

                JButton backButton = ModernTheme.createNeonButton("Back", Color.GRAY);
                JButton reserveButton = ModernTheme.createNeonButton("Reserve Now", ModernTheme.COLOR_NEON_GREEN);
                reserveButton.setPreferredSize(new Dimension(200, 45));

                backButton.addActionListener(e -> {
                        new SearchFromName().setVisible(true);
                        this.dispose();
                });

                reserveButton.addActionListener(e -> handleReservation(dateField));

                buttonPanel.add(backButton);
                buttonPanel.add(reserveButton);

                contentWrapper.add(buttonPanel);
                contentWrapper.add(Box.createVerticalGlue());

                // Scroll pane
                JScrollPane scrollPane = new JScrollPane(contentWrapper);
                scrollPane.setOpaque(false);
                scrollPane.getViewport().setOpaque(false);
                scrollPane.setBorder(null);
                scrollPane.getVerticalScrollBar().setUnitIncrement(16);

                mainPanel.add(scrollPane, BorderLayout.CENTER);

                setContentPane(mainPanel);
        }

        private String[] generateTimeSlots() {
                String[] slots = new String[96]; // 24 * 4 = 96 slots (15 min intervals)
                int index = 0;
                for (int h = 0; h < 24; h++) {
                        for (int m = 0; m < 60; m += 15) {
                                slots[index++] = String.format("%02d:%02d", h, m);
                        }
                }
                return slots;
        }

        private void addFormField(JPanel panel, GridBagConstraints gbc, String labelText, JComponent field,
                        boolean editable) {
                gbc.gridy++;
                gbc.insets = new Insets(10, 0, 5, 0);
                JLabel label = new JLabel(labelText);
                label.setFont(ModernTheme.FONT_BODY);
                label.setForeground(Color.WHITE);
                panel.add(label, gbc);

                gbc.gridy++;
                gbc.insets = new Insets(0, 0, 10, 0);
                panel.add(field, gbc);
        }

        private void handleReservation(JTextField dateField) {
                String userId = userField.getText().trim();
                String stationId = stationField.getText().trim();
                String chargingTimeStr = chargingTimeField.getText().trim();

                String hour = (String) hourField.getSelectedItem();
                String minute = (String) minuteField.getSelectedItem();
                String startTimeStr = hour + ":" + minute;

                String dateStr = dateField.getText().trim();

                DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
                DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                LocalDateTime start;
                LocalDateTime end;

                try {
                        if (userId.isEmpty() || stationId.isEmpty() || dateStr.isEmpty()) {
                                JOptionPane.showMessageDialog(this,
                                                "Please fill in all required fields",
                                                "Incomplete Data",
                                                JOptionPane.WARNING_MESSAGE);
                                return;
                        }

                        int chargingMinutes = Integer.parseInt(chargingTimeStr);
                        if (chargingMinutes < 60) {
                                JOptionPane.showMessageDialog(this,
                                                "Minimum charging time is 60 minutes (1 hour)",
                                                "Invalid Charging Time",
                                                JOptionPane.WARNING_MESSAGE);
                                return;
                        }

                        java.time.LocalDate date = java.time.LocalDate.parse(dateStr, dateFormatter);
                        java.time.LocalTime time = java.time.LocalTime.parse(startTimeStr, timeFormatter);
                        start = java.time.LocalDateTime.of(date, time);

                        if (start.isBefore(LocalDateTime.now())) {
                                JOptionPane.showMessageDialog(this,
                                                "Cannot make a reservation in the past.\nPlease check date and time.",
                                                "Invalid Time",
                                                JOptionPane.WARNING_MESSAGE);
                                return;
                        }

                        end = start.plusMinutes(chargingMinutes);
                        endTimeField.setText(end.toLocalTime().format(timeFormatter));

                        Reservation newReservation = new Reservation(
                                        "RES_" + System.currentTimeMillis(),
                                        userId,
                                        stationId,
                                        start,
                                        end,
                                        "Pending");

                        newReservation.createReservation();

                        JOptionPane.showMessageDialog(this,
                                        "Reservation Created!\n\n" +
                                                        "ID: " + newReservation.getReservationID() + "\n" +
                                                        "Station: " + stationId + "\n" +
                                                        "Date: " + start.toLocalDate() + "\n" +
                                                        "Time: " + start.toLocalTime() + " - " + end.toLocalTime()
                                                        + "\n" +
                                                        "Duration: " + chargingMinutes + " minutes\n" +
                                                        "Price: " + String.format("%.2f", newReservation.getPrice())
                                                        + " baht",
                                        "Success",
                                        JOptionPane.INFORMATION_MESSAGE);

                        new PaymentPage(newReservation).setVisible(true);
                        this.dispose();

                } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(this,
                                        "Charging time must be a valid number (minimum 60 minutes)",
                                        "Invalid Input",
                                        JOptionPane.ERROR_MESSAGE);
                } catch (java.time.format.DateTimeParseException ex) {
                        JOptionPane.showMessageDialog(this,
                                        "Invalid date format.\nUse: DD/MM/YYYY\nExample: 25/12/2025",
                                        "Format Error",
                                        JOptionPane.ERROR_MESSAGE);
                } catch (Exception ex) {
                        JOptionPane.showMessageDialog(this,
                                        "Reservation failed: " + ex.getMessage(),
                                        "Error",
                                        JOptionPane.ERROR_MESSAGE);
                }
        }

        public static void main(String[] args) {
                SwingUtilities.invokeLater(() -> {
                        new ReservationPage().setVisible(true);
                });
        }
}
