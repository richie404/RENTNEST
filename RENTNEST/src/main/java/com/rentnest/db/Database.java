package com.rentnest.db;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class Database {
    private static final Properties props = new Properties();

    static {
        // 1) Load MySQL driver (this fixes the module error without requires com.mysql.cj)
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new IllegalStateException("MySQL driver not found on classpath", e);
        }

        // 2) Load db.properties
        try (InputStream is = Database.class.getClassLoader().getResourceAsStream("db.properties")) {
            if (is == null) throw new IllegalStateException("db.properties not found");
            props.load(is);
        } catch (Exception e) {
            throw new RuntimeException("Failed to load DB properties", e);
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(
                props.getProperty("db.url"),
                props.getProperty("db.user"),
                props.getProperty("db.password")
        );
    }
}
