package GUI.LoginGUI;

import Entity.*;
import GUI.CEO_GUI;
import GUI.Common.CustomCursor;
import GUI.Common.RoundedCornerButton;
import GUI.SignUPGUI.SignUP;
import DataBase.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Random;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

public class Login extends JFrame implements ActionListener, KeyListener {
    private JTextField ceoIdField;
    private JPasswordField passwordField;
    private JButton loginButton, signUpButton, exitButton;
    private JButton showPasswordButton, forgotPasswordButton;
    private DatabaseManager databaseManager;

    private final String ACCOUNT_SID = "YOUR_ACC_SID"; // Replace it with actual values
    private final String AUTH_TOKEN = "YOUR_ACC_TOKEN"; // Replace it with actual values
    private final String TWILIO_PHONE_NUMBER = "YOUR_PHONE_NUMBER"; // Replace it with actual values

    public Login(DatabaseManager databaseManager) {
        this.databaseManager = databaseManager;

        setupUI();
        setCursorIcon();
        setResizable(false);
        setVisible(true);
    }

    private void setupUI() {
        setTitle("CEO Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1100, 800);
        setLocationRelativeTo(null);

        ImageIcon icon = new ImageIcon(getClass().getResource(""));
        setIconImage(icon.getImage());

        ImageIcon backgroundIcon = new ImageIcon(getClass().getResource("/GUI/Pictures/CEOLogin/Background.png"));
        Image backgroundImage = backgroundIcon.getImage().getScaledInstance(getWidth(), getHeight(),
                Image.SCALE_SMOOTH);
        backgroundIcon = new ImageIcon(backgroundImage);

        JLabel backgroundLabel = new JLabel(backgroundIcon);
        backgroundLabel.setLayout(null);
        add(backgroundLabel);

        JLabel userIdLabel = new JLabel("CEO ID:");
        userIdLabel.setFont(new Font("Arial", Font.BOLD, 14));
        userIdLabel.setForeground(Color.WHITE);
        userIdLabel.setBounds(380, 315, 170, 30);

        ceoIdField = new JTextField();
        ceoIdField.setBackground(new Color(230, 230, 230));
        ceoIdField.setPreferredSize(new Dimension(150, 30));
        ceoIdField.setBounds(470, 315, 170, 30);

        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setFont(new Font("Arial", Font.BOLD, 14));
        passwordLabel.setForeground(Color.WHITE);
        passwordLabel.setBounds(380, 360, 170, 30);

        passwordField = new JPasswordField();
        passwordField.setBackground(new Color(230, 230, 230));
        passwordField.setPreferredSize(new Dimension(150, 30));
        passwordField.setBounds(470, 360, 170, 30);
        passwordField.addKeyListener(this);

        ImageIcon forgotIcon = new ImageIcon(
                getClass().getResource("/GUI/Pictures/CEOLogin/ForgetPassword.png.png"));
        Image scaledForgotImage = forgotIcon.getImage().getScaledInstance(400, 200, Image.SCALE_SMOOTH);
        ImageIcon scaledForgotIcon = new ImageIcon(scaledForgotImage);
        forgotPasswordButton = new RoundedCornerButton("", 45);
        forgotPasswordButton.setIcon(scaledForgotIcon);
        forgotPasswordButton.addActionListener(this);
        forgotPasswordButton.setBounds(435, 450, 230, 35);

        ImageIcon signUpIcon = new ImageIcon(getClass().getResource("/GUI/Pictures/CEOLogin/Signup.png"));
        Image scaledImage = signUpIcon.getImage().getScaledInstance(235, 200, Image.SCALE_SMOOTH);
        ImageIcon scaledIcon = new ImageIcon(scaledImage);
        signUpButton = new RoundedCornerButton("", 45);
        signUpButton.setIcon(scaledIcon);
        signUpButton.addActionListener(this);
        signUpButton.setBounds(435, 495, 230, 35);

        ImageIcon loginIcon = new ImageIcon(getClass().getResource("/GUI/Pictures/Common/login.png"));
        loginButton = new JButton(loginIcon);
        loginButton.addActionListener(this);
        loginButton.setPreferredSize(new Dimension(50, 30));
        loginButton.setBounds(515, 407, 70, 30);

        ImageIcon exitIcon = new ImageIcon(getClass().getResource("/GUI/Pictures/Common/Exit.png"));
        exitButton = new JButton(exitIcon);
        exitButton.addActionListener(this);
        exitButton.setPreferredSize(new Dimension(25, 25));
        exitButton.setBounds(1015, 30, 45, 45);
        exitButton.setBackground(Color.decode("#A8251A"));
        backgroundLabel.add(exitButton);

        ImageIcon eyeIcon = new ImageIcon(getClass().getResource("/GUI/Pictures/Common/eye.png"));
        showPasswordButton = new JButton(eyeIcon);
        showPasswordButton.addActionListener(this);
        showPasswordButton.setBounds(650, 362, 25, 25);

        backgroundLabel.add(userIdLabel);
        backgroundLabel.add(ceoIdField);
        backgroundLabel.add(passwordLabel);
        backgroundLabel.add(passwordField);
        backgroundLabel.add(loginButton);
        backgroundLabel.add(signUpButton);
        backgroundLabel.add(showPasswordButton);
        backgroundLabel.add(forgotPasswordButton);
    }

    private void setCursorIcon() {
        CustomCursor.setCustomCursor(this);
        CustomCursor.setHandCursor(ceoIdField);
        CustomCursor.setHandCursor(passwordField);
        CustomCursor.setHandCursor(loginButton);
        CustomCursor.setHandCursor(showPasswordButton);
        CustomCursor.setHandCursor(forgotPasswordButton);
        CustomCursor.setHandCursor(exitButton);

    }

    private void handleForgotPassword() {
        String ceoIdStr = ceoIdField.getText();
        try {
            int ceoId;
            try {
                ceoId = Integer.parseInt(ceoIdStr);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Invalid CEO ID! Please use a valid numeric ID.");
                ceoIdField.setText("");
                return;
            }

            Connection connection = databaseManager.getConnection();
            String sql = "SELECT * FROM ceop WHERE ceo_id = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, ceoId);

            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                String phoneNumber = resultSet.getString("phone_no");
                String otp = generateOTP();
                sendOTP(phoneNumber, otp);
                String inputOTP = JOptionPane.showInputDialog(this, "Please enter the OTP sent to your phone:");
                if (otp.equals(inputOTP)) {
                    String newPassword = JOptionPane.showInputDialog(this, "Enter your new password:");
                    updatePassword(ceoId, newPassword);
                    JOptionPane.showMessageDialog(this, "Password reset successful!");
                } else {
                    JOptionPane.showMessageDialog(this, "Incorrect OTP entered. Password reset failed!");
                }
            } else {
                JOptionPane.showMessageDialog(this, "No CEO found with the provided ID!");
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Can't connect to the database!");
        }
    }

    private String generateOTP() {
        Random random = new Random();
        int otpValue = 100000 + random.nextInt(900000);
        return String.valueOf(otpValue);
    }

    private void sendOTP(String phoneNumber, String otp) {
        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);

        try {
            Message message = Message.creator(
                    new PhoneNumber(phoneNumber),
                    new PhoneNumber(TWILIO_PHONE_NUMBER),
                    "Your OTP is: " + otp).create();

            System.out.println("OTP sent successfully to " + phoneNumber + ". SID: " + message.getSid());
        } catch (Exception e) {
            System.err.println("Failed to send OTP to " + phoneNumber + ": " + e.getMessage());
        }
    }

    private void updatePassword(int ceoId, String newPassword) {
        try {
            String hashedPassword = PasswordHashing.hashPassword(newPassword);

            Connection connection = databaseManager.getConnection();
            String sql = "UPDATE ceop SET password = ? WHERE ceo_id = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, hashedPassword);
            statement.setInt(2, ceoId);

            int rowsUpdated = statement.executeUpdate();

            if (rowsUpdated > 0) {
                System.out.println("Password updated successfully.");
            } else {
                System.out.println("Failed to update password.");
            }
        } catch (SQLException ex) {
            System.err.println("Error updating password: " + ex.getMessage());
        }
    }

    private void performLogin(String ceoIdStr, String password) {
        try {
            int ceoId;
            try {
                ceoId = Integer.parseInt(ceoIdStr);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Invalid CEO ID! Please use a valid numeric ID.");
                ceoIdField.setText("");
                passwordField.setText("");
                return;
            }

            Connection connection = databaseManager.getConnection();
            String sql = "SELECT * FROM ceop WHERE ceo_id = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, ceoId);

            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                String hashedPassword = resultSet.getString("password");

                if (PasswordHashing.checkPassword(password, hashedPassword)) {
                    CEO loggedInCEO = new CEO(resultSet.getInt("ceo_id"), resultSet.getString("ceo_name"),
                            resultSet.getString("phone_no"), resultSet.getString("email"));
                    dispose();
                    new CEO_GUI(databaseManager, loggedInCEO);
                } else {
                    JOptionPane.showMessageDialog(this, "Invalid password!");
                }
            } else {
                JOptionPane.showMessageDialog(this, "Invalid CEO ID!");
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Can't connect to the database!");
        }
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == loginButton) {
            String ceoIdStr = ceoIdField.getText();
            String password = new String(passwordField.getPassword());
            performLogin(ceoIdStr, password);
        } else if (e.getSource() == showPasswordButton) {
            char currentEchoChar = passwordField.getEchoChar();
            char newEchoChar = (currentEchoChar == 0) ? '\u2022' : 0;
            passwordField.setEchoChar(newEchoChar);
        } else if (e.getSource() == forgotPasswordButton) {
            handleForgotPassword();
        } else if (e.getSource() == signUpButton) {
            new SignUP(databaseManager);
        } else if (e.getSource() == exitButton) {
            this.dispose();
        }
    }

    public void keyPressed(KeyEvent e) {
        if (e.getSource() == passwordField && e.getKeyCode() == KeyEvent.VK_ENTER) {
            String ceoIdStr = ceoIdField.getText();
            String password = new String(passwordField.getPassword());
            performLogin(ceoIdStr, password);
        }
    }

    public void keyReleased(KeyEvent e) {
    }

    public void keyTyped(KeyEvent e) {
    }
}
