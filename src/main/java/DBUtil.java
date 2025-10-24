import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * DBUtil — Centralized database connection utility for RentNest.
 * Automatically loads MySQL driver and provides pooled connection access.
 */
public class DBUtil {

    // ✅ Connection details
    private static final String URL = "jdbc:mysql://localhost:3306/rentnest?useSSL=false&serverTimezone=UTC";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    static {
        try {
            // ✅ Ensure the MySQL driver is registered
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.err.println("❌ MySQL Driver not found. Add mysql-connector-j.jar to your classpath.");
            e.printStackTrace();
        }
    }

    /**
     * ✅ Get a live DB connection
     */
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    /**
     * ✅ Safely close connection (optional helper)
     */
    public static void close(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
