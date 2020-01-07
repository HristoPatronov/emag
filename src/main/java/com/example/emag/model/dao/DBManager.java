package com.example.emag.model.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBManager {
    private static DBManager ourInstance = new DBManager();

    //credentials to DB
    private static final String DB_HOSTNAME = "localhost";
    private static final  String DB_PORT = "3306";
    private static final String DB_NAME = "emag";
    private static final String DB_USERNAME = "root";
    private static final String DB_PASSWORD = "root";

    private Connection connection = null;

    public static DBManager getInstance() {
        return ourInstance;
    }

    //loading driver
    private DBManager() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("Sorry, driver not found! Please check your dependencies!");
        }
        this.connection = createConnection();
    }

    //establishing a connection
    private Connection createConnection() {
        try {
            return DriverManager.getConnection("jdbc:mysql://" +
                    DB_HOSTNAME + ":" + DB_PORT + "/" + DB_NAME, DB_USERNAME, DB_PASSWORD);
        } catch (SQLException e) {
            System.out.println("Error connecting to DB! Check your credentials!");
            return null;
        }
    }

    public Connection getConnection() throws SQLException {
        if (connection.isClosed()){
            connection = createConnection();
        }
        return connection;
    }

}
