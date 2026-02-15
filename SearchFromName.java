
import javax.swing.*;
import java.awt.*;
import java.util.List;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.ListSelectionEvent;

/**
 * SearchFromName - Modern search page using ModernTheme
 */
public class SearchFromName extends JFrame {

    private JTextField searchField;
    private JList<String> stationList;
    private DefaultListModel<String> listModel;
    private java.util.ArrayList<String> stationNames;
    private String selectedStation = "";

    public SearchFromName() {
        initComponents();
        displayStations(null);
    }

    private void initComponents() {
        setTitle("Search Stations - " + AppConstants.APP_NAME);
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
        JLabel titleLabel = new JLabel("Search Charging Stations");
        titleLabel.setFont(ModernTheme.FONT_TITLE);
        titleLabel.setForeground(ModernTheme.COLOR_NEON_CYAN);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        contentPanel.add(titleLabel);
        contentPanel.add(Box.createVerticalStrut(20));

        // Search Bar
        JPanel searchPanel = ModernTheme.createGlassPanel();
        searchPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 15, 15));
        searchPanel.setMaximumSize(new Dimension(700, 80));

        JLabel searchLabel = new JLabel("Search:");
        searchLabel.setFont(ModernTheme.FONT_BODY);
        searchLabel.setForeground(Color.WHITE);

        searchField = ModernTheme.createStyledTextField();
        searchField.setPreferredSize(new Dimension(300, 40));

        JButton searchButton = ModernTheme.createNeonButton("Search", ModernTheme.COLOR_NEON_CYAN);
        searchButton.setPreferredSize(new Dimension(100, 40));

        JButton clearButton = ModernTheme.createNeonButton("Clear", Color.GRAY);
        clearButton.setPreferredSize(new Dimension(100, 40));

        searchButton.addActionListener(e -> searchStations());
        searchField.addActionListener(e -> searchStations());
        clearButton.addActionListener(e -> {
            searchField.setText("");
            displayStations(null);
        });

        searchPanel.add(searchLabel);
        searchPanel.add(searchField);
        searchPanel.add(searchButton);
        searchPanel.add(clearButton);

        contentPanel.add(searchPanel);
        contentPanel.add(Box.createVerticalStrut(20));

        // Results List
        JPanel listPanel = ModernTheme.createGlassPanel();
        listPanel.setLayout(new BorderLayout());
        listPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel resultsLabel = new JLabel("Available Stations (Click to select):");
        resultsLabel.setFont(ModernTheme.FONT_HEADER);
        resultsLabel.setForeground(Color.WHITE);
        resultsLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        listPanel.add(resultsLabel, BorderLayout.NORTH);

        listModel = new DefaultListModel<>();
        stationList = new JList<>(listModel);
        stationList.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        stationList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        stationList.setFixedCellHeight(60);
        stationList.setBackground(new Color(0, 0, 0, 0));
        stationList.setOpaque(false);
        stationList.setForeground(Color.WHITE);
        stationList.setSelectionBackground(new Color(0, 198, 255, 50));
        stationList.setSelectionForeground(Color.WHITE);

        // Custom Cell Renderer for HTML content
        stationList.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected,
                    boolean cellHasFocus) {
                JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected,
                        cellHasFocus);
                label.setOpaque(isSelected);
                return label;
            }
        });

        stationList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int index = stationList.getSelectedIndex();
                if (index >= 0 && index < stationNames.size()) {
                    selectedStation = stationNames.get(index);
                }
            }
        });

        JScrollPane scrollPane = new JScrollPane(stationList);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(255, 255, 255, 50)));
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        listPanel.add(scrollPane, BorderLayout.CENTER);
        contentPanel.add(listPanel);
        contentPanel.add(Box.createVerticalStrut(20));

        // Bottom Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        buttonPanel.setOpaque(false);

        JButton backButton = ModernTheme.createNeonButton("Back", Color.GRAY);
        JButton reserveButton = ModernTheme.createNeonButton("Make Reservation", ModernTheme.COLOR_NEON_GREEN);
        reserveButton.setPreferredSize(new Dimension(200, 45));

        backButton.addActionListener(e -> {
            new MenuUser().setVisible(true);
            this.dispose();
        });

        reserveButton.addActionListener(e -> makeReservation());

        buttonPanel.add(backButton);
        buttonPanel.add(reserveButton);

        contentPanel.add(buttonPanel);
        contentPanel.add(Box.createVerticalGlue());

        mainPanel.add(contentPanel, BorderLayout.CENTER);

        setContentPane(mainPanel);
    }

    private void displayStations(String keyword) {
        listModel.clear();
        stationNames = new java.util.ArrayList<>();

        List<ChargingStationInfo> allStations = StationDatabase.getAllStations();
        boolean found = false;

        for (ChargingStationInfo station : allStations) {
            boolean matches = keyword == null || keyword.isEmpty() ||
                    station.getName().toLowerCase().contains(keyword.toLowerCase());

            if (matches) {
                stationNames.add(station.getName());
                String display = String.format(
                        "<html><div style='padding: 5px;'><b>%s</b><br/>Types: %s, %s | Status: %s</div></html>",
                        station.getName(),
                        station.getHeadTypes()[0],
                        station.getHeadTypes()[1],
                        station.getStatus());
                listModel.addElement(display);
                found = true;
            }
        }

        if (!found) {
            listModel.addElement("<html><i>No stations found</i></html>");
        }
    }

    private void searchStations() {
        String searchText = searchField.getText().trim();
        displayStations(searchText.isEmpty() ? null : searchText);
    }

    private void makeReservation() {
        if (selectedStation.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Please select a station from the list",
                    "No Station Selected",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        ChargingStationInfo station = StationDatabase.getStationByName(selectedStation);

        if (station != null) {
            if (UserInfo.currentUser == null) {
                JOptionPane.showMessageDialog(this,
                        "You must be logged in to make a reservation",
                        "Login Required",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            new ReservationPage(selectedStation).setVisible(true);
            this.dispose();
        } else {
            JOptionPane.showMessageDialog(this,
                    "Station not found",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new SearchFromName().setVisible(true);
        });
    }
}
