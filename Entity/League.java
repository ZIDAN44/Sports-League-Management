package Entity;

import java.time.LocalDate;

public class League {
    private int leagueID;
    private String leagueName;
    private String season;
    private LocalDate startDate;
    private LocalDate endDate;
    private String rulesAndRegulations;
    private CEO ceo;

    public League(int leagueID, String leagueName, String season, LocalDate startDate, LocalDate endDate,
            String rulesAndRegulations, CEO ceo) {
        this.leagueID = leagueID;
        this.leagueName = leagueName;
        this.season = season;
        this.startDate = startDate;
        this.endDate = endDate;
        this.rulesAndRegulations = rulesAndRegulations;
        this.ceo = ceo;
    }

    public League(String leagueName, String season, LocalDate startDate, LocalDate endDate,
            String rulesAndRegulations, CEO ceo) {
        this.leagueName = leagueName;
        this.season = season;
        this.startDate = startDate;
        this.endDate = endDate;
        this.rulesAndRegulations = rulesAndRegulations;
        this.ceo = ceo;
    }

    public int getLeagueID() {
        return leagueID;
    }

    public void setLeagueID(int leagueID) {
        this.leagueID = leagueID;
    }

    public String getLeagueName() {
        return leagueName;
    }

    public void setLeagueName(String leagueName) {
        this.leagueName = leagueName;
    }

    public String getSeason() {
        return season;
    }

    public void setSeason(String season) {
        this.season = season;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public String getRulesAndRegulations() {
        return rulesAndRegulations;
    }

    public void setRulesAndRegulations(String rulesAndRegulations) {
        this.rulesAndRegulations = rulesAndRegulations;
    }

    public CEO getCEO() {
        return ceo;
    }

    public void setCEO(CEO ceo) {
        this.ceo = ceo;
    }

    public String toString() {
        return "League{" +
                "leagueID='" + leagueID + '\'' +
                ", leagueName='" + leagueName + '\'' +
                ", season='" + season + '\'' +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", rulesAndRegulations='" + rulesAndRegulations + '\'' +
                '}';
    }
}
