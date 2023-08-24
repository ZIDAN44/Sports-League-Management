package GUI.CreateGUI;

import DataBase.DatabaseManager;
import Entity.Player;
import GUI.Common.LookAndFeelUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.Period;

public class PlayerGUI extends JDialog implements ActionListener {
    private JTextField playerNameField;
    private JTextField birthDateField;
    private JTextField ageField;
    private JTextField positionField;
    private JTextField statisticsField;
    private JButton addButton, cancelButton;
    private DatabaseManager databaseManager;

    public PlayerGUI(JFrame parentFrame, DatabaseManager databaseManager) {
        super(parentFrame, "Create Player", true);
        this.databaseManager = databaseManager;
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        LookAndFeelUtil.setSubstanceMistSilverLookAndFeel();

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel playerPanel = createPlayerPanel();
        mainPanel.add(playerPanel);

        JPanel buttonPanel = new JPanel();
        addButton = new JButton("Add");
        addButton.addActionListener(this);
        cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(this);
        buttonPanel.add(addButton);
        buttonPanel.add(cancelButton);

        mainPanel.add(buttonPanel);
        add(mainPanel);

        pack();
        setSize(330, 270);
        setLocationRelativeTo(parentFrame);
    }

    private JPanel createPlayerPanel() {
        JPanel panel = new JPanel();
        panel.setBorder(BorderFactory.createTitledBorder("Player"));
        panel.setLayout(new GridLayout(5, 2, 5, 5));

        JLabel playerNameLabel = new JLabel("Player Name:");
        playerNameField = new JTextField();

        JLabel birthDateLabel = new JLabel("Birth Date (yyyy-mm-dd):");
        birthDateField = new JTextField();

        JLabel ageLabel = new JLabel("Age:");
        ageField = new JTextField();
        ageField.setEditable(false);

        JLabel positionLabel = new JLabel("Position:");
        positionField = new JTextField();

        JLabel statisticsLabel = new JLabel("Statistics:");
        statisticsField = new JTextField();

        panel.add(playerNameLabel);
        panel.add(playerNameField);
        panel.add(birthDateLabel);
        panel.add(birthDateField);
        panel.add(ageLabel);
        panel.add(ageField);
        panel.add(positionLabel);
        panel.add(positionField);
        panel.add(statisticsLabel);
        panel.add(statisticsField);

        return panel;
    }

    private void addPlayer() {
        String playerName = playerNameField.getText();
        String birthDateStr = birthDateField.getText();
        String position = positionField.getText();
        String statistics = statisticsField.getText();

        // Convert birth date to LocalDate object
        LocalDate birthDate = LocalDate.parse(birthDateStr);

        // Calculate age using birth date and current date
        int age = calculateAge(birthDate);

        Player player = new Player(playerName, age, position, statistics);

        try {
            if (databaseManager.insertPlayer(player)) {
                JOptionPane.showMessageDialog(this, "Player added successfully!");
            } else {
                JOptionPane.showMessageDialog(this, "Failed to add player!");
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Failed to add player: " + ex.getMessage());
        }

        dispose();
    }

    private int calculateAge(LocalDate birthDate) {
        LocalDate currentDate = LocalDate.now();
        return Period.between(birthDate, currentDate).getYears();
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == addButton) {
            addPlayer();
        } else if (e.getSource() == cancelButton) {
            dispose();
        }
    }
}
