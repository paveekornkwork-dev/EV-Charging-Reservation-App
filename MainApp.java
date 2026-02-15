
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

/**
 * MainApp - จุดเริ่มต้นของแอปพลิเคชัน CarCharge
 * รับประกันว่าโปรแกรมจะเริ่มที่หน้า Intro เสมอ
 */
public class MainApp {

    public static void main(String[] args) {
        // Set Look and Feel
        setLookAndFeel();

        // Start application on Event Dispatch Thread
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                // Initialize sample data
                initializeSampleData();

                // Show intro page
                Intro intro = new Intro();
                intro.setVisible(true);
                intro.setLocationRelativeTo(null); // Center on screen
            }
        });
    }

    /**
     * ตั้งค่า Look and Feel ให้สวยงาม
     */
    private static void setLookAndFeel() {
        try {
            // Try to use Nimbus Look and Feel
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    return;
                }
            }
            // If Nimbus not available, use system default
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            // Use default if error
            System.err.println("Could not set Look and Feel: " + e.getMessage());
        }
    }

    /**
     * สร้างข้อมูลตัวอย่างสำหรับทดสอบ
     */
    private static void initializeSampleData() {
        System.out.println("=== CarCharge Application Starting ===");
        System.out.println("Version: " + AppConstants.APP_VERSION);
        System.out.println("Sample users loaded: " + UserInfo.users.size());
        System.out.println("Sample stations loaded: " + StationDatabase.stations.size());
        System.out.println("======================================");
    }
}
