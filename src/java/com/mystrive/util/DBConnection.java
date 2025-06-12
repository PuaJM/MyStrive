package com.mystrive.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Utility class for managing database connections.
 * This version reads database credentials from environment variables,
 * suitable for deployment on platforms like Render.
 */
public class DBConnection {

    // Database connection parameters - now read from environment variables
    // Render automatically provides these for managed databases.
    // The names (MYSQLHOST, MYSQLPORT, etc.) are commonly used defaults.
    private static final String DB_HOST = System.getenv("MYSQLHOST");
    private static final String DB_PORT = System.getenv("MYSQLPORT");
    private static final String DB_NAME = System.getenv("MYSQLDATABASE");
    private static final String DB_USERNAME = System.getenv("MYSQLUSER");
    private static final String DB_PASSWORD = System.getenv("MYSQLPASSWORD");

    // Construct the JDBC URL dynamically using environment variables
    private static final String JDBC_URL;

    static {
        // This static block ensures JDBC_URL is constructed once when the class is loaded.
        // It also handles cases where environment variables might be null (e.g., local run without setting them).
        if (DB_HOST != null && DB_PORT != null && DB_NAME != null) {
            JDBC_URL = "jdbc:mysql://" + DB_HOST + ":" + DB_PORT + "/" + DB_NAME + "?useSSL=false&serverTimezone=UTC";
        } else {
            // Provide a default or throw an error if essential variables are missing at startup
            // For local development without Docker or env variables, you might fallback to localhost.
            // For production on Render, these variables MUST be set.
            JDBC_URL = "jdbc:mysql://localhost:3306/mystrive_db?useSSL=false&serverTimezone=UTC"; // Fallback for local testing
            Logger.getLogger(DBConnection.class.getName()).log(Level.WARNING, "Database environment variables not fully set. Using local fallback URL. Host: {0}, Port: {1}, DB Name: {2}", new Object[]{DB_HOST, DB_PORT, DB_NAME});
        }
    }

    // JDBC Driver Name
    private static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";

    private static final Logger LOGGER = Logger.getLogger(DBConnection.class.getName());

    /**
     * Establishes and returns a connection to the MySQL database.
     *
     * @return A valid database Connection object, or null if a connection cannot be established.
     */
    public static Connection getConnection() {
        Connection connection = null;
        try {
            // Re-check if environment variables are available *before* attempting connection
            // This catches cases where the static block might have used a fallback but now we need proper credentials.
            if (DB_USERNAME == null || DB_PASSWORD == null || DB_HOST == null || DB_PORT == null || DB_NAME == null) {
                 LOGGER.log(Level.SEVERE, "Missing one or more database environment variables (MYSQLHOST, MYSQLPORT, MYSQLDATABASE, MYSQLUSER, MYSQLPASSWORD). Cannot establish connection.");
                 return null;
            }

            // Register JDBC driver
            Class.forName(JDBC_DRIVER);

            // Open a connection
            LOGGER.log(Level.INFO, "Attempting to connect to database at {0} using user {1}...", new Object[]{JDBC_URL, DB_USERNAME});
            connection = DriverManager.getConnection(JDBC_URL, DB_USERNAME, DB_PASSWORD);
            LOGGER.log(Level.INFO, "Database connection established successfully.");
        } catch (ClassNotFoundException e) {
            LOGGER.log(Level.SEVERE, "MySQL JDBC Driver not found. Make sure the JAR is in WEB-INF/lib and the driver class name is correct.", e);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Failed to connect to database. Check credentials, host, port, and database name. URL: " + JDBC_URL + ", User: " + DB_USERNAME + ", Error: " + e.getMessage(), e);
        }
        return connection;
    }

    /**
     * Closes the provided database connection.
     *
     * @param connection The Connection object to close.
     */
    public static void closeConnection(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
                LOGGER.log(Level.INFO, "Database connection closed.");
            } catch (SQLException e) {
                LOGGER.log(Level.SEVERE, "Error closing database connection: " + e.getMessage(), e);
            }
        }
    }
}
