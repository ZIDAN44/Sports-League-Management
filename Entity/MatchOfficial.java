package Entity;

public class MatchOfficial {
    private int officialID;
    private int matchID;
    private String name;
    private String ORules;

    public MatchOfficial(int officialID, String name) {
        this.officialID = officialID;
        this.name = name;
    }

    public MatchOfficial(int officialID, int matchID, String ORules) {
        this.officialID = officialID;
        this.matchID = matchID;
        this.ORules = ORules;
    }

    public MatchOfficial(String name) {
        this.name = name;
    }

    public int getOfficialID() {
        return officialID;
    }

    public void setOfficialID(int officialID) {
        this.officialID = officialID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getmatchID() {
        return matchID;
    }

    public String getOrules() {
        return ORules;
    }

    public String toString() {
        return "MatchOfficial{" +
                "officialID='" + officialID + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
