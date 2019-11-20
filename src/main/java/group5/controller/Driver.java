package group5.controller;

import group5.model.Team;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class Driver {

    Connection connection;
    String teamRegex = "'''\\[\\[([A-Za-z.\\s]*)\\]\\]'''";
    String singleCityRegex = "'''\\s\\|\\[\\[([A-Za-z,\\s]*)\\]\\],\\s\\[\\[([A-z]*)";
    String cityRegex = "\\[\\[([A-Za-z\\s]*, [A-z]*)";

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

    private void bulkInsertTeams(List<Team> teams) {
        String sql = "INSERT INTO sport_teams (team_name, team_location, sport_type) " + "VALUES (?,?,?)";
        int teamCounter = 0;

        try (PreparedStatement statement = connection.prepareStatement(sql)) {

            for (Team team : teams) {
                statement.setString(1, team.getTeamName());
                statement.setString(2, team.getTeamLocation());
                statement.setString(3, team.getSportType());

                statement.addBatch();
                teamCounter++;

                if(teamCounter > 65000) {
                    statement.executeBatch();
                    teamCounter = 0;
                }
            }
            statement.executeBatch();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
