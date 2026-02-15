
import java.awt.Color;
import java.awt.Font;

/**
 * AppConstants - เก็บค่าคงที่ทั้งหมดของแอปพลิเคชัน
 * เพื่อให้ UI สม่ำเสมอทุกหน้า
 */
public class AppConstants {

    // ========== COLORS ==========
    public static final Color PRIMARY_COLOR = new Color(33, 150, 243); // Blue
    public static final Color SUCCESS_COLOR = new Color(76, 175, 80); // Green
    public static final Color WARNING_COLOR = new Color(255, 152, 0); // Orange
    public static final Color DANGER_COLOR = new Color(244, 67, 54); // Red
    public static final Color BACKGROUND_COLOR = new Color(255, 255, 255); // White
    public static final Color TEXT_COLOR = new Color(51, 51, 51); // Dark Gray
    public static final Color LIGHT_GRAY = new Color(240, 240, 240);

    // ========== FONTS ==========
    public static final Font HEADER_FONT = new Font("Segoe UI", Font.BOLD, 24);
    public static final Font SUBHEADER_FONT = new Font("Segoe UI", Font.BOLD, 18);
    public static final Font BODY_FONT = new Font("Segoe UI", Font.PLAIN, 14);
    public static final Font LABEL_FONT = new Font("Segoe UI", Font.PLAIN, 12);
    public static final Font TITLE_FONT = new Font("Segoe UI", Font.BOLD, 32);

    // ========== SIZES ==========
    public static final int TEXTFIELD_WIDTH = 350;
    public static final int TEXTFIELD_HEIGHT = 30;
    public static final int BUTTON_HEIGHT = 40;
    public static final int BUTTON_WIDTH = 200;
    public static final int STANDARD_PADDING = 20;
    public static final int COMPONENT_SPACING = 15;

    // ========== APP INFO ==========
    public static final String APP_NAME = "EVIO Charging Station";
    public static final String APP_VERSION = "2.0";

    // ========== USER ROLES ==========
    public static final String ROLE_USER = "USER";
    public static final String ROLE_ADMIN = "ADMIN";
}
