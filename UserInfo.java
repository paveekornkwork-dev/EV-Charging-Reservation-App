
import java.util.ArrayList;

public class UserInfo {
    private String name, email, phone, address, brand, license, charger, username, password;
    private String role; // "USER" or "ADMIN"

    public UserInfo(String name, String email, String phone, String address,
            String brand, String license, String charger, String username, String password) {
        this(name, email, phone, address, brand, license, charger, username, password, AppConstants.ROLE_USER);
    }

    public UserInfo(String name, String email, String phone, String address,
            String brand, String license, String charger, String username, String password, String role) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.brand = brand;
        this.license = license;
        this.charger = charger;
        this.username = username;
        this.password = password;
        this.role = role;
    }

    // Getters
    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public String getAddress() {
        return address;
    }

    public String getBrand() {
        return brand;
    }

    public String getLicense() {
        return license;
    }

    public String getCharger() {
        return charger;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getRole() {
        return role;
    }

    // Check if user is admin
    public boolean isAdmin() {
        return AppConstants.ROLE_ADMIN.equals(role);
    }

    // Static list of all users
    public static ArrayList<UserInfo> users = new ArrayList<>();

    // Currently logged in user
    public static UserInfo currentUser = null;

    static {
        // Admin user for testing
        users.add(new UserInfo(
                "Administrator", "admin@evio.com", "0800000000", "EVIO HQ",
                "Tesla Model S", "ADMIN001", "Supercharger",
                "admin", "admin123", AppConstants.ROLE_ADMIN));

        // Regular users for testing
        users.add(new UserInfo(
                "Grace Hopper", "grace@example.com", "0812345678", "123 Tech Road",
                "BYD Atto 3", "BYD456", "Type 2",
                "grace", "123456", AppConstants.ROLE_USER));

        users.add(new UserInfo(
                "Alan Turing", "alan@example.com", "0823456789", "456 Innovation St",
                "Tesla Model 3", "TESLA789", "CCS Type 2",
                "alan", "123456", AppConstants.ROLE_USER));
    }

    /**
     * Login method with role-based routing
     * 
     * @return UserInfo object if successful, null if failed
     */
    public static UserInfo login(String username, String password) {
        for (UserInfo user : users) {
            if (user.getUsername().equals(username) && user.getPassword().equals(password)) {
                currentUser = user;
                System.out.println("Login successful: " + user.getName() + " (" + user.getRole() + ")");
                return user;
            }
        }
        return null;
    }

    /**
     * Logout current user
     */
    public static void logout() {
        if (currentUser != null) {
            System.out.println("Logout: " + currentUser.getName());
            currentUser = null;
        }
    }
}
