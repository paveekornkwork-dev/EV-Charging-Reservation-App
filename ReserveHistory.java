
import javax.swing.*;
import java.awt.*;
import java.util.List;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;

/**
 * ReserveHistory - Modern reservation history using ModernTheme
 */
public class ReserveHistory extends JFrame {

    private JTable reservationTable;

    public ReserveHistory() {
        initComponents();
        loadReservationHistory();
    }

    private void initComponents() {
        setTitle("Reservation History - " + AppConstants.APP_NAME);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 700);
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
        JLabel titleLabel = new JLabel("Your Reservation History");
        titleLabel.setFont(ModernTheme.FONT_TITLE);
        titleLabel.setForeground(ModernTheme.COLOR_NEON_GREEN);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subtitleLabel = new JLabel("View all your past and current reservations");
        subtitleLabel.setFont(ModernTheme.FONT_BODY);
        subtitleLabel.setForeground(ModernTheme.COLOR_TEXT_SECONDARY);
        subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        contentPanel.add(titleLabel);
        contentPanel.add(Box.createVerticalStrut(10));
        contentPanel.add(subtitleLabel);
        contentPanel.add(Box.createVerticalStrut(20));

        // Table Card
        JPanel tableCard = ModernTheme.createGlassPanel();
        tableCard.setLayout(new BorderLayout());
        tableCard.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        String[] columnNames = { "Date", "Station", "Charger Type", "Time", "Price", "Status" };
        DefaultTableModel model = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        reservationTable = new JTable(model);
        reservationTable.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        reservationTable.setRowHeight(40);
        reservationTable.setBackground(new Color(0, 0, 0, 0));
        reservationTable.setOpaque(false);
        reservationTable.setForeground(Color.WHITE);
        reservationTable.setSelectionBackground(new Color(0, 198, 255, 50));
        reservationTable.setSelectionForeground(Color.WHITE);
        reservationTable.setGridColor(new Color(255, 255, 255, 30));

        // Header styling
        reservationTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        reservationTable.getTableHeader().setBackground(new Color(0, 0, 0, 100));
        reservationTable.getTableHeader().setForeground(ModernTheme.COLOR_NEON_CYAN);
        reservationTable.getTableHeader().setOpaque(false);

        // Center align cells
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        centerRenderer.setOpaque(false);
        centerRenderer.setForeground(Color.WHITE);
        for (int i = 0; i < reservationTable.getColumnCount(); i++) {
            reservationTable.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        JScrollPane scrollPane = new JScrollPane(reservationTable);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(255, 255, 255, 50)));
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        tableCard.add(scrollPane, BorderLayout.CENTER);

        contentPanel.add(tableCard);
        contentPanel.add(Box.createVerticalStrut(20));

        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        buttonPanel.setOpaque(false);

        JButton backButton = ModernTheme.createNeonButton("Back", Color.GRAY);
        JButton refreshButton = ModernTheme.createNeonButton("Refresh", ModernTheme.COLOR_NEON_CYAN);

        backButton.addActionListener(e -> {
            new MenuUser().setVisible(true);
            this.dispose();
        });

        refreshButton.addActionListener(e -> {
            loadReservationHistory();
            JOptionPane.showMessageDialog(this, "Data refreshed successfully");
        });

        buttonPanel.add(backButton);
        buttonPanel.add(refreshButton);

        contentPanel.add(buttonPanel);
        contentPanel.add(Box.createVerticalGlue());

        mainPanel.add(contentPanel, BorderLayout.CENTER);

        setContentPane(mainPanel);
    }

    private void loadReservationHistory() {
        DefaultTableModel model = (DefaultTableModel) reservationTable.getModel();
        model.setRowCount(0);

        if (UserInfo.currentUser == null) {
            model.addRow(new Object[] { "N/A", "Must be logged in", "N/A", "N/A", "N/A", "ERROR" });
            return;
        }

        String loggedInUserID = UserInfo.currentUser.getUsername();
        List<Reservation> historyList = ReservationManager.getHistory(loggedInUserID);

        if (historyList.isEmpty()) {
            model.addRow(new Object[] { "N/A", "No reservations yet", "N/A", "N/A", "N/A", "-" });
            return;
        }

        for (Reservation res : historyList) {
            Object[] row = new Object[] {
                    res.getStartTime().toLocalDate(),
                    res.getStationID(),
                    "Type 2",
                    res.getStartTime().toLocalTime() + " - " + res.getEndTime().toLocalTime(),
                    String.format("%.2f", res.getPrice()) + " baht",
                    res.getStatus()
            };
            model.addRow(row);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new ReserveHistory().setVisible(true);
        });
    }
}
