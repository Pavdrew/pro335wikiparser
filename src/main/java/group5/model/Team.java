package group5.model;

public class Team {

    private String teamName;

    private String teamLocation;

    private String sportType;

    public Team(String teamName, String teamLocation, String sportType) {
        setTeamName(teamName);
        setTeamLocation(teamLocation);
        setSportType(sportType);
    }

    public String getSportType() {
        return sportType;
    }

    public void setSportType(String sportType) {
        this.sportType = sportType;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public String getTeamLocation() {
        return teamLocation;
    }

    public void setTeamLocation(String teamLocation) {
        this.teamLocation = teamLocation;
    }
}
