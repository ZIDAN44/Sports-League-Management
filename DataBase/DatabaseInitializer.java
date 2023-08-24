package DataBase;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseInitializer {

        public static void initializeDatabase() {
                String url = "jdbc:mysql://localhost:3306/";
                String database = "YOUR_DATABASE_NAME"; // Replace it with actual values
                String username = "YOUR_DATABASE_USERNAME"; // Replace it with actual values
                String password = "YOUR_DATABASE_PASSWORD"; // Replace it with actual values

                try {
                        // Load the MySQL JDBC driver
                        Class.forName("com.mysql.cj.jdbc.Driver");

                        // Connect to the MySQL server
                        Connection connection = DriverManager.getConnection(url, username, password);

                        // Create a statement object
                        Statement statement = connection.createStatement();

                        // Create the database
                        statement.executeUpdate("CREATE DATABASE IF NOT EXISTS " + database);

                        // Use the database name
                        statement.execute("USE " + database);

                        // Create tables for entities

                        // CEO table
                        String createCEOTableQuery = "CREATE TABLE IF NOT EXISTS ceo ("
                                        + "ceo_id INT AUTO_INCREMENT PRIMARY KEY,"
                                        + "ceo_name VARCHAR(100),"
                                        + "phone_no VARCHAR(20),"
                                        + "email VARCHAR(100)"
                                        + ") AUTO_INCREMENT = 123";
                        statement.executeUpdate(createCEOTableQuery);

                        // CEOp table
                        String createCEOPTableQuery = "CREATE TABLE IF NOT EXISTS ceop ("
                                        + "ceo_id INT,"
                                        + "ceo_name VARCHAR(100),"
                                        + "phone_no VARCHAR(20),"
                                        + "email VARCHAR(100),"
                                        + "password VARCHAR(100),"
                                        + "PRIMARY KEY (ceo_id),"
                                        + "FOREIGN KEY (ceo_id) REFERENCES ceo(ceo_id)"
                                        + ")";
                        statement.executeUpdate(createCEOPTableQuery);

                        // League table
                        String createLeagueTableQuery = "CREATE TABLE IF NOT EXISTS league ("
                                        + "l_id INT AUTO_INCREMENT PRIMARY KEY,"
                                        + "l_name VARCHAR(100),"
                                        + "season VARCHAR(50),"
                                        + "l_rules TEXT,"
                                        + "s_date DATE,"
                                        + "e_date DATE"
                                        + ") AUTO_INCREMENT = 10";
                        statement.executeUpdate(createLeagueTableQuery);

                        // Organize table
                        String createOrganizeTableQuery = "CREATE TABLE IF NOT EXISTS organize ("
                                        + "l_id INT,"
                                        + "l_name VARCHAR(100),"
                                        + "season VARCHAR(50),"
                                        + "l_rules TEXT,"
                                        + "s_date DATE,"
                                        + "e_date DATE,"
                                        + "ceo_id INT,"
                                        + "PRIMARY KEY (l_id),"
                                        + "FOREIGN KEY (ceo_id) REFERENCES ceo(ceo_id),"
                                        + "FOREIGN KEY (l_id) REFERENCES league(l_id)"
                                        + ")";
                        statement.executeUpdate(createOrganizeTableQuery);

                        // Team table
                        String createTeamTableQuery = "CREATE TABLE IF NOT EXISTS team ("
                                        + "t_id INT AUTO_INCREMENT PRIMARY KEY,"
                                        + "t_name VARCHAR(100),"
                                        + "logo VARCHAR(100)"
                                        + ") AUTO_INCREMENT = 20";
                        statement.executeUpdate(createTeamTableQuery);

                        // Player table
                        String createPlayerTableQuery = "CREATE TABLE IF NOT EXISTS player ("
                                        + "p_id INT AUTO_INCREMENT PRIMARY KEY,"
                                        + "p_name VARCHAR(100),"
                                        + "age INT,"
                                        + "p_position VARCHAR(50),"
                                        + "statistics TEXT"
                                        + ") AUTO_INCREMENT = 40";
                        statement.executeUpdate(createPlayerTableQuery);

                        // Associate table
                        String createAssociateTableQuery = "CREATE TABLE IF NOT EXISTS associate ("
                                        + "t_id INT,"
                                        + "p_id INT,"
                                        + "PRIMARY KEY (t_id, p_id),"
                                        + "FOREIGN KEY (t_id) REFERENCES team(t_id),"
                                        + "FOREIGN KEY (p_id) REFERENCES player(p_id)"
                                        + ")";
                        statement.executeUpdate(createAssociateTableQuery);

                        // Coach table
                        String createCoachTableQuery = "CREATE TABLE IF NOT EXISTS coach ("
                                        + "c_id INT AUTO_INCREMENT PRIMARY KEY,"
                                        + "c_name VARCHAR(100),"
                                        + "salary DOUBLE,"
                                        + "c_position VARCHAR(50)"
                                        + ") AUTO_INCREMENT = 200";
                        statement.executeUpdate(createCoachTableQuery);

                        // Supervise table
                        String createSuperviseTableQuery = "CREATE TABLE IF NOT EXISTS supervise ("
                                        + "c_id INT,"
                                        + "t_id INT,"
                                        + "PRIMARY KEY (c_id, t_id),"
                                        + "FOREIGN KEY (c_id) REFERENCES coach(c_id),"
                                        + "FOREIGN KEY (t_id) REFERENCES team(t_id)"
                                        + ")";
                        statement.executeUpdate(createSuperviseTableQuery);

                        // Match_info table
                        String createMatchInfoTableQuery = "CREATE TABLE IF NOT EXISTS match_info ("
                                        + "m_id INT AUTO_INCREMENT PRIMARY KEY,"
                                        + "m_date DATE,"
                                        + "m_time VARCHAR(10),"
                                        + "venu VARCHAR(100)"
                                        + ") AUTO_INCREMENT = 300";
                        statement.executeUpdate(createMatchInfoTableQuery);

                        // Schedule table
                        String createScheduleTableQuery = "CREATE TABLE IF NOT EXISTS schedule ("
                                        + "s_no INT AUTO_INCREMENT PRIMARY KEY,"
                                        + "m_date DATE,"
                                        + "m_time VARCHAR(10),"
                                        + "venu VARCHAR(100),"
                                        + "vs_name VARCHAR(100),"
                                        + "m_id INT,"
                                        + "ceo_id INT,"
                                        + "FOREIGN KEY (ceo_id) REFERENCES ceo(ceo_id),"
                                        + "FOREIGN KEY (m_id) REFERENCES match_info(m_id)"
                                        + ")";
                        statement.executeUpdate(createScheduleTableQuery);

                        // Participate table
                        String createParticipateTableQuery = "CREATE TABLE IF NOT EXISTS participate ("
                                        + "t_id INT,"
                                        + "m_id INT,"
                                        + "pt_name VARCHAR(100),"
                                        + "PRIMARY KEY (t_id, m_id),"
                                        + "FOREIGN KEY (t_id) REFERENCES team(t_id),"
                                        + "FOREIGN KEY (m_id) REFERENCES match_info(m_id)"
                                        + ")";
                        statement.executeUpdate(createParticipateTableQuery);

                        // Match_official table
                        String createMatchOfficialTableQuery = "CREATE TABLE IF NOT EXISTS match_official ("
                                        + "mof_id INT AUTO_INCREMENT PRIMARY KEY,"
                                        + "mof_name VARCHAR(100)"
                                        + ") AUTO_INCREMENT = 400";
                        statement.executeUpdate(createMatchOfficialTableQuery);

                        // Maintain table
                        String createMaintainTableQuery = "CREATE TABLE IF NOT EXISTS maintain ("
                                        + "mof_id INT,"
                                        + "m_id INT,"
                                        + "o_rules TEXT,"
                                        + "PRIMARY KEY (mof_id, m_id),"
                                        + "FOREIGN KEY (mof_id) REFERENCES match_official(mof_id),"
                                        + "FOREIGN KEY (m_id) REFERENCES match_info(m_id)"
                                        + ")";
                        statement.executeUpdate(createMaintainTableQuery);

                        // Hire table
                        String createHireTableQuery = "CREATE TABLE IF NOT EXISTS hire ("
                                        + "c_id INT,"
                                        + "c_name VARCHAR(100),"
                                        + "salary DOUBLE,"
                                        + "c_position VARCHAR(50),"
                                        + "h_date DATE,"
                                        + "ceo_id INT,"
                                        + "PRIMARY KEY (c_id),"
                                        + "FOREIGN KEY (ceo_id) REFERENCES ceo(ceo_id)"
                                        + ")";
                        statement.executeUpdate(createHireTableQuery);

                        // Assemble table
                        String createAssembleTableQuery = "CREATE TABLE IF NOT EXISTS assemble ("
                                        + "l_id INT,"
                                        + "t_id INT,"
                                        + "PRIMARY KEY (l_id, t_id),"
                                        + "FOREIGN KEY (l_id) REFERENCES league(l_id),"
                                        + "FOREIGN KEY (t_id) REFERENCES team(t_id)"
                                        + ")";
                        statement.executeUpdate(createAssembleTableQuery);

                        // Create view table
                        createViewTable(connection);

                        // Close statement and connection
                        statement.close();
                        connection.close();

                        System.out.println("Sports League Management System database created successfully!");
                } catch (ClassNotFoundException | SQLException e) {
                        e.printStackTrace();
                }
        }

        private static void createViewTable(Connection connection) throws SQLException {
                String createViewTableQuery = "CREATE OR REPLACE VIEW MatchInfoView AS "
                                + "SELECT t.t_name AS `Team Name`, "
                                + "l.l_name AS `League Name`, l.season AS `Season`, "
                                + "m.m_date AS `Match Date`, m.m_time AS `Match Time`, s.vs_name AS `VS` "
                                + "FROM team t "
                                + "JOIN assemble a ON t.t_id = a.t_id "
                                + "JOIN organize o ON a.l_id = o.l_id "
                                + "JOIN league l ON o.l_id = l.l_id "
                                + "JOIN participate p ON t.t_id = p.t_id "
                                + "JOIN match_info m ON p.m_id = m.m_id "
                                + "JOIN schedule s ON p.m_id = s.m_id";

                Statement statement = connection.createStatement();
                statement.executeUpdate(createViewTableQuery);
                statement.close();
        }
}
