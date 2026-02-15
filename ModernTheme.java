import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * ModernTheme - Centralized styling for the futuristic "Deep Space" theme.
 */
public class ModernTheme {

    // Colors
    public static final Color COLOR_BG_1 = new Color(15, 12, 41);
    public static final Color COLOR_BG_2 = new Color(48, 43, 99);
    public static final Color COLOR_BG_3 = new Color(36, 36, 62);

    public static final Color COLOR_NEON_CYAN = new Color(0, 198, 255);
    public static final Color COLOR_NEON_GREEN = new Color(56, 239, 125);
    public static final Color COLOR_NEON_RED = new Color(255, 60, 60);
    public static final Color COLOR_TEXT_PRIMARY = Color.WHITE;
    public static final Color COLOR_TEXT_SECONDARY = new Color(200, 200, 200);

    // Fonts
    public static final Font FONT_TITLE = new Font("Segoe UI", Font.BOLD, 36);
    public static final Font FONT_HEADER = new Font("Segoe UI", Font.BOLD, 24);
    public static final Font FONT_BODY = new Font("Segoe UI", Font.PLAIN, 14);
    public static final Font FONT_BUTTON = new Font("Segoe UI", Font.BOLD, 16);

    public static void applyCommonFrameStyle(JFrame frame) {
        frame.setUndecorated(true);
        frame.setBackground(new Color(0, 0, 0, 0));

        MouseAdapter ma = new MouseAdapter() {
            int lastX, lastY;

            @Override
            public void mousePressed(MouseEvent e) {
                lastX = e.getXOnScreen();
                lastY = e.getYOnScreen();
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                int x = e.getXOnScreen();
                int y = e.getYOnScreen();
                frame.setLocation(frame.getLocation().x + (x - lastX), frame.getLocation().y + (y - lastY));
                lastX = x;
                lastY = y;
            }
        };
        frame.addMouseListener(ma);
        frame.addMouseMotionListener(ma);
    }

    public static JPanel createMainPanel() {
        return new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                int w = getWidth();
                int h = getHeight();

                GradientPaint gp = new GradientPaint(0, 0, COLOR_BG_1, w, h, COLOR_BG_3);
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, w, h);

                RadialGradientPaint glow = new RadialGradientPaint(
                        new Point(w / 2, h / 2), Math.max(w, h) / 2,
                        new float[] { 0.0f, 1.0f },
                        new Color[] { new Color(255, 255, 255, 15), new Color(0, 0, 0, 0) });
                g2d.setPaint(glow);
                g2d.fillRect(0, 0, w, h);
            }
        };
    }

    public static JPanel createGlassPanel() {
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                g2d.setColor(new Color(255, 255, 255, 20));
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);

                g2d.setColor(new Color(255, 255, 255, 50));
                g2d.setStroke(new BasicStroke(1));
                g2d.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 30, 30);
            }
        };
        panel.setOpaque(false);
        return panel;
    }

    public static JButton createNeonButton(String text, Color themeColor) {
        return new NeonButton(text, themeColor);
    }

    public static JButton createControlButton(String text, Color hoverColor, Runnable action) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Arial", Font.BOLD, 14));
        btn.setForeground(Color.WHITE);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setContentAreaFilled(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.addActionListener(e -> action.run());
        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                btn.setForeground(hoverColor);
            }

            public void mouseExited(MouseEvent e) {
                btn.setForeground(Color.WHITE);
            }
        });
        return btn;
    }

    public static JTextField createStyledTextField() {
        JTextField field = new JTextField();
        field.setPreferredSize(new Dimension(300, 40));
        field.setFont(FONT_BODY);
        field.setForeground(Color.WHITE);
        field.setCaretColor(Color.WHITE);
        field.setBackground(new Color(60, 60, 60)); // Dark gray background
        field.setOpaque(true);
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 2, 0, new Color(255, 255, 255, 100)),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));

        field.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                field.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createMatteBorder(0, 0, 2, 0, COLOR_NEON_GREEN),
                        BorderFactory.createEmptyBorder(5, 5, 5, 5)));
            }

            @Override
            public void focusLost(FocusEvent e) {
                field.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createMatteBorder(0, 0, 2, 0, new Color(255, 255, 255, 100)),
                        BorderFactory.createEmptyBorder(5, 5, 5, 5)));
            }
        });

        return field;
    }

    public static JComboBox<String> createStyledComboBox(String[] items) {
        JComboBox<String> comboBox = new JComboBox<>(items);
        comboBox.setPreferredSize(new Dimension(300, 40));
        comboBox.setFont(FONT_BODY);
        comboBox.setForeground(Color.WHITE);
        comboBox.setBackground(new Color(60, 60, 60));

        // Custom renderer for dropdown items
        comboBox.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                    boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (isSelected) {
                    setBackground(COLOR_NEON_GREEN);
                    setForeground(Color.BLACK);
                } else {
                    setBackground(new Color(60, 60, 60));
                    setForeground(Color.WHITE);
                }
                setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
                return this;
            }
        });

        return comboBox;
    }

    public static JPasswordField createStyledPasswordField() {
        JPasswordField field = new JPasswordField();
        field.setPreferredSize(new Dimension(300, 40));
        field.setFont(FONT_BODY);
        field.setForeground(Color.WHITE);
        field.setCaretColor(Color.WHITE);
        field.setBackground(new Color(60, 60, 60)); // Dark gray background
        field.setOpaque(true);
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 2, 0, new Color(255, 255, 255, 100)),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));

        field.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                field.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createMatteBorder(0, 0, 2, 0, COLOR_NEON_GREEN),
                        BorderFactory.createEmptyBorder(5, 5, 5, 5)));
            }

            @Override
            public void focusLost(FocusEvent e) {
                field.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createMatteBorder(0, 0, 2, 0, new Color(255, 255, 255, 100)),
                        BorderFactory.createEmptyBorder(5, 5, 5, 5)));
            }
        });

        return field;
    }

    private static class NeonButton extends JButton {
        private final Color themeColor;
        private boolean isHovered = false;

        public NeonButton(String text, Color themeColor) {
            super(text);
            this.themeColor = themeColor;

            setPreferredSize(new Dimension(180, 45));
            setFont(FONT_BUTTON);
            setBorderPainted(false);
            setContentAreaFilled(false);
            setFocusPainted(false);
            setCursor(new Cursor(Cursor.HAND_CURSOR));
            setForeground(Color.WHITE);

            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    isHovered = true;
                    repaint();
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    isHovered = false;
                    repaint();
                }
            });
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int w = getWidth();
            int h = getHeight();

            Color c1 = themeColor;
            Color c2 = themeColor.darker();

            if (isHovered) {
                c1 = c1.brighter();
                g2.setColor(new Color(themeColor.getRed(), themeColor.getGreen(), themeColor.getBlue(), 100));
                g2.fillRoundRect(0, 0, w, h, 30, 30);
            }

            GradientPaint gp = new GradientPaint(0, 0, c1, w, h, c2);
            g2.setPaint(gp);
            g2.fillRoundRect(2, 2, w - 4, h - 4, 30, 30);

            g2.setColor(Color.WHITE);
            g2.setFont(getFont());
            FontMetrics fm = g2.getFontMetrics();
            int x = (w - fm.stringWidth(getText())) / 2;
            int y = (h + fm.getAscent()) / 2 - 2;
            g2.drawString(getText(), x, y);

            g2.dispose();
        }
    }
}
