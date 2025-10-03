package com.rentnest.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    static {
        try { Class.forName("com.mysql.cj.jdbc.Driver"); }
        catch (ClassNotFoundException e) { throw new IllegalStateException(e); }
    }


    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DBConfig.getUrl(), DBConfig.getUser(), DBConfig.getPassword());
    }
}
