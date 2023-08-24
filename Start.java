import GUI.LoginGUI.Login;
import DataBase.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Start {
    public static void main(String[] args) throws SQLException {

        DatabaseInitializer.initializeDatabase();
        Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/"+ "YOUR_DATABASE_NAME", "YOUR_USERNAME", "YOUR_PASSWORD"); // Replace with actual values
        DatabaseManager databaseManager = new DatabaseManager(connection);

        new Login(databaseManager);
    }
}
