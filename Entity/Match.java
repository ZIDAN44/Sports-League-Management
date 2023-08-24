package Entity;

import java.time.LocalDate;

public class Match {
    private int matchID;
    private int s_no;
    private int ceo_id;
    private LocalDate date;
    private String time;
    private String venue;
    private League league;
    private Team homeTeam;
    private Team awayTeam;

    public Match(int matchID, LocalDate date, String time, String venue) {
        this.matchID = matchID;
        this.date = date;
        this.time = time;
        this.venue = venue;
    }

    public Match(LocalDate date, String time, String venue) {
        this.date = date;
        this.time = time;
        this.venue = venue;
    }

    public Match(int s_no, LocalDate date, String time, String venue, int matchID, int ceo_id) {
        this.s_no = s_no;
        this.date = date;
        this.time = time;
        this.venue = venue;
        this.matchID = matchID;
        this.ceo_id = ceo_id;
    }

    public int getMatchID() {
        return matchID;
    }

    public void setMatchID(int matchID) {
        this.matchID = matchID;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getVenue() {
        return venue;
    }

    public void setVenue(String venue) {
        this.venue = venue;
    }

    public League getLeague() {
        return league;
    }

    public void setLeague(League league) {
        this.league = league;
    }

    public Team getHomeTeam() {
        return homeTeam;
    }

    public void setHomeTeam(Team homeTeam) {
        this.homeTeam = homeTeam;
    }

    public Team getAwayTeam() {
        return awayTeam;
    }

    public void setAwayTeam(Team awayTeam) {
        this.awayTeam = awayTeam;
    }

    public int getSchduleNo() {
        return s_no;
    }

    public int getceoID() {
        return ceo_id;
    }

    public String toString() {
        return "Match{" +
                "matchID=" + matchID +
                ", date=" + date +
                ", time='" + time + '\'' +
                ", venue='" + venue + '\'' +
                ", league=" + league +
                ", homeTeam=" + homeTeam +
                ", awayTeam=" + awayTeam +
                '}';
    }
}
