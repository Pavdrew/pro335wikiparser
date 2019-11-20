package group5.controller;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Driver {

    Connection connection;

    public void run() {
        connectToDb();
    }

    private void connectToDb() {
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            connection = DriverManager.getConnection("jdbc:sqlserver://localhost;databaseName=sport_teams;user=lab2;password=lab2");
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
