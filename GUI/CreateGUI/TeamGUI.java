package GUI.CreateGUI;

import DataBase.DatabaseManager;
import Entity.Team;
import GUI.Common.LookAndFeelUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

public class TeamGUI extends JDialog implements ActionListener {
    private JTextField nameField;
    private JTextField logoField;
    private JButton addButton, cancelButton;
    private DatabaseManager databaseManager;

    public TeamGUI(JFrame parentFrame, DatabaseManager databaseManager) {
        super(parentFrame, "Create Team", true);
        this.databaseManager = databaseManager;
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        LookAndFeelUtil.setSubstanceMistSilverLookAndFeel();

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel teamPanel = createTeamPanel();
        mainPanel.add(teamPanel);

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
        setSize(350, 200);
        setLocationRelativeTo(parentFrame);
    }

    private JPanel createTeamPanel() {
        JPanel panel = new JPanel();
        panel.setBorder(BorderFactory.createTitledBorder("Team"));
        panel.setLayout(new GridLayout(2, 2, 5, 5));

        JLabel nameLabel = new JLabel("Name:");
        nameField = new JTextField();

        JLabel logoLabel = new JLabel("Logo:");
        logoField = new JTextField();

        panel.add(nameLabel);
        panel.add(nameField);
        panel.add(logoLabel);
        panel.add(logoField);

        return panel;
    }

    private void addTeam() {
        String teamName = nameField.getText();
        String logo = logoField.getText();

        if (teamName.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Team name cannot be empty.");
            return;
        }

        Team team = new Team(teamName, logo);

        try {
            if (databaseManager.insertTeam(team)) {
                JOptionPane.showMessageDialog(this, "Team added successfully!");
            } else {
                JOptionPane.showMessageDialog(this, "Failed to add team!");
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Failed to add team: " + ex.getMessage());
        }

        dispose();
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == addButton) {
            addTeam();
        } else if (e.getSource() == cancelButton) {
            dispose();
        }
    }
}
