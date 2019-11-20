package group5.controller;

import group5.model.Team;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Driver {

    Connection connection;

    public void run() {

       String path = "baseball.xml";

        List<Team> teamList = xmlTeamParser(path);

        connectToDb();
    }

    private List<Team> xmlTeamParser(String path) {

        List<Team> teams = new ArrayList<>();

        Team team = new Team();
        System.out.println("Parsing Teams");

        XMLInputFactory xmlInputFactory = XMLInputFactory.newInstance();
        try{
            XMLEventReader xmlEventReader = xmlInputFactory.createXMLEventReader(new FileInputStream(path));
            while (xmlEventReader.hasNext()){
                XMLEvent xmlEvent = xmlEventReader.nextEvent();
                if (xmlEvent.isStartElement()){
                    StartElement startElement = xmlEvent.asStartElement();
                    if (startElement.getName().getLocalPart().equals("Customer")) {
                        team = new Team();
                    }

                }

                if (xmlEvent.isEndElement()) {
                    EndElement endElement = xmlEvent.asEndElement();
                    if (endElement.getName().getLocalPart().equals("Customer")) {
                        teams.add(team);
                    }
                }
            }

        }catch (FileNotFoundException | XMLStreamException e) {
        e.printStackTrace();
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
}
