package DataBase;

import Entity.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class DatabaseManager {
    private Connection connection;

    public DatabaseManager(Connection connection) {
        this.connection = connection;
    }

    public Connection getConnection() {
        return connection;
    }

    public boolean insertCEO(CEO ceo) throws SQLException {
        String ceoQuery = "INSERT INTO ceo (ceo_name, email, phone_no) VALUES (?, ?, ?)";
        String ceopQuery = "INSERT INTO ceop (ceo_id, ceo_name, email, phone_no, password) VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement ceoStatement = connection.prepareStatement(ceoQuery);
                PreparedStatement ceopStatement = connection.prepareStatement(ceopQuery)) {

            // Insert data into ceo table
            ceoStatement.setString(1, ceo.getName());
            ceoStatement.setString(2, ceo.getEmail());
            ceoStatement.setString(3, ceo.getPhoneNumber());

            int ceoRowsInserted = ceoStatement.executeUpdate();

            if (ceoRowsInserted > 0) {
                // Get the last inserted CEO ID
                int ceoId = getLastInsertedCEOId();

                // Insert data into ceop table
                ceopStatement.setInt(1, ceoId);
                ceopStatement.setString(2, ceo.getName());
                ceopStatement.setString(3, ceo.getEmail());
                ceopStatement.setString(4, ceo.getPhoneNumber());

                // Hash the password before inserting
                String hashedPassword = PasswordHashing.hashPassword(ceo.getPassword());
                ceopStatement.setString(5, hashedPassword);

                int ceopRowsInserted = ceopStatement.executeUpdate();

                return ceopRowsInserted > 0;
            }

            return false;
        }
    }

    // Insert League & Organize into the database
    public boolean insertLeagueAndOrganize(League league) throws SQLException {
        String insertLeagueQuery = "INSERT INTO league (l_name, season, l_rules, s_date, e_date) VALUES (?, ?, ?, ?, ?)";
        String insertOrganizeQuery = "INSERT INTO organize (l_id, l_name, season, l_rules, s_date, e_date, ceo_id) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement insertLeagueStatement = connection.prepareStatement(insertLeagueQuery,
                Statement.RETURN_GENERATED_KEYS);
                PreparedStatement insertOrganizeStatement = connection.prepareStatement(insertOrganizeQuery)) {

            connection.setAutoCommit(false);

            insertLeagueStatement.setString(1, league.getLeagueName());
            insertLeagueStatement.setString(2, league.getSeason());
            insertLeagueStatement.setString(3, league.getRulesAndRegulations());
            insertLeagueStatement.setDate(4, Date.valueOf(league.getStartDate()));
            insertLeagueStatement.setDate(5, Date.valueOf(league.getEndDate()));

            int rowsInserted = insertLeagueStatement.executeUpdate();
            if (rowsInserted <= 0) {
                connection.rollback();
                connection.setAutoCommit(true);
                return false;
            }

            try (ResultSet generatedKeys = insertLeagueStatement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    int leagueId = generatedKeys.getInt(1);

                    insertOrganizeStatement.setInt(1, leagueId);
                    insertOrganizeStatement.setString(2, league.getLeagueName());
                    insertOrganizeStatement.setString(3, league.getSeason());
                    insertOrganizeStatement.setString(4, league.getRulesAndRegulations());
                    insertOrganizeStatement.setDate(5, Date.valueOf(league.getStartDate()));
                    insertOrganizeStatement.setDate(6, Date.valueOf(league.getEndDate()));
                    insertOrganizeStatement.setInt(7, league.getCEO().getID());

                    int rowsOrganized = insertOrganizeStatement.executeUpdate();
                    if (rowsOrganized <= 0) {
                        connection.rollback();
                        connection.setAutoCommit(true);
                        return false;
                    }

                    connection.commit();
                    connection.setAutoCommit(true);
                    return true;
                } else {
                    connection.rollback();
                    connection.setAutoCommit(true);
                    return false;
                }
            }
        } catch (SQLException e) {
            connection.rollback();
            connection.setAutoCommit(true);
            throw e;
        }
    }

    // Insert Assemble into the database
    public boolean insertAssemble(int leagueId, int teamId) throws SQLException {
        String insertAssembleQuery = "INSERT INTO assemble (l_id, t_id) VALUES (?, ?)";

        try (PreparedStatement statement = connection.prepareStatement(insertAssembleQuery)) {
            statement.setInt(1, leagueId);
            statement.setInt(2, teamId);

            int rowsInserted = statement.executeUpdate();
            return rowsInserted > 0;
        }
    }

    // Insert Team into the database
    public boolean insertTeam(Team team) throws SQLException {
        String query = "INSERT INTO team (t_name, logo) VALUES (?, ?)";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, team.getTeamName());
            statement.setString(2, team.getLogo());

            int rowsInserted = statement.executeUpdate();
            return rowsInserted > 0;
        }
    }

    // Insert Player into the database
    public boolean insertPlayer(Player player) throws SQLException {
        String query = "INSERT INTO player (p_name, age, p_position, statistics) VALUES (?, ?, ?, ?)";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, player.getName());
            statement.setInt(2, player.getAge());
            statement.setString(3, player.getPosition());
            statement.setString(4, player.getStatistics());

            int rowsInserted = statement.executeUpdate();
            return rowsInserted > 0;
        }
    }

    // Insert Coach and corresponding hire information into the database
    public boolean insertCoachWithHire(Coach coach, LocalDate h_date, int ceo_id) throws SQLException {
        String coachQuery = "INSERT INTO coach (c_name, salary, c_position) VALUES (?, ?, ?)";
        String hireQuery = "INSERT INTO hire (c_id, c_name, salary, c_position, h_date, ceo_id) VALUES (?, ?, ?, ?, ?, ?)";

        try (PreparedStatement coachStatement = connection.prepareStatement(coachQuery,
                Statement.RETURN_GENERATED_KEYS);
                PreparedStatement hireStatement = connection.prepareStatement(hireQuery)) {

            // Insert into the coach table
            coachStatement.setString(1, coach.getName());
            coachStatement.setDouble(2, coach.getSalary());
            coachStatement.setString(3, coach.getPosition());
            int rowsInsertedCoach = coachStatement.executeUpdate();

            if (rowsInsertedCoach > 0) {
                // Get the generated coach ID
                ResultSet generatedKeys = coachStatement.getGeneratedKeys();
                int c_id = -1;
                if (generatedKeys.next()) {
                    c_id = generatedKeys.getInt(1);
                }

                // Insert into the hire table
                hireStatement.setInt(1, c_id);
                hireStatement.setString(2, coach.getName());
                hireStatement.setDouble(3, coach.getSalary());
                hireStatement.setString(4, coach.getPosition());
                hireStatement.setDate(5, Date.valueOf(h_date));
                hireStatement.setInt(6, ceo_id);
                int rowsInsertedHire = hireStatement.executeUpdate();

                return rowsInsertedHire > 0;
            }
        }

        return false;
    }

    // Insert MatchOfficial into the database
    public boolean insertMatchOfficial(MatchOfficial matchOfficial) throws SQLException {
        String query = "INSERT INTO match_official (mof_name) VALUES (?)";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, matchOfficial.getName());

            int rowsInserted = statement.executeUpdate();
            return rowsInserted > 0;
        }
    }

    // Insert Match into the database
    public boolean insertMatch(Match match) throws SQLException {
        String query = "INSERT INTO match_info (m_date, m_time, venu) VALUES (?, ?, ?)";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setDate(1, Date.valueOf(match.getDate()));
            statement.setString(2, match.getTime());
            statement.setString(3, match.getVenue());

            int rowsInserted = statement.executeUpdate();
            return rowsInserted > 0;
        }
    }

    // Insert Match into the Schedule table
    public boolean insertSchedule(int matchId, Match match, String t1VSt2, int ceoId) throws SQLException {
        String query = "INSERT INTO schedule (m_id, m_date, m_time, venu, vs_name, ceo_id) VALUES (?, ?, ?, ?, ?, ?)";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, matchId);
            statement.setDate(2, Date.valueOf(match.getDate()));
            statement.setString(3, match.getTime());
            statement.setString(4, match.getVenue());
            statement.setString(5, t1VSt2);
            statement.setInt(6, ceoId);

            int rowsInserted = statement.executeUpdate();
            return rowsInserted > 0;
        }
    }

    // Insert player-team association into the database
    public boolean insertAssociates(int teamId, int playerId) throws SQLException {
        String query = "INSERT INTO associate (t_id, p_id) VALUES (?, ?)";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, teamId);
            statement.setInt(2, playerId);

            int rowsInserted = statement.executeUpdate();
            return rowsInserted > 0;
        }
    }

    // Insert a coach-team relationship into the supervise table
    public boolean insertSupervise(int coachId, int teamId) throws SQLException {
        String query = "INSERT INTO supervise (c_id, t_id) VALUES (?, ?)";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, coachId);
            statement.setInt(2, teamId);

            int rowsInserted = statement.executeUpdate();
            return rowsInserted > 0;
        }
    }

    // Insert a team-match relationship into the participate table
    public boolean insertParticipate(int teamId1, int teamId2, int matchId, String participationType)
            throws SQLException {
        String query = "INSERT INTO participate (t_id, m_id, pt_name) VALUES (?, ?, ?), (?, ?, ?)";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, teamId1);
            statement.setInt(2, matchId);
            statement.setString(3, participationType);

            statement.setInt(4, teamId2);
            statement.setInt(5, matchId);
            statement.setString(6, participationType);

            int rowsInserted = statement.executeUpdate();
            return rowsInserted > 0;
        }
    }

    // Insert match official and maintain relationship into the maintain table
    public boolean insertMaintain(MatchOfficial matchOfficial, int matchId, String officialRules) throws SQLException {
        String query = "INSERT INTO maintain (mof_id, m_id, o_rules) VALUES (?, ?, ?)";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            int matchOfficialId = getMatchOfficialIdByName(matchOfficial.getName());
            statement.setInt(1, matchOfficialId);
            statement.setInt(2, matchId);
            statement.setString(3, officialRules);

            int rowsInserted = statement.executeUpdate();
            return rowsInserted > 0;
        }
    }

    // Retrieve the match official ID by name
    public int getMatchOfficialIdByName(String matchOfficialName) {
        String query = "SELECT mof_id FROM match_official WHERE mof_name = ?";
        int matchOfficialId = -1; // Default value if not found

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, matchOfficialName);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                matchOfficialId = resultSet.getInt("mof_id");
            }

            resultSet.close();
        } catch (SQLException e) {
            System.out.println("Failed to fetch match official ID: " + e.getMessage());
        }

        return matchOfficialId;
    }

    // Retrieve the league by name
    public List<League> getLeaguesByName(String searchQuery) throws SQLException {
        List<League> foundLeagues = new ArrayList<>();

        String query = "SELECT * FROM league AS l " +
                "JOIN organize AS o ON l.l_id = o.l_id " +
                "WHERE l.l_name LIKE ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, "%" + searchQuery + "%");
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    int leagueId = resultSet.getInt("l_id");
                    String leagueName = resultSet.getString("l_name");
                    String season = resultSet.getString("season");
                    LocalDate startDate = resultSet.getDate("s_date").toLocalDate();
                    LocalDate endDate = resultSet.getDate("e_date").toLocalDate();
                    String rulesAndRegulations = resultSet.getString("l_rules");

                    int ceoID = resultSet.getInt("ceo_id");
                    CEO ceo = getCEOById(ceoID);

                    League league = new League(leagueId, leagueName, season, startDate, endDate, rulesAndRegulations,
                            ceo);
                    foundLeagues.add(league);
                }
            }
        }

        return foundLeagues;
    }

    // Retrieve League or Organize by Name
    public List<League> getLeaguesWithOrganizeByName(String searchQuery) throws SQLException {
        List<League> foundLeagues = new ArrayList<>();

        String query = "SELECT * FROM league AS l " +
                "JOIN organize AS o ON l.l_id = o.l_id " +
                "WHERE l.l_name LIKE ?";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, "%" + searchQuery + "%");
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    int leagueId = resultSet.getInt("l_id");
                    String leagueName = resultSet.getString("l_name");
                    String season = resultSet.getString("season");
                    LocalDate startDate = resultSet.getDate("s_date").toLocalDate();
                    LocalDate endDate = resultSet.getDate("e_date").toLocalDate();
                    String rulesAndRegulations = resultSet.getString("l_rules");

                    int ceoID = resultSet.getInt("ceo_id");
                    CEO ceo = getCEOById(ceoID);

                    League league = new League(leagueId, leagueName, season, startDate, endDate, rulesAndRegulations,
                            ceo);
                    foundLeagues.add(league);
                }
            }
        }

        return foundLeagues;
    }

    // Retrieve Team By Name
    public List<Team> getTeamsByName(String searchQuery) throws SQLException {
        List<Team> foundTeams = new ArrayList<>();

        String query = "SELECT * FROM team WHERE t_name LIKE ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, "%" + searchQuery + "%");
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    int teamId = resultSet.getInt("t_id");
                    String teamName = resultSet.getString("t_name");
                    String logo = resultSet.getString("logo");

                    Team team = new Team(teamId, teamName, logo);
                    foundTeams.add(team);
                }
            }
        }

        return foundTeams;
    }

    // Retrieve Player By Name
    public List<Player> getPlayersByName(String searchQuery) throws SQLException {
        List<Player> foundPlayers = new ArrayList<>();

        String query = "SELECT * FROM player WHERE p_name LIKE ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, "%" + searchQuery + "%");
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    int playerId = resultSet.getInt("p_id");
                    String playerName = resultSet.getString("p_name");
                    int age = resultSet.getInt("age");
                    String position = resultSet.getString("p_position");
                    String statistics = resultSet.getString("statistics");

                    Player player = new Player(playerId, playerName, age, position, statistics);
                    foundPlayers.add(player);
                }
            }
        }

        return foundPlayers;
    }

    // Retrieve Match By Date
    public List<Match> getMatchesByDate(LocalDate searchQuery) throws SQLException {
        List<Match> foundMatches = new ArrayList<>();

        String query = "SELECT * FROM match_info WHERE m_date LIKE ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, "%" + searchQuery + "%");
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    int matchId = resultSet.getInt("m_id");
                    LocalDate matchDate = resultSet.getDate("m_date").toLocalDate();
                    String matchTime = resultSet.getString("m_time");
                    String venu = resultSet.getString("venu");

                    Match match = new Match(matchId, matchDate, matchTime, venu);
                    foundMatches.add(match);
                }
            }
        }

        return foundMatches;
    }

    // Retrieve Coach By Name
    public List<Coach> getCoachesByName(String searchQuery) throws SQLException {
        List<Coach> foundCoachs = new ArrayList<>();

        String query = "SELECT * FROM coach WHERE c_name LIKE ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, "%" + searchQuery + "%");
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    int coachId = resultSet.getInt("c_id");
                    String coachName = resultSet.getString("c_name");
                    double salary = resultSet.getDouble("salary");
                    String position = resultSet.getString("c_position");

                    Coach coach = new Coach(coachId, coachName, position, salary);
                    foundCoachs.add(coach);
                }
            }
        }

        return foundCoachs;
    }

    // Retrieve MatchOfficial By Name
    public List<MatchOfficial> getMatchOfficialsByName(String searchQuery) throws SQLException {
        List<MatchOfficial> foundMatchOfficials = new ArrayList<>();

        String query = "SELECT * FROM match_official WHERE mof_name LIKE ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, "%" + searchQuery + "%");
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    int mofId = resultSet.getInt("mof_id");
                    String mofName = resultSet.getString("mof_name");

                    MatchOfficial matchOfficial = new MatchOfficial(mofId, mofName);
                    foundMatchOfficials.add(matchOfficial);
                }
            }
        }

        return foundMatchOfficials;
    }

    // Retrieve League by ID
    public League getLeagueById(int leagueId) {
        String query = "SELECT * FROM league AS l " +
                "JOIN organize AS o ON l.l_id = o.l_id " +
                "WHERE l.l_id = ?";
        League league = null;

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, leagueId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    String leagueName = resultSet.getString("l_name");
                    String season = resultSet.getString("season");
                    LocalDate startDate = resultSet.getDate("s_date").toLocalDate();
                    LocalDate endDate = resultSet.getDate("e_date").toLocalDate();
                    String rulesAndRegulations = resultSet.getString("l_rules");

                    int ceoID = resultSet.getInt("ceo_id");
                    CEO ceo = getCEOById(ceoID);

                    league = new League(leagueId, leagueName, season, startDate, endDate, rulesAndRegulations, ceo);
                }
            }
        } catch (SQLException e) {
            System.out.println("Failed to fetch League from the database: " + e.getMessage());
        }

        return league;
    }

    // Retrieve CEO by ID
    public CEO getCEOById(int ceoId) {
        String query = "SELECT * FROM ceo WHERE ceo_id = ?";
        CEO ceo = null;

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, ceoId);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                String name = resultSet.getString("ceo_name");
                String email = resultSet.getString("email");
                String phone = resultSet.getString("phone_no");

                ceo = new CEO(name, email, phone);
                ceo.setID(ceoId);
            }

            resultSet.close();
        } catch (SQLException e) {
            System.out.println("Failed to fetch CEO from the database: " + e.getMessage());
        }

        return ceo;
    }

    // Retrieve Team by ID
    public Team getTeamById(int teamId) {
        String query = "SELECT * FROM team WHERE t_id = ?";
        Team team = null;

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, teamId);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                String teamName = resultSet.getString("t_name");
                String logo = resultSet.getString("logo");

                team = new Team(teamName, logo);
                team.setTeamID(teamId);
            }

            resultSet.close();
        } catch (SQLException e) {
            System.out.println("Failed to fetch Team from the database: " + e.getMessage());
        }

        return team;
    }

    // Retrieve Player by ID
    public Player getPlayerById(int playerId) {
        String query = "SELECT * FROM player WHERE p_id = ?";
        Player player = null;

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, playerId);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                String playerName = resultSet.getString("p_name");
                int age = resultSet.getInt("age");
                String position = resultSet.getString("p_position");
                String statistics = resultSet.getString("statistics");

                player = new Player(playerName, age, position, statistics);
                player.setPlayerID(playerId);
            }

            resultSet.close();
        } catch (SQLException e) {
            System.out.println("Failed to fetch Player from the database: " + e.getMessage());
        }

        return player;
    }

    // Retrieve Match by ID
    public Match getMatchById(int matchId) {
        String query = "SELECT * FROM match_info WHERE m_id = ?";
        Match match = null;

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, matchId);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                LocalDate matchDate = resultSet.getDate("m_date").toLocalDate();
                String matchTime = resultSet.getString("m_time");
                String venue = resultSet.getString("venu");

                match = new Match(matchDate, matchTime, venue);
                match.setMatchID(matchId);
            }

            resultSet.close();
        } catch (SQLException e) {
            System.out.println("Failed to fetch Match Table from the database: " + e.getMessage());
        }

        return match;
    }

    // Retrieve Coach by ID
    public Coach getCoachById(int coachId) {
        String query = "SELECT * FROM coach WHERE c_id = ?";
        Coach coach = null;

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, coachId);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                String coachName = resultSet.getString("c_name");
                String position = resultSet.getString("c_position");
                double salary = resultSet.getDouble("salary");

                coach = new Coach(coachName, position, salary);
                coach.setCoachID(coachId);
            }

            resultSet.close();
        } catch (SQLException e) {
            System.out.println("Failed to fetch Coach from the database: " + e.getMessage());
        }

        return coach;
    }

    // Retrieve MatchOfficial by ID
    public MatchOfficial getMatchOfficialById(int officialId) {
        String query = "SELECT * FROM match_official WHERE mof_id = ?";
        MatchOfficial matchOfficial = null;

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, officialId);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                String officialName = resultSet.getString("mof_name");

                matchOfficial = new MatchOfficial(officialName);
                matchOfficial.setOfficialID(officialId);
            }

            resultSet.close();
        } catch (SQLException e) {
            System.out.println("Failed to fetch MatchOfficial from the database: " + e.getMessage());
        }

        return matchOfficial;
    }

    // Retrieve all players from the database
    public List<Player> getAllPlayers() {
        String query = "SELECT * FROM player";
        List<Player> players = new ArrayList<>();

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                int playerId = resultSet.getInt("p_id");
                String playerName = resultSet.getString("p_name");
                int age = resultSet.getInt("age");
                String position = resultSet.getString("p_position");
                String statistics = resultSet.getString("statistics");

                Player player = new Player(playerName, age, position, statistics);
                player.setPlayerID(playerId);
                players.add(player);
            }

            resultSet.close();
        } catch (SQLException e) {
            System.out.println("Failed to fetch Players from the database: " + e.getMessage());
        }

        return players;
    }

    // Retrieve all teams from the database
    public List<Team> getAllTeams() {
        String query = "SELECT * FROM team";
        List<Team> teams = new ArrayList<>();

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                int teamId = resultSet.getInt("t_id");
                String teamName = resultSet.getString("t_name");
                String logo = resultSet.getString("logo");

                Team team = new Team(teamName, logo);
                team.setTeamID(teamId);
                teams.add(team);
            }

            resultSet.close();
        } catch (SQLException e) {
            System.out.println("Failed to fetch Teams from the database: " + e.getMessage());
        }

        return teams;
    }

    // Retrieve all coaches from the database
    public List<Coach> getAllCoaches() {
        String query = "SELECT * FROM coach";
        List<Coach> coaches = new ArrayList<>();

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                int coachId = resultSet.getInt("c_id");
                String coachName = resultSet.getString("c_name");
                String position = resultSet.getString("c_position");
                double salary = resultSet.getDouble("salary");

                Coach coach = new Coach(coachName, position, salary);
                coach.setCoachID(coachId);
                coaches.add(coach);
            }

            resultSet.close();
        } catch (SQLException e) {
            System.out.println("Failed to fetch Coaches from the database: " + e.getMessage());
        }

        return coaches;
    }

    // Retrieve all hires from the database
    public List<Coach> getAllHires() {
        String query = "SELECT * FROM hire";
        List<Coach> coaches = new ArrayList<>();

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                int coachId = resultSet.getInt("c_id");
                String coachName = resultSet.getString("c_name");
                String position = resultSet.getString("c_position");
                double salary = resultSet.getDouble("salary");
                LocalDate hireDate = resultSet.getDate("h_date").toLocalDate();

                Coach coach = new Coach(coachName, position, salary, hireDate);
                coach.setCoachID(coachId);
                coaches.add(coach);
            }

            resultSet.close();
        } catch (SQLException e) {
            System.out.println("Failed to fetch Coaches from the database: " + e.getMessage());
        }

        return coaches;
    }

    // Retrieve all MatchOfficials from the database
    public List<MatchOfficial> getAllMatchOfficials() {
        String query = "SELECT * FROM match_official";
        List<MatchOfficial> matchOfficials = new ArrayList<>();

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                int officialId = resultSet.getInt("mof_id");
                String officialName = resultSet.getString("mof_name");

                MatchOfficial matchOfficial = new MatchOfficial(officialName);
                matchOfficial.setOfficialID(officialId);
                matchOfficials.add(matchOfficial);
            }

            resultSet.close();
        } catch (SQLException e) {
            System.out.println("Failed to fetch MatchOfficials from the database: " + e.getMessage());
        }

        return matchOfficials;
    }

    // Retrieve All Matches
    public List<Match> getAllMatches() throws SQLException {
        List<Match> matchs = new ArrayList<>();
        String getAllMatchsQuery = "SELECT * FROM match_info";

        try (PreparedStatement statement = connection.prepareStatement(getAllMatchsQuery);
                ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                int matchID = resultSet.getInt("m_id");
                LocalDate matchDate = resultSet.getDate("m_date").toLocalDate();
                String matchTime = resultSet.getString("m_time");
                String venu = resultSet.getString("venu");

                Match match = new Match(matchID, matchDate, matchTime, venu);
                matchs.add(match);
            }
        }

        return matchs;
    }

    // Retrieve All Schedules
    public List<Match> getAllSchedules() throws SQLException {
        List<Match> matchs = new ArrayList<>();
        String getAllMatchsQuery = "SELECT * FROM schedule";

        try (PreparedStatement statement = connection.prepareStatement(getAllMatchsQuery);
                ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                int s_no = resultSet.getInt("s_no");
                LocalDate matchDate = resultSet.getDate("m_date").toLocalDate();
                String matchTime = resultSet.getString("m_time");
                String venu = resultSet.getString("venu");
                int m_id = resultSet.getInt("m_id");
                int ceo_id = resultSet.getInt("ceo_id");

                Match match = new Match(s_no, matchDate, matchTime, venu, m_id, ceo_id);
                matchs.add(match);
            }
        }

        return matchs;
    }

    // Retrieve All Participates
    public List<Team> getAllParticipates() throws SQLException {
        List<Team> teams = new ArrayList<>();
        String getAllTeamsQuery = "SELECT * FROM participate";

        try (PreparedStatement statement = connection.prepareStatement(getAllTeamsQuery);
                ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                int t_id = resultSet.getInt("t_id");
                int m_id = resultSet.getInt("m_id");
                String pt_name = resultSet.getString("pt_name");

                Team team = new Team(t_id, m_id, pt_name);
                teams.add(team);
            }
        }

        return teams;
    }

    // Retrieve All Leagues
    public List<League> getAllLeagues() throws SQLException {
        List<League> leagues = new ArrayList<>();
        String getAllLeaguesQuery = "SELECT * FROM league AS l " +
                "JOIN organize AS o ON l.l_id = o.l_id";

        try (PreparedStatement statement = connection.prepareStatement(getAllLeaguesQuery);
                ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                int leagueID = resultSet.getInt("l_id");
                String leagueName = resultSet.getString("l_name");
                String season = resultSet.getString("season");
                LocalDate startDate = resultSet.getDate("s_date").toLocalDate();
                LocalDate endDate = resultSet.getDate("e_date").toLocalDate();
                String rulesAndRegulations = resultSet.getString("l_rules");

                int ceoID = resultSet.getInt("ceo_id");
                CEO ceo = getCEOById(ceoID);

                League league = new League(leagueID, leagueName, season, startDate, endDate, rulesAndRegulations, ceo);
                leagues.add(league);
            }
        }

        return leagues;
    }

    // Retrieve All Team By League
    public List<Team> getTeamsByLeague(int leagueID) {
        String query = "SELECT team.t_id, team.t_name FROM team "
                + "INNER JOIN assemble ON team.t_id = assemble.t_id "
                + "WHERE assemble.l_id = ?";

        List<Team> teams = new ArrayList<>();

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, leagueID);

            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                int teamID = resultSet.getInt("t_id");
                String teamName = resultSet.getString("t_name");

                Team team = new Team(teamName);
                team.setTeamID(teamID);
                teams.add(team);
            }

            resultSet.close();
        } catch (SQLException e) {
            System.out.println("Failed to fetch Teams by League ID from the database: " + e.getMessage());
        }

        return teams;
    }

    // Retrieve All Maintenance
    public List<MatchOfficial> getAllMaintenance() throws SQLException {
        List<MatchOfficial> matchOfficials = new ArrayList<>();
        String getAllMatchOfficialQuery = "SELECT * FROM maintain";

        try (PreparedStatement statement = connection.prepareStatement(getAllMatchOfficialQuery);
                ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                int mof_ID = resultSet.getInt("mof_id");
                int m_ID = resultSet.getInt("m_id");
                String o_rules = resultSet.getString("o_rules");

                MatchOfficial matchOfficial = new MatchOfficial(mof_ID, m_ID, o_rules);
                matchOfficials.add(matchOfficial);
            }
        }

        return matchOfficials;
    }

    // Retrieve All Supervise
    public List<Team> getAllSupervise() throws SQLException {
        List<Team> supervise = new ArrayList<>();
        String getAllSuperviseQuery = "SELECT * FROM supervise";

        try (PreparedStatement statement = connection.prepareStatement(getAllSuperviseQuery);
                ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                int c_ID = resultSet.getInt("c_id");
                int t_ID = resultSet.getInt("t_id");

                Team team = new Team(c_ID, t_ID);
                supervise.add(team);
            }
        }

        return supervise;
    }

    // Retrieve the latest match ID from the match_info table
    public int getLatestMatchId() throws SQLException {
        String query = "SELECT MAX(m_id) AS latest_match_id FROM match_info";
        int latestMatchId = -1;

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                latestMatchId = resultSet.getInt("latest_match_id");
            }

            resultSet.close();
        } catch (SQLException e) {
            System.out.println("Failed to fetch the latest match ID: " + e.getMessage());
        }

        return latestMatchId;
    }

    // Retrieve team by team name
    public Team getTeamByName(String teamName) throws SQLException {
        String query = "SELECT t_id, t_name, logo FROM team WHERE t_name = ?";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, teamName);

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                int teamId = resultSet.getInt("t_id");
                String teamNameResult = resultSet.getString("t_name");
                String logo = resultSet.getString("logo");

                Team team = new Team(teamNameResult, logo);
                team.setTeamID(teamId);

                resultSet.close();
                return team;
            }
        } catch (SQLException e) {
            System.out.println("Failed to fetch Team by Team Name from the database: " + e.getMessage());
            throw e;
        }

        return null;
    }

    // Retrieve all players belonging to a specific team by team name
    public List<Player> getPlayersByTeamName(String teamName) throws SQLException {
        String query = "SELECT player.p_id, player.p_name, player.age, player.p_position, player.statistics "
                + "FROM player "
                + "INNER JOIN associate ON player.p_id = associate.p_id "
                + "INNER JOIN team ON associate.t_id = team.t_id "
                + "WHERE team.t_name = ?";

        List<Player> players = new ArrayList<>();

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, teamName);

            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                int playerId = resultSet.getInt("p_id");
                String playerName = resultSet.getString("p_name");
                int age = resultSet.getInt("age");
                String position = resultSet.getString("p_position");
                String statistics = resultSet.getString("statistics");

                Team team = getTeamByName(teamName);

                Player player = new Player(playerName, age, position, statistics, team);
                player.setPlayerID(playerId);
                players.add(player);
            }

            resultSet.close();
        } catch (SQLException e) {
            System.out.println("Failed to fetch Players by Team Name from the database: " + e.getMessage());
            throw e;
        }

        return players;
    }

    // Update League and Organize table
    public boolean updateLeagueAndOrganize(League league) throws SQLException {
        String updateLeagueQuery = "UPDATE league SET l_name = ?, season = ?, s_date = ?, e_date = ?, l_rules = ? WHERE l_id = ?";
        String updateOrganizeQuery = "UPDATE organize SET l_name = ?, season = ?, s_date = ?, e_date = ?, l_rules = ?, ceo_id = ? WHERE l_id = ?";

        try (PreparedStatement updateLeagueStatement = connection.prepareStatement(updateLeagueQuery);
                PreparedStatement updateOrganizeStatement = connection.prepareStatement(updateOrganizeQuery)) {

            connection.setAutoCommit(false);

            updateLeagueStatement.setString(1, league.getLeagueName());
            updateLeagueStatement.setString(2, league.getSeason());
            updateLeagueStatement.setDate(3, Date.valueOf(league.getStartDate()));
            updateLeagueStatement.setDate(4, Date.valueOf(league.getEndDate()));
            updateLeagueStatement.setString(5, league.getRulesAndRegulations());
            updateLeagueStatement.setInt(6, league.getLeagueID());

            int rowsUpdatedInLeague = updateLeagueStatement.executeUpdate();
            if (rowsUpdatedInLeague <= 0) {
                connection.rollback();
                connection.setAutoCommit(true);
                return false;
            }

            updateOrganizeStatement.setString(1, league.getLeagueName());
            updateOrganizeStatement.setString(2, league.getSeason());
            updateOrganizeStatement.setDate(3, Date.valueOf(league.getStartDate()));
            updateOrganizeStatement.setDate(4, Date.valueOf(league.getEndDate()));
            updateOrganizeStatement.setString(5, league.getRulesAndRegulations());
            updateOrganizeStatement.setInt(6, league.getCEO().getID());
            updateOrganizeStatement.setInt(7, league.getLeagueID());

            int rowsUpdatedInOrganize = updateOrganizeStatement.executeUpdate();
            if (rowsUpdatedInOrganize <= 0) {
                connection.rollback();
                connection.setAutoCommit(true);
                return false;
            }

            connection.commit();
            connection.setAutoCommit(true);
            return true;
        } catch (SQLException e) {
            connection.rollback();
            connection.setAutoCommit(true);
            throw e;
        }
    }

    // Update Team table
    public boolean updateTeam(Team team) throws SQLException {
        String updateTeamQuery = "UPDATE team SET t_name = ?, logo = ? WHERE t_id = ?";

        try (PreparedStatement statement = connection.prepareStatement(updateTeamQuery)) {
            statement.setString(1, team.getTeamName());
            statement.setString(2, team.getLogo());
            statement.setInt(3, team.getTeamID());

            int rowsAffected = statement.executeUpdate();
            return rowsAffected > 0;
        }
    }

    // Update Player table
    public boolean updatePlayer(Player player) throws SQLException {
        String updateLeagueQuery = "UPDATE player SET p_name = ?, age = ?, p_position = ?, statistics = ? WHERE p_id = ?";

        try (PreparedStatement statement = connection.prepareStatement(updateLeagueQuery)) {
            statement.setString(1, player.getName());
            statement.setInt(2, player.getAge());
            statement.setString(3, player.getPosition());
            statement.setString(4, player.getStatistics());
            statement.setInt(5, player.getPlayerID());

            int rowsAffected = statement.executeUpdate();
            return rowsAffected > 0;
        }
    }

    // Update Match and Schedule table
    public boolean updateMatch(Match match) throws SQLException {
        String updateMatchInfoQuery = "UPDATE match_info SET m_date = ?, m_time = ?, venu = ? WHERE m_id = ?";
        String updateScheduleQuery = "UPDATE schedule SET m_date = ?, m_time = ?, venu = ? WHERE m_id = ?";

        try (PreparedStatement matchInfoStatement = connection.prepareStatement(updateMatchInfoQuery);
                PreparedStatement scheduleStatement = connection.prepareStatement(updateScheduleQuery)) {

            // match_info table
            matchInfoStatement.setDate(1, Date.valueOf(match.getDate()));
            matchInfoStatement.setString(2, match.getTime());
            matchInfoStatement.setString(3, match.getVenue());
            matchInfoStatement.setInt(4, match.getMatchID());

            // schedule table
            scheduleStatement.setDate(1, Date.valueOf(match.getDate()));
            scheduleStatement.setString(2, match.getTime());
            scheduleStatement.setString(3, match.getVenue());
            scheduleStatement.setInt(4, match.getMatchID());

            int matchInfoRowsAffected = matchInfoStatement.executeUpdate();
            int scheduleRowsAffected = scheduleStatement.executeUpdate();
            return matchInfoRowsAffected > 0 && scheduleRowsAffected > 0;
        }
    }

    // Update MatchOfficial table
    public boolean updateMatchOfficial(MatchOfficial matchOfficial) throws SQLException {
        String updateLeagueQuery = "UPDATE match_official SET mof_name = ? WHERE mof_id = ?";

        try (PreparedStatement statement = connection.prepareStatement(updateLeagueQuery)) {
            statement.setString(1, matchOfficial.getName());
            statement.setInt(2, matchOfficial.getOfficialID());

            int rowsAffected = statement.executeUpdate();
            return rowsAffected > 0;
        }
    }

    // Update Coach table
    public boolean updateCoach(Coach coach) throws SQLException {
        String updateLeagueQuery = "UPDATE coach SET c_name = ?, salary = ?, c_position = ? WHERE c_id = ?";

        try (PreparedStatement statement = connection.prepareStatement(updateLeagueQuery)) {
            statement.setString(1, coach.getName());
            statement.setDouble(2, coach.getSalary());
            statement.setString(3, coach.getPosition());
            statement.setInt(4, coach.getCoachID());

            int rowsAffected = statement.executeUpdate();
            return rowsAffected > 0;
        }
    }

    // Retrieve Last Inserted ID of CEO
    public int getLastInsertedCEOId() throws SQLException {
        int ceoId = -1;

        String query = "SELECT LAST_INSERT_ID()";
        try (PreparedStatement statement = connection.prepareStatement(query);
                ResultSet resultSet = statement.executeQuery()) {
            if (resultSet.next()) {
                ceoId = resultSet.getInt(1);
            }
        }

        return ceoId;
    }

    // Close the database connection
    public void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            System.out.println("Error while closing the database connection: " + e.getMessage());
        }
    }
}
