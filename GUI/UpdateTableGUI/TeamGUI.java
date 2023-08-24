package GUI.UpdateTableGUI;

import DataBase.DatabaseManager;
import Entity.Team;
import GUI.Common.LookAndFeelUtil;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class TeamGUI extends JDialog implements ActionListener {
    private JTable teamTable;
    private JTextField teamNameField;
    private JTextField logoField;
    private JButton updateButton, searchButton, refreshButton, cancelButton;
    private DefaultTableModel tableModel;
    private DatabaseManager databaseManager;

    public TeamGUI(JFrame parentFrame, DatabaseManager databaseManager) {
        super(parentFrame, "Update Team", true);
        this.databaseManager = databaseManager;
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        LookAndFeelUtil.setSubstanceNebulaLookAndFeel();

        setBounds(500, 500, 930, 750);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());

        createTeamTable(mainPanel);
        createTeamPanel(mainPanel);
        createButtonPanel(mainPanel);

        add(mainPanel);

        setLocationRelativeTo(parentFrame);
    }

    private void createTeamTable(JPanel mainPanel) {
        Vector<String> columnNames = new Vector<>();
        columnNames.add("Team ID");
        columnNames.add("Team Name");
        columnNames.add("Logo");

        tableModel = new DefaultTableModel(columnNames, 0) {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        teamTable = new JTable(tableModel);
        teamTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        teamTable.getSelectionModel().addListSelectionListener(e -> populateTeamInformation());

        teamTable.setRowHeight(35);
        teamTable.setFont(new Font("Arial", Font.PLAIN, 18));

        teamTable.getColumnModel().getColumn(0).setPreferredWidth(120);
        teamTable.getColumnModel().getColumn(1).setPreferredWidth(300);
        teamTable.getColumnModel().getColumn(2).setPreferredWidth(450);

        JScrollPane scrollPane = new JScrollPane(teamTable);
        mainPanel.add(scrollPane, BorderLayout.NORTH);

        refreshTable();
    }

    private void createTeamPanel(JPanel mainPanel) {
        JPanel panel = new JPanel();
        panel.setBorder(BorderFactory.createTitledBorder("Team Information"));
        panel.setLayout(null);

        JLabel teamNameLabel = new JLabel("Team Name:");
        teamNameLabel.setBounds(10, 30, 100, 40);
        panel.add(teamNameLabel);

        teamNameField = new JTextField();
        teamNameField.setBounds(120, 40, 200, 30);
        panel.add(teamNameField);

        JLabel logoLabel = new JLabel("Logo:");
        logoLabel.setBounds(10, 70, 100, 40);
        panel.add(logoLabel);

        logoField = new JTextField();
        logoField.setBounds(120, 80, 200, 30);
        panel.add(logoField);

        panel.setBounds(10, 320, 300, 130);
        mainPanel.add(panel);
    }

    private void createButtonPanel(JPanel mainPanel) {
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));

        refreshButton = new JButton("Refresh");
        refreshButton.addActionListener(this);
        buttonPanel.add(refreshButton);

        updateButton = new JButton("Update");
        updateButton.addActionListener(this);
        buttonPanel.add(updateButton);

        searchButton = new JButton("Search");
        searchButton.addActionListener(this);
        buttonPanel.add(searchButton);

        cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(this);
        buttonPanel.add(cancelButton);

        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
    }

    private void refreshTable() {
        tableModel.setRowCount(0);
        List<Team> allTeams = databaseManager.getAllTeams();
        for (Team team : allTeams) {
            Vector<Object> rowData = new Vector<>();
            rowData.add(team.getTeamID());
            rowData.add(team.getTeamName());
            rowData.add(team.getLogo());
            tableModel.addRow(rowData);
        }
    }

    private void populateTeamInformation() {
        int selectedRow = teamTable.getSelectedRow();
        if (selectedRow != -1) {
            String teamName = (String) tableModel.getValueAt(selectedRow, 1);
            String logo = (String) tableModel.getValueAt(selectedRow, 2);
            teamNameField.setText(teamName);
            logoField.setText(logo);
        }
    }

    private void updateTeamInfo() {
        int selectedRow = teamTable.getSelectedRow();
        if (selectedRow != -1) {
            int teamID = (int) tableModel.getValueAt(selectedRow, 0);
            String teamName = teamNameField.getText();
            String logo = logoField.getText();

            Team updatedTeam = new Team(teamID, teamName, logo);
            try {
                if (databaseManager.updateTeam(updatedTeam)) {
                    JOptionPane.showMessageDialog(this, "Team updated successfully!");
                    refreshTable();
                    teamNameField.setText("");
                    logoField.setText("");
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to update team!");
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Failed to update team: " + ex.getMessage());
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select a valid team.");
        }
    }

    private void searchTeam() {
        String searchQuery = JOptionPane.showInputDialog(this, "Enter Team Name or ID to Search:");

        if (searchQuery != null && !searchQuery.isEmpty()) {
            List<Team> foundTeams;
            try {
                if (searchQuery.matches("\\d+")) {
                    int teamId = Integer.parseInt(searchQuery);
                    Team foundTeam = databaseManager.getTeamById(teamId);
                    foundTeams = new ArrayList<>();
                    if (foundTeam != null) {
                        foundTeams.add(foundTeam);
                    }
                } else {
                    foundTeams = databaseManager.getTeamsByName(searchQuery);
                }
            } catch (SQLException ex) {
                foundTeams = new ArrayList<>();
                JOptionPane.showMessageDialog(this, "Failed to search teams: " + ex.getMessage());
            }

            tableModel.setRowCount(0);
            for (Team team : foundTeams) {
                Vector<Object> rowData = new Vector<>();
                rowData.add(team.getTeamID());
                rowData.add(team.getTeamName());
                rowData.add(team.getLogo());
                tableModel.addRow(rowData);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please enter a valid search query.");
        }
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == updateButton) {
            updateTeamInfo();
        } else if (e.getSource() == searchButton) {
            searchTeam();
        } else if (e.getSource() == refreshButton) {
            refreshTable();
            teamNameField.setText("");
            logoField.setText("");
        } else if (e.getSource() == cancelButton) {
            dispose();
        }
    }
}
