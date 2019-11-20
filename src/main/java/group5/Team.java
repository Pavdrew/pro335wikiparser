package group5;

public class Team {

    private String teamName;

    private String teamLocation;

    public Team(String teamName, String teamLocation) {
        setTeamName(teamName);
        setTeamLocation(teamLocation);
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
