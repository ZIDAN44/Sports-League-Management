package Entity;

public class Team {
    private int teamID;
    private int matchID;
    private int coachID;
    private String teamName;
    private String logo;
    private String pt_name;
    private Coach coach;

    public Team(String teamName) {
        this.teamName = teamName;
    }

    public Team(int teamID, String teamName, String logo) {
        this.teamID = teamID;
        this.teamName = teamName;
        this.logo = logo;
    }

    public Team(String teamName, String logo) {
        this.teamName = teamName;
        this.logo = logo;
    }

    public Team(int teamID, int matchID, String pt_name) {
        this.teamID = teamID;
        this.matchID = matchID;
        this.pt_name = pt_name;

    }

    public Team(int coachID, int teamID) {
        this.coachID = coachID;
        this.teamID = teamID;

    }

    public int getTeamID() {
        return teamID;
    }

    public int getMatchID() {
        return matchID;
    }

    public void setTeamID(int teamID) {
        this.teamID = teamID;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public Coach getCoach() {
        return coach;
    }

    public void setCoach(Coach coach) {
        this.coach = coach;
    }

    public String getPTName() {
        return pt_name;
    }

    public int getCoachID() {
        return coachID;
    }

    public String toString() {
        return "Team{" +
                "teamID=" + teamID +
                ", teamName='" + teamName + '\'' +
                ", logo=" + logo +
                '}';
    }
}
