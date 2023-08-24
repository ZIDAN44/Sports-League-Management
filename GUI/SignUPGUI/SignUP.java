package GUI.SignUPGUI;

import GUI.Common.CustomCursor;
import GUI.Common.LookAndFeelUtil;
import DataBase.DatabaseManager;
import Entity.CEO;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

public class SignUP extends JDialog implements ActionListener {
    private JTextField usernameField, phoneNoField, emailField;
    private JPasswordField passwordField, retypePasswordField;
    private JButton signUpButton;
    private DatabaseManager databaseManager;

    public SignUP(DatabaseManager databaseManager) {
        super((Frame) null, "CEO Sign Up", true);
        this.databaseManager = databaseManager;

        SwingUtilities.invokeLater(() -> LookAndFeelUtil.setSubstanceMistSilverLookAndFeel());

        setupUI();
        setCursorIcon();
        setResizable(false);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void setupUI() {
        setTitle("CEO Sign Up");
        setSize(420, 390);
        ImageIcon icon = new ImageIcon(getClass().getResource("/GUI/Pictures/Common/Icon.png"));
        setIconImage(icon.getImage());

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(null);

        JLabel usernameLabel = createLabel("Username:", 20, 20);
        usernameField = createTextField(140, 20);

        JLabel phoneNoLabel = createLabel("Phone NO:", 20, 65);
        phoneNoField = createTextField(140, 65);

        JLabel emailLabel = createLabel("Email:", 20, 110);
        emailField = createTextField(140, 110);

        JLabel passwordLabel = createLabel("Password:", 20, 155);
        passwordField = createPasswordField(140, 155);

        JLabel retypePasswordLabel = createLabel("Re-type Pass:", 20, 200);
        retypePasswordField = createPasswordField(140, 200);

        signUpButton = createButton("Sign Up", 160, 280);

        mainPanel.add(usernameLabel);
        mainPanel.add(usernameField);
        mainPanel.add(phoneNoLabel);
        mainPanel.add(phoneNoField);
        mainPanel.add(emailLabel);
        mainPanel.add(emailField);
        mainPanel.add(passwordLabel);
        mainPanel.add(passwordField);
        mainPanel.add(retypePasswordLabel);
        mainPanel.add(retypePasswordField);
        mainPanel.add(signUpButton);

        setContentPane(mainPanel);
    }

    private JLabel createLabel(String text, int x, int y) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.BOLD, 14));
        label.setForeground(Color.BLACK);
        label.setBounds(x, y, 250, 30);
        return label;
    }

    private JTextField createTextField(int x, int y) {
        JTextField textField = new JTextField();
        textField.setBounds(x, y, 250, 30);
        return textField;
    }

    private JPasswordField createPasswordField(int x, int y) {
        JPasswordField passwordField = new JPasswordField();
        passwordField.setBounds(x, y, 250, 30);
        return passwordField;
    }

    private JButton createButton(String text, int x, int y) {
        JButton button = new JButton(text);
        button.addActionListener(this);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setForeground(Color.BLACK);
        button.setBounds(x, y, 100, 35);
        return button;
    }

    private void setCursorIcon() {
        Component[] components = { usernameField, phoneNoField, emailField, passwordField, retypePasswordField,
                signUpButton };
        CustomCursor.setCustomCursor(this);
        for (Component component : components) {
            CustomCursor.setHandCursor(component);
        }
    }

    private void showError() {
        JOptionPane.showMessageDialog(this, "Failed to sign up!!", "Error", JOptionPane.ERROR_MESSAGE);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == signUpButton) {
            String username = usernameField.getText();
            String phoneNo = phoneNoField.getText();
            String email = emailField.getText();
            String password = new String(passwordField.getPassword());
            String retypePassword = new String(retypePasswordField.getPassword());

            if (username.isEmpty() || phoneNo.isEmpty() || password.isEmpty() || retypePassword.isEmpty()
                    || email.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill in all fields.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (!password.equals(retypePassword)) {
                JOptionPane.showMessageDialog(this, "Passwords do not match.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                CEO ceo = new CEO(username, phoneNo, email, password);
                boolean success = databaseManager.insertCEO(ceo);

                if (success) {
                    int ceoId;
                    try {
                        ceoId = databaseManager.getLastInsertedCEOId();
                    } catch (SQLException ex) {
                        showError();
                        return;
                    }

                    if (ceoId != -1) {
                        JOptionPane.showMessageDialog(this,
                                "<html>Sign up successful! Please note down your CEO ID:<br><br>"
                                        + "<font color=\"#FF0000\"><b>=== CEO ID: " + ceoId
                                        + " ===</b></font></html>",
                                "Success", JOptionPane.INFORMATION_MESSAGE);
                        dispose();
                        // new CEOLogin(databaseManager);
                    } else {
                        showError();
                    }
                } else {
                    showError();
                }
            } catch (SQLException ex) {
                showError();
            }
        }
    }
}
