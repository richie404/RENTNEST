import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public final class DatabaseConnection {
    private static final String URL;
    private static final String USER;
    private static final String PASS;

    static {
        // 1) Ensure MySQL driver is available
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new IllegalStateException("MySQL driver not found on classpath", e);
        }

        // 2) Load db.properties (works both in IDE and when packaged)
        Properties props = new Properties();
        try (InputStream isA = DatabaseConnection.class.getResourceAsStream("/db.properties")) {
            InputStream is = isA;
            if (is == null) {
                is = Thread.currentThread().getContextClassLoader().getResourceAsStream("db.properties");
            }
            if (is == null) throw new IllegalStateException("db.properties not found on classpath");
            props.load(is);
        } catch (Exception e) {
            throw new RuntimeException("Failed to load DB properties", e);
        }

        URL  = require(props, "db.url");
        USER = props.getProperty("db.user", "");
        PASS = props.getProperty("db.password", "");
    }

    private DatabaseConnection() {}

    /** Canonical method name */
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASS);
    }

    /** Alias so existing DAOs using .get() compile */
    public static Connection get() throws SQLException {
        return getConnection();
    }

    private static String require(Properties p, String key) {
        String v = p.getProperty(key);
        if (v == null || v.isBlank()) {
            throw new IllegalStateException("Missing required property: " + key);
        }
        return v;
    }
}
