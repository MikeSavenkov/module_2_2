package com.msavenkov.crudgradleproject.utils;

import com.msavenkov.crudgradleproject.config.DatabaseConfig;

import java.sql.*;

public class DBUtils {

    private static final String DATABASE_URL = DatabaseConfig.getUrl();
    private static final String USER = DatabaseConfig.getUser();
    private static final String PASSWORD = DatabaseConfig.getPassword();

    private static Connection connection;

    private static Connection getConnection() {
        if (connection == null) {
            try {
                connection = DriverManager.getConnection(DATABASE_URL, USER, PASSWORD);
            } catch (SQLException e) {
                System.out.println("Database get connection error: " + e);
            }
        }
        return connection;
    }

    public static PreparedStatement getPreparedStatement(String sql) throws SQLException {
        return getConnection().prepareStatement(sql);
    }
}
