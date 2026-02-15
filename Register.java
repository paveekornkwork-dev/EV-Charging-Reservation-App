
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * Register - Modern registration page using ModernTheme
 */
public class Register extends JFrame {

        private JTextField nameField, emailField, phoneField, usernameField;
        private JPasswordField passwordField;
        private JCheckBox tosCheckBox;

        public Register() {
                initComponents();
        }

        private void initComponents() {
                setTitle("Register - " + AppConstants.APP_NAME);
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

                // Center Content (Scrollable)
                JPanel contentPanel = new JPanel();
                contentPanel.setOpaque(false);
                contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));

                // Header
                JLabel titleLabel = new JLabel("Create Account");
                titleLabel.setFont(ModernTheme.FONT_TITLE);
                titleLabel.setForeground(ModernTheme.COLOR_NEON_GREEN);
                titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

                JLabel subtitleLabel = new JLabel("Join the EV charging community");
                subtitleLabel.setFont(ModernTheme.FONT_BODY);
                subtitleLabel.setForeground(ModernTheme.COLOR_TEXT_SECONDARY);
                subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

                contentPanel.add(titleLabel);
                contentPanel.add(Box.createVerticalStrut(10));
                contentPanel.add(subtitleLabel);
                contentPanel.add(Box.createVerticalStrut(30));

                // Form Card
                JPanel cardPanel = ModernTheme.createGlassPanel();
                cardPanel.setLayout(new GridBagLayout());
                cardPanel.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));

                GridBagConstraints gbc = new GridBagConstraints();
                gbc.fill = GridBagConstraints.HORIZONTAL;
                gbc.gridx = 0;
                gbc.insets = new Insets(10, 0, 5, 0);

                // Name
                addFormField(cardPanel, gbc, "Full Name", nameField = ModernTheme.createStyledTextField());

                // Username
                addFormField(cardPanel, gbc, "Username", usernameField = ModernTheme.createStyledTextField());

                // Password
                addFormField(cardPanel, gbc, "Password", passwordField = ModernTheme.createStyledPasswordField());

                // Email
                addFormField(cardPanel, gbc, "Email Address", emailField = ModernTheme.createStyledTextField());

                // Phone
                addFormField(cardPanel, gbc, "Phone Number", phoneField = ModernTheme.createStyledTextField());

                // TOS Checkbox
                gbc.gridy++;
                gbc.insets = new Insets(20, 0, 10, 0);
                tosCheckBox = new JCheckBox("<html>I accept the Terms of Service</html>");
                tosCheckBox.setFont(ModernTheme.FONT_BODY);
                tosCheckBox.setForeground(Color.WHITE);
                tosCheckBox.setOpaque(false);
                tosCheckBox.setFocusPainted(false);
                cardPanel.add(tosCheckBox, gbc);

                contentPanel.add(cardPanel);
                contentPanel.add(Box.createVerticalStrut(30));

                // Buttons
                JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
                buttonPanel.setOpaque(false);

                JButton backButton = ModernTheme.createNeonButton("Back", new Color(100, 100, 100));
                JButton registerButton = ModernTheme.createNeonButton("Register", ModernTheme.COLOR_NEON_GREEN);

                backButton.addActionListener(e -> {
                        new Intro().setVisible(true);
                        this.dispose();
                });

                registerButton.addActionListener(e -> handleRegister());

                buttonPanel.add(backButton);
                buttonPanel.add(registerButton);

                contentPanel.add(buttonPanel);
                contentPanel.add(Box.createVerticalStrut(20));

                // Scroll Pane for small screens
                JScrollPane scrollPane = new JScrollPane(contentPanel);
                scrollPane.setOpaque(false);
                scrollPane.getViewport().setOpaque(false);
                scrollPane.setBorder(null);
                scrollPane.getVerticalScrollBar().setUnitIncrement(16);

                mainPanel.add(scrollPane, BorderLayout.CENTER);

                setContentPane(mainPanel);
        }

        private void addFormField(JPanel panel, GridBagConstraints gbc, String labelText, JComponent field) {
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

        private void handleRegister() {
                if (!tosCheckBox.isSelected()) {
                        JOptionPane.showMessageDialog(this,
                                        "Please accept the Terms of Service",
                                        "Terms Required",
                                        JOptionPane.WARNING_MESSAGE);
                        return;
                }

                String name = nameField.getText().trim();
                String username = usernameField.getText().trim();
                String password = new String(passwordField.getPassword()).trim();
                String email = emailField.getText().trim();
                String phone = phoneField.getText().trim();

                if (name.isEmpty() || username.isEmpty() || password.isEmpty() ||
                                email.isEmpty() || phone.isEmpty()) {
                        JOptionPane.showMessageDialog(this,
                                        "Please fill in all required fields",
                                        "Incomplete Data",
                                        JOptionPane.WARNING_MESSAGE);
                        return;
                }

                // Check username uniqueness
                for (UserInfo user : UserInfo.users) {
                        if (user.getUsername().equals(username)) {
                                JOptionPane.showMessageDialog(this,
                                                "This username already exists\nPlease choose another username",
                                                "Username Taken",
                                                JOptionPane.ERROR_MESSAGE);
                                return;
                        }
                }

                UserInfo newUser = new UserInfo(
                                name, email, phone, "-",
                                "-", "-", "Type 2",
                                username, password);

                UserInfo.users.add(newUser);

                JOptionPane.showMessageDialog(this,
                                "Registration successful!\nWelcome " + name,
                                "Success",
                                JOptionPane.INFORMATION_MESSAGE);

                new Login().setVisible(true);
                this.dispose();
        }

        public static void main(String[] args) {
                SwingUtilities.invokeLater(() -> {
                        new Register().setVisible(true);
                });
        }
}
