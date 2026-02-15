
import javax.swing.*;
import java.awt.*;

/**
 * MenuAdmin - Modern main menu for admins using ModernTheme
 */
public class MenuAdmin extends JFrame {

    private JTextArea infoArea;

    public MenuAdmin() {
        initComponents();
        loadSystemInfo();
    }

    private void initComponents() {
        setTitle("Admin Dashboard - " + AppConstants.APP_NAME);
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
        String adminName = UserInfo.currentUser != null ? UserInfo.currentUser.getName() : "Admin";
        JLabel titleLabel = new JLabel("Administrator: " + adminName);
        titleLabel.setFont(ModernTheme.FONT_TITLE);
        titleLabel.setForeground(ModernTheme.COLOR_NEON_RED);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

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
        headerPanel.add(titleLabel, BorderLayout.WEST);
        headerPanel.add(logoutButton, BorderLayout.EAST);

        contentPanel.add(headerPanel);
        contentPanel.add(Box.createVerticalStrut(30));

        // System Info Card
        JPanel infoCard = ModernTheme.createGlassPanel();
        infoCard.setLayout(new BorderLayout());
        infoCard.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel infoLabel = new JLabel("System Information");
        infoLabel.setFont(ModernTheme.FONT_HEADER);
        infoLabel.setForeground(Color.WHITE);
        infoCard.add(infoLabel, BorderLayout.NORTH);

        infoArea = new JTextArea();
        infoArea.setFont(new Font("Consolas", Font.PLAIN, 14));
        infoArea.setEditable(false);
        infoArea.setBackground(new Color(60, 60, 60)); // Dark gray background
        infoArea.setOpaque(true);
        infoArea.setForeground(Color.WHITE);
        infoArea.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JScrollPane scrollPane = new JScrollPane(infoArea);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setBorder(null);
        scrollPane.setPreferredSize(new Dimension(700, 150));

        infoCard.add(scrollPane, BorderLayout.CENTER);

        contentPanel.add(infoCard);
        contentPanel.add(Box.createVerticalStrut(30));

        // Management Buttons
        JPanel buttonPanel = new JPanel(new GridLayout(4, 1, 0, 15));
        buttonPanel.setOpaque(false);
        buttonPanel.setMaximumSize(new Dimension(600, 250));

        JButton viewUsersButton = ModernTheme.createNeonButton("Manage Users", ModernTheme.COLOR_NEON_CYAN);
        JButton viewStationsButton = ModernTheme.createNeonButton("Manage Stations", ModernTheme.COLOR_NEON_GREEN);
        JButton viewReservationsButton = ModernTheme.createNeonButton("View All Reservations", Color.ORANGE);
        JButton refreshButton = ModernTheme.createNeonButton("Refresh Data", Color.GRAY);

        viewUsersButton.addActionListener(e -> showAllUsers());
        viewStationsButton.addActionListener(e -> showAllStations());
        viewReservationsButton.addActionListener(e -> showAllReservations());
        refreshButton.addActionListener(e -> {
            loadSystemInfo();
            JOptionPane.showMessageDialog(this, "Data refreshed successfully");
        });

        buttonPanel.add(viewUsersButton);
        buttonPanel.add(viewStationsButton);
        buttonPanel.add(viewReservationsButton);
        buttonPanel.add(refreshButton);

        contentPanel.add(buttonPanel);
        contentPanel.add(Box.createVerticalGlue());

        mainPanel.add(contentPanel, BorderLayout.CENTER);

        setContentPane(mainPanel);
    }

    private void loadSystemInfo() {
        int totalUsers = UserInfo.users.size();
        int totalStations = StationDatabase.stations.size();
        int totalReservations = Reservation.allReservations.size();
        int activeReservations = 0;

        for (Reservation r : Reservation.allReservations) {
            if (r.getStatus().equals("Confirmed") || r.getStatus().equals("Pending")) {
                activeReservations++;
            }
        }

        StringBuilder sb = new StringBuilder();
        sb.append("System Statistics\n");
        sb.append("==========================================\n\n");
        sb.append("Total Users: ").append(totalUsers).append(" users\n");
        sb.append("Total Stations: ").append(totalStations).append(" stations\n");
        sb.append("Total Reservations: ").append(totalReservations).append(" bookings\n");
        sb.append("Active Reservations: ").append(activeReservations).append(" bookings\n");

        infoArea.setText(sb.toString());
    }

    private void showAllUsers() {
        StringBuilder sb = new StringBuilder();
        sb.append("All Users List\n");
        sb.append("==========================================\n\n");

        int count = 1;
        for (UserInfo user : UserInfo.users) {
            sb.append(count++).append(". ").append(user.getName()).append("\n");
            sb.append("   Username: ").append(user.getUsername()).append("\n");
            sb.append("   Email: ").append(user.getEmail()).append("\n");
            sb.append("   Phone: ").append(user.getPhone()).append("\n");
            sb.append("   Role: ").append(user.getRole()).append("\n\n");

        }

        JTextArea textArea = new JTextArea(sb.toString());
        textArea.setFont(new Font("Consolas", Font.PLAIN, 14));
        textArea.setEditable(false);

        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(600, 400));

        JOptionPane.showMessageDialog(this, scrollPane, "User Management", JOptionPane.INFORMATION_MESSAGE);
    }

    private void showAllStations() {
        StringBuilder sb = new StringBuilder();
        sb.append("All Charging Stations\n");
        sb.append("==========================================\n\n");

        int count = 1;
        for (ChargingStationInfo station : StationDatabase.stations) {
            sb.append(count++).append(". ").append(station.getName()).append("\n");
            sb.append("   Charger Types: ").append(station.getHeadTypes()[0])
                    .append(", ").append(station.getHeadTypes()[1]).append("\n");
            sb.append("   Status: ").append(station.getStatus()).append("\n\n");
        }

        JTextArea textArea = new JTextArea(sb.toString());
        textArea.setFont(new Font("Consolas", Font.PLAIN, 14));
        textArea.setEditable(false);

        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(600, 400));

        JOptionPane.showMessageDialog(this, scrollPane, "Station Management", JOptionPane.INFORMATION_MESSAGE);
    }

    private void showAllReservations() {
        StringBuilder sb = new StringBuilder();
        sb.append("All Reservations\n");
        sb.append("==========================================\n\n");

        if (Reservation.allReservations.isEmpty()) {
            sb.append("No reservations yet");
        } else {
            int count = 1;
            for (Reservation r : Reservation.allReservations) {
                sb.append(count++).append(". ID: ").append(r.getReservationID()).append("\n");
                sb.append("   User: ").append(r.getUserID()).append("\n");
                sb.append("   Station: ").append(r.getStationID()).append("\n");
                sb.append("   Date: ").append(r.getStartTime().toLocalDate()).append("\n");
                sb.append("   Time: ").append(r.getStartTime().toLocalTime())
                        .append(" - ").append(r.getEndTime().toLocalTime()).append("\n");
                sb.append("   Status: ").append(r.getStatus()).append("\n\n");
            }
        }

        JTextArea textArea = new JTextArea(sb.toString());
        textArea.setFont(new Font("Consolas", Font.PLAIN, 14));
        textArea.setEditable(false);

        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(600, 400));

        JOptionPane.showMessageDialog(this, scrollPane, "View All Reservations", JOptionPane.INFORMATION_MESSAGE);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new MenuAdmin().setVisible(true);
        });
    }
}
