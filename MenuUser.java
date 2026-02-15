
import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * MenuUser - Modern main menu for users using ModernTheme
 */
public class MenuUser extends JFrame {

    private JTextArea reservationArea;

    public MenuUser() {
        initComponents();
        loadReservations();

        if (UserInfo.currentUser != null) {
            NotificationManager.checkUpcomingReservations(UserInfo.currentUser.getUsername());
        }
    }

    private void initComponents() {
        setTitle("User Dashboard - " + AppConstants.APP_NAME);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 700);
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

        // Content Panel
        JPanel contentPanel = new JPanel();
        contentPanel.setOpaque(false);
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));

        // Header
        String userName = UserInfo.currentUser != null ? UserInfo.currentUser.getName() : "Guest";
        JLabel welcomeLabel = new JLabel("Welcome, " + userName);
        welcomeLabel.setFont(ModernTheme.FONT_TITLE);
        welcomeLabel.setForeground(ModernTheme.COLOR_NEON_CYAN);
        welcomeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton logoutButton = ModernTheme.createNeonButton("Logout", ModernTheme.COLOR_NEON_RED);
        logoutButton.setPreferredSize(new Dimension(120, 40));
        logoutButton.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this,
                    "Do you want to logout?",
                    "Confirm Logout",
                    JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                UserInfo.logout();
                new Intro().setVisible(true);
                this.dispose();
            }
        });

        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);
        headerPanel.setMaximumSize(new Dimension(800, 60));
        headerPanel.add(welcomeLabel, BorderLayout.WEST);
        headerPanel.add(logoutButton, BorderLayout.EAST);

        contentPanel.add(headerPanel);
        contentPanel.add(Box.createVerticalStrut(30));

        // Reservation Info Card
        JPanel infoCard = ModernTheme.createGlassPanel();
        infoCard.setLayout(new BorderLayout());
        infoCard.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel infoLabel = new JLabel("Your Active Reservations");
        infoLabel.setFont(ModernTheme.FONT_HEADER);
        infoLabel.setForeground(Color.WHITE);
        infoCard.add(infoLabel, BorderLayout.NORTH);

        reservationArea = new JTextArea();
        reservationArea.setFont(new Font("Consolas", Font.PLAIN, 14));
        reservationArea.setEditable(false);
        reservationArea.setBackground(new Color(60, 60, 60)); // Dark gray background
        reservationArea.setOpaque(true);
        reservationArea.setForeground(Color.WHITE);
        reservationArea.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JScrollPane scrollPane = new JScrollPane(reservationArea);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setBorder(null);
        scrollPane.setPreferredSize(new Dimension(700, 200));

        infoCard.add(scrollPane, BorderLayout.CENTER);

        contentPanel.add(infoCard);
        contentPanel.add(Box.createVerticalStrut(30));

        // Action Buttons
        JPanel buttonPanel = new JPanel(new GridLayout(1, 3, 20, 0));
        buttonPanel.setOpaque(false);
        buttonPanel.setMaximumSize(new Dimension(800, 60));

        JButton searchButton = ModernTheme.createNeonButton("Search Stations", ModernTheme.COLOR_NEON_CYAN);
        JButton historyButton = ModernTheme.createNeonButton("History", ModernTheme.COLOR_NEON_GREEN);
        JButton refreshButton = ModernTheme.createNeonButton("Refresh", Color.ORANGE);

        searchButton.addActionListener(e -> {
            new SearchFromName().setVisible(true);
            this.dispose();
        });

        historyButton.addActionListener(e -> {
            new ReserveHistory().setVisible(true);
            this.dispose();
        });

        refreshButton.addActionListener(e -> {
            loadReservations();
            JOptionPane.showMessageDialog(this, "Data refreshed successfully!");
        });

        buttonPanel.add(searchButton);
        buttonPanel.add(historyButton);
        buttonPanel.add(refreshButton);

        contentPanel.add(buttonPanel);
        contentPanel.add(Box.createVerticalGlue());

        mainPanel.add(contentPanel, BorderLayout.CENTER);

        setContentPane(mainPanel);
    }

    private void loadReservations() {
        if (UserInfo.currentUser == null) {
            reservationArea.setText("Please login first");
            return;
        }

        String userId = UserInfo.currentUser.getUsername();
        List<Reservation> userReservations = ReservationManager.getHistory(userId);

        StringBuilder sb = new StringBuilder();

        if (userReservations.isEmpty()) {
            sb.append("No reservations yet\n\n");
            sb.append("Click 'Search Stations' to start booking!");
        } else {
            int active = 0;
            for (Reservation r : userReservations) {
                if (r.getStatus().equals("Confirmed") || r.getStatus().equals("Pending")) {
                    sb.append("Station: ").append(r.getStationID()).append("\n");
                    sb.append("   Date: ").append(r.getStartTime().toLocalDate()).append("\n");
                    sb.append("   Time: ").append(r.getStartTime().toLocalTime())
                            .append(" - ").append(r.getEndTime().toLocalTime()).append("\n");
                    sb.append("   Status: ").append(r.getStatus()).append("\n\n");
                    active++;
                }
            }

            if (active == 0) {
                sb.append("No active reservations\n\n");
                sb.append("View your history to see past reservations");
            } else {
                sb.append("Total active reservations: ").append(active);
            }
        }

        reservationArea.setText(sb.toString());
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new MenuUser().setVisible(true);
        });
    }
}
