package GUI.CreateGUI;

import DataBase.DatabaseManager;
import Entity.CEO;
import Entity.League;
import GUI.Common.LookAndFeelUtil;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.time.LocalDate;

public class LeagueGUI extends JDialog implements ActionListener {
    private JTextField nameField;
    private JTextField seasonField;
    private JTextField startDateField;
    private JTextField endDateField;
    private JTextField rulesField;
    private JButton addButton, cancelButton;
    private DatabaseManager databaseManager;
    private CEO loggedInCEO;

    public LeagueGUI(JFrame parentFrame, DatabaseManager databaseManager, CEO loggedInCEO) {
        super(parentFrame, "Create League", true);
        this.databaseManager = databaseManager;
        this.loggedInCEO = loggedInCEO;
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        LookAndFeelUtil.setSubstanceMistSilverLookAndFeel();

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(null);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel leaguePanel = createLeaguePanel();
        leaguePanel.setBounds(10, 10, 300, 200);
        mainPanel.add(leaguePanel);

        addButton = new JButton("Add");
        addButton.setBounds(100, 220, 80, 30);
        addButton.addActionListener(this);
        mainPanel.add(addButton);

        cancelButton = new JButton("Cancel");
        cancelButton.setBounds(190, 220, 80, 30);
        cancelButton.addActionListener(this);
        mainPanel.add(cancelButton);

        add(mainPanel);

        setSize(330, 300);
        setLocationRelativeTo(parentFrame);
    }

    private JPanel createLeaguePanel() {
        JPanel panel = new JPanel();
        panel.setBorder(BorderFactory.createTitledBorder("League"));
        panel.setLayout(null);

        JLabel nameLabel = new JLabel("Name:");
        nameLabel.setBounds(10, 20, 80, 25);
        panel.add(nameLabel);

        nameField = new JTextField();
        nameField.setBounds(100, 20, 180, 25);
        panel.add(nameField);

        JLabel seasonLabel = new JLabel("Season:");
        seasonLabel.setBounds(10, 50, 80, 25);
        panel.add(seasonLabel);

        seasonField = new JTextField();
        seasonField.setBounds(100, 50, 180, 25);
        panel.add(seasonField);

        JLabel startDateLabel = new JLabel("Start Date:");
        startDateLabel.setBounds(10, 80, 80, 25);
        panel.add(startDateLabel);

        startDateField = new JTextField();
        startDateField.setBounds(100, 80, 180, 25);
        panel.add(startDateField);

        JLabel endDateLabel = new JLabel("End Date:");
        endDateLabel.setBounds(10, 110, 80, 25);
        panel.add(endDateLabel);

        endDateField = new JTextField();
        endDateField.setBounds(100, 110, 180, 25);
        panel.add(endDateField);

        JLabel rulesLabel = new JLabel("Rules:");
        rulesLabel.setBounds(10, 140, 80, 25);
        panel.add(rulesLabel);

        rulesField = new JTextField();
        rulesField.setBounds(100, 140, 180, 25);
        panel.add(rulesField);

        return panel;
    }

    private void addLeague() {
        String name = nameField.getText();
        String season = seasonField.getText();
        LocalDate startDate = LocalDate.parse(startDateField.getText().trim());
        LocalDate endDate = LocalDate.parse(endDateField.getText().trim());
        String rulesAndRegulations = rulesField.getText();

        League league = new League(name, season, startDate, endDate, rulesAndRegulations, loggedInCEO);

        try {
            if (databaseManager.insertLeagueAndOrganize(league)) {
                JOptionPane.showMessageDialog(this, "League added successfully!");
            } else {
                JOptionPane.showMessageDialog(this, "Failed to add league!");
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Failed to add league: " + ex.getMessage());
        }

        dispose();
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == addButton) {
            addLeague();
        } else if (e.getSource() == cancelButton) {
            dispose();
        }
    }
}
