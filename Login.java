
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * Login - Premium login page using ModernTheme
 */
public class Login extends JFrame {

    private JTextField usernameField;
    private JPasswordField passwordField;
    private float opacity = 0.0f;

    public Login() {
        initComponents();
        fadeIn();
    }

    private void initComponents() {
        setTitle("Login - " + AppConstants.APP_NAME);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 550);
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

        // Center Content
        JPanel centerPanel = new JPanel();
        centerPanel.setOpaque(false);
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));

        // Header
        JLabel titleLabel = new JLabel("Login");
        titleLabel.setFont(ModernTheme.FONT_TITLE);
        titleLabel.setForeground(ModernTheme.COLOR_NEON_CYAN);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subtitleLabel = new JLabel("Access your account");
        subtitleLabel.setFont(ModernTheme.FONT_BODY);
        subtitleLabel.setForeground(ModernTheme.COLOR_TEXT_SECONDARY);
        subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        centerPanel.add(titleLabel);
        centerPanel.add(Box.createVerticalStrut(10));
        centerPanel.add(subtitleLabel);
        centerPanel.add(Box.createVerticalStrut(30));

        // Form Card
        JPanel cardPanel = ModernTheme.createGlassPanel();
        cardPanel.setLayout(new GridBagLayout());
        cardPanel.setMaximumSize(new Dimension(450, 280));
        cardPanel.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.insets = new Insets(10, 0, 5, 0);

        // Username
        JLabel userLabel = new JLabel("Username");
        userLabel.setFont(ModernTheme.FONT_BODY);
        userLabel.setForeground(Color.WHITE);
        cardPanel.add(userLabel, gbc);

        gbc.insets = new Insets(0, 0, 15, 0);
        usernameField = ModernTheme.createStyledTextField();
        cardPanel.add(usernameField, gbc);

        // Password
        gbc.insets = new Insets(10, 0, 5, 0);
        JLabel passLabel = new JLabel("Password");
        passLabel.setFont(ModernTheme.FONT_BODY);
        passLabel.setForeground(Color.WHITE);
        cardPanel.add(passLabel, gbc);

        gbc.insets = new Insets(0, 0, 15, 0);
        passwordField = ModernTheme.createStyledPasswordField();
        cardPanel.add(passwordField, gbc);

        // Hint
        JLabel hintLabel = new JLabel("<html><i>Test: admin/admin123</i></html>");
        hintLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        hintLabel.setForeground(Color.GRAY);
        cardPanel.add(hintLabel, gbc);

        centerPanel.add(cardPanel);
        centerPanel.add(Box.createVerticalStrut(30));

        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        buttonPanel.setOpaque(false);

        JButton backButton = ModernTheme.createNeonButton("Back", new Color(100, 100, 100));
        JButton loginButton = ModernTheme.createNeonButton("Login", ModernTheme.COLOR_NEON_CYAN);

        backButton.addActionListener(e -> {
            new Intro().setVisible(true);
            this.dispose();
        });

        loginButton.addActionListener(e -> handleLogin());
        passwordField.addActionListener(e -> handleLogin());

        buttonPanel.add(backButton);
        buttonPanel.add(loginButton);

        centerPanel.add(buttonPanel);
        centerPanel.add(Box.createVerticalGlue());

        mainPanel.add(centerPanel, BorderLayout.CENTER);

        setContentPane(mainPanel);
    }

    private void handleLogin() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword()).trim();

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Please enter both username and password",
                    "Incomplete Information",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        UserInfo user = UserInfo.login(username, password);

        if (user != null) {
            JOptionPane.showMessageDialog(this,
                    "Login successful!\nWelcome " + user.getName(),
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE);

            if (user.isAdmin()) {
                new MenuAdmin().setVisible(true);
            } else {
                new MenuUser().setVisible(true);
            }
            this.dispose();
        } else {
            JOptionPane.showMessageDialog(this,
                    "Invalid username or password\nPlease try again",
                    "Login Failed",
                    JOptionPane.ERROR_MESSAGE);
            passwordField.setText("");
            usernameField.requestFocus();
        }
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

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new Login().setVisible(true);
        });
    }
}
