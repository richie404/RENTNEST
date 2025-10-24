import java.io.InputStream;
import java.util.Properties;

/**
 * Loads database configuration from db.properties file inside resources/
 */
public class DBConfig {
    private static final Properties props = new Properties();

    static {
        try (InputStream is = DBConfig.class.getClassLoader().getResourceAsStream("db.properties")) {
            if (is == null) {
                throw new IllegalStateException("❌ db.properties not found in resources/");
            }
            props.load(is);
        } catch (Exception e) {
            throw new RuntimeException("❌ Failed to load DB configuration", e);
        }
    }

    public static String getUrl() {
        return props.getProperty("db.url");
    }

    public static String getUser() {
        return props.getProperty("db.user");
    }

    public static String getPassword() {
        return props.getProperty("db.password");
    }
}
