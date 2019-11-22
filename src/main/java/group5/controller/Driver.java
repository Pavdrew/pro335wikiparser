package group5.controller;

import group5.model.Team;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Driver {

    Connection connection;
    String regex = "\\| ?'''\\[\\[([a-zA-Z\\s\\.]+)\\]\\]'''\\n\\| ?\\[\\[(([a-zA-Z\\s\\.])*\\|?([a-zA-Z\\s\\.,]+))\\]\\]";

    public void run() {

        File folder = new File("xml files");
        File[] listOfFiles = folder.listFiles();

        List<Team> teams = new ArrayList<>();

        connectToDb();

        for (File file : listOfFiles) {
            if (file.isFile()) {
                teams = getTeamInfo(xmlTeamParser(file), file.getName());
                bulkInsertTeams(teams);
            }
        }


    }

    private String xmlTeamParser(File path) {

        List<Team> teams = new ArrayList<>();

        Team team = new Team();
        System.out.println("Parsing Teams");
        String text = "";
        XMLInputFactory xmlInputFactory = XMLInputFactory.newInstance();
        try {
            XMLEventReader xmlEventReader = xmlInputFactory.createXMLEventReader(new FileInputStream(path));
            while (xmlEventReader.hasNext()) {
                XMLEvent xmlEvent = xmlEventReader.nextEvent();
                if (xmlEvent.isStartElement()) {
                    StartElement startElement = xmlEvent.asStartElement();
                    if (startElement.getName().getLocalPart().equals("text")) {
                        for (int i = 0; i < 1300; i++) {
                            xmlEvent = xmlEventReader.nextEvent();
                            text += xmlEvent.asCharacters().getData();
                        }
                    }

                }

                if (xmlEvent.isEndElement()) {
                    EndElement endElement = xmlEvent.asEndElement();
                    if (endElement.getName().getLocalPart().equals("text")) {
                        teams.add(team);
                    }
                }
            }

        } catch (FileNotFoundException | XMLStreamException e) {
            e.printStackTrace();
        }


        return text;
    }

    private List<Team> getTeamInfo(String data, String sportType) {
        List<Team> teams = new ArrayList<>();

        Pattern p = Pattern.compile(regex);
        Matcher m =p.matcher(data);
        String teamName, teamLocation;
        while(m.find()) {
            teamName = m.group(1);
            if(m.group(2).contains("|")) {
                teamLocation = m.group(4);
            } else {
                teamLocation = m.group(2);
            }

            teams.add(new Team(teamName, teamLocation, sportType));
        }

        return teams;
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

                if (teamCounter > 65000) {
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
