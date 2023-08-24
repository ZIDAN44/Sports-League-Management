package Entity;

import java.util.ArrayList;
import java.util.List;

public class Player {
    private int playerID;
    private String name;
    private int age;
    private String position;
    private String statistics;
    private Team team;
    private List<Team> selectedByTeams;
    private List<String> selectedTeam;

    public Player(int playerID, String name, int age, String position, String statistics) {
        this.playerID = playerID;
        this.name = name;
        this.age = age;
        this.position = position;
        this.statistics = statistics;
        this.selectedByTeams = new ArrayList<>();
    }

    public Player(String name, int age, String position, String statistics, Team team) {
        this.name = name;
        this.age = age;
        this.position = position;
        this.statistics = statistics;
        this.team = team;
        this.selectedByTeams = new ArrayList<>();
    }

    public Player(String name, int age, String position, String statistics) {
        this.name = name;
        this.age = age;
        this.position = position;
        this.statistics = statistics;
        this.selectedByTeams = new ArrayList<>();
    }

    public int getPlayerID() {
        return playerID;
    }

    public void setPlayerID(int playerID) {
        this.playerID = playerID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getStatistics() {
        return statistics;
    }

    public void setStatistics(String statistics) {
        this.statistics = statistics;
    }

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    public List<Team> getSelectedByTeams() {
        return selectedByTeams;
    }

    public void addSelectedByTeam(Team team) {
        selectedByTeams.add(team);
    }

    public List<String> getSelectedTeam() {
        return selectedTeam;
    }

    public void setSelectedTeam(List<String> selectedTeam) {
        this.selectedTeam = selectedTeam;
    }

    public String toString() {
        return "Player{" +
                "playerID='" + playerID + '\'' +
                ", name='" + name + '\'' +
                ", age=" + age +
                ", position='" + position + '\'' +
                ", statistics='" + statistics + '\'' +
                '}';
    }
}
