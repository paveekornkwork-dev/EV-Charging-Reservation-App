
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * Intro - Premium futuristic landing page using ModernTheme
 */
public class Intro extends JFrame {

    private float opacity = 0.0f;

    public Intro() {
        initComponents();
        fadeIn();
    }

    private void initComponents() {
        setTitle(AppConstants.APP_NAME);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 600);
        setResizable(false);

        // Apply frameless style
        ModernTheme.applyCommonFrameStyle(this);
        setLocationRelativeTo(null);

        // Main Background Panel
        JPanel mainPanel = ModernTheme.createMainPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));

        // Window Controls
        JPanel controlsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        controlsPanel.setOpaque(false);
        controlsPanel.setMaximumSize(new Dimension(900, 30));

        JButton closeBtn = ModernTheme.createControlButton("X", Color.RED, () -> System.exit(0));
        controlsPanel.add(closeBtn);

        mainPanel.add(controlsPanel);
        mainPanel.add(Box.createVerticalStrut(20));

        // Header Section
        JLabel titleLabel = new JLabel(AppConstants.APP_NAME);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 48));
        titleLabel.setForeground(ModernTheme.COLOR_NEON_CYAN);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel versionLabel = new JLabel("Version " + AppConstants.APP_VERSION);
        versionLabel.setFont(ModernTheme.FONT_BODY);
        versionLabel.setForeground(ModernTheme.COLOR_TEXT_SECONDARY);
        versionLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        mainPanel.add(titleLabel);
        mainPanel.add(Box.createVerticalStrut(10));
        mainPanel.add(versionLabel);
        mainPanel.add(Box.createVerticalStrut(50));

        // Glassmorphism Card
        JPanel cardPanel = ModernTheme.createGlassPanel();
        cardPanel.setLayout(new BoxLayout(cardPanel, BoxLayout.Y_AXIS));
        cardPanel.setBorder(BorderFactory.createEmptyBorder(40, 60, 40, 60));
        cardPanel.setMaximumSize(new Dimension(600, 200));

        JLabel subLabel = new JLabel("Electric Vehicle Charging Station");
        subLabel.setFont(ModernTheme.FONT_HEADER);
        subLabel.setForeground(ModernTheme.COLOR_TEXT_PRIMARY);
        subLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel descLabel = new JLabel("Modern Reservation System");
        descLabel.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        descLabel.setForeground(ModernTheme.COLOR_TEXT_SECONDARY);
        descLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        cardPanel.add(subLabel);
        cardPanel.add(Box.createVerticalStrut(15));
        cardPanel.add(descLabel);

        mainPanel.add(cardPanel);
        mainPanel.add(Box.createVerticalGlue());

        // Buttons Panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 40, 0));
        buttonPanel.setOpaque(false);

        JButton loginButton = ModernTheme.createNeonButton("Login", ModernTheme.COLOR_NEON_CYAN);
        JButton registerButton = ModernTheme.createNeonButton("Register", ModernTheme.COLOR_NEON_GREEN);

        loginButton.addActionListener(e -> {
            new Login().setVisible(true);
            this.dispose();
        });

        registerButton.addActionListener(e -> {
            new Register().setVisible(true);
            this.dispose();
        });

        buttonPanel.add(loginButton);
        buttonPanel.add(registerButton);

        mainPanel.add(buttonPanel);
        mainPanel.add(Box.createVerticalStrut(50));

        setContentPane(mainPanel);
    }

    private void fadeIn() {
        Timer timer = new Timer(20, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                opacity += 0.05f;
                if (opacity >= 1.0f) {
                    opacity = 1.0f;
                    ((Timer) e.getSource()).stop();
                }
                setOpacity(Math.min(opacity, 1.0f));
            }
        });
        timer.start();
    }
}
