package GUI.CreateGUI;

import DataBase.DatabaseManager;
import Entity.League;
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

public class AssembleGUI extends JDialog implements ActionListener {
    private JTable teamTable;
    private JTable leagueTable;
    private JButton assembleButton, cancelButton;
    private DatabaseManager databaseManager;

    public AssembleGUI(JFrame parentFrame, DatabaseManager databaseManager) {
        super(parentFrame, "Assemble Team to League", true);
        this.databaseManager = databaseManager;
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        LookAndFeelUtil.setSubstanceNebulaLookAndFeel();

        setSize(1032, 750);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(null);

        createTeamTable(mainPanel);
        createLeagueTable(mainPanel);
        createButtonPanel(mainPanel);

        add(mainPanel);

        setLocationRelativeTo(parentFrame);
    }

    private void createTeamTable(JPanel mainPanel) {
        List<Team> teams;
        teams = databaseManager.getAllTeams();

        Vector<String> teamColumnNames = new Vector<>();
        teamColumnNames.add("Team ID");
        teamColumnNames.add("Team Name");

        Vector<Vector<Object>> teamData = new Vector<>();
        for (Team team : teams) {
            Vector<Object> rowData = new Vector<>();
            rowData.add(team.getTeamID());
            rowData.add(team.getTeamName());
            teamData.add(rowData);
        }

        DefaultTableModel teamTableModel = new DefaultTableModel(teamData, teamColumnNames) {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        teamTable = new JTable(teamTableModel);
        teamTable.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

        teamTable.setRowHeight(30);
        teamTable.setFont(new Font("Arial", Font.PLAIN, 16));

        JScrollPane scrollPane = new JScrollPane(teamTable);
        scrollPane.setBounds(10, 10, 1000, 260);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Teams"));
        mainPanel.add(scrollPane);
    }

    private void createLeagueTable(JPanel mainPanel) {
        List<League> leagues;
        try {
            leagues = databaseManager.getAllLeagues();
        } catch (SQLException ex) {
            leagues = new ArrayList<>();
            JOptionPane.showMessageDialog(this, "Failed to fetch leagues from the database: " + ex.getMessage());
        }

        Vector<String> leagueColumnNames = new Vector<>();
        leagueColumnNames.add("League ID");
        leagueColumnNames.add("League Name");

        Vector<Vector<Object>> leagueData = new Vector<>();
        for (League league : leagues) {
            Vector<Object> rowData = new Vector<>();
            rowData.add(league.getLeagueID());
            rowData.add(league.getLeagueName());
            leagueData.add(rowData);
        }

        DefaultTableModel leagueTableModel = new DefaultTableModel(leagueData, leagueColumnNames) {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        leagueTable = new JTable(leagueTableModel);
        leagueTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        leagueTable.setRowHeight(30);
        leagueTable.setFont(new Font("Arial", Font.PLAIN, 16));

        JScrollPane scrollPane = new JScrollPane(leagueTable);
        scrollPane.setBounds(10, 300, 1000, 260);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Leagues"));
        mainPanel.add(scrollPane);
    }

    private void createButtonPanel(JPanel mainPanel) {
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER));

        assembleButton = new JButton("Assemble");
        assembleButton.addActionListener(this);
        buttonPanel.add(assembleButton);

        cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(this);
        buttonPanel.add(cancelButton);

        buttonPanel.setBounds(10, 580, 1000, 40);
        mainPanel.add(buttonPanel);
    }

    private void assembleTeamToLeague() {
        int selectedLeagueRow = leagueTable.getSelectedRow();
        int[] selectedTeamRows = teamTable.getSelectedRows();

        if (selectedLeagueRow != -1 && selectedTeamRows.length > 0) {
            try {
                int leagueID = (int) leagueTable.getValueAt(selectedLeagueRow, 0);
                for (int selectedTeamRow : selectedTeamRows) {
                    int teamID = (int) teamTable.getValueAt(selectedTeamRow, 0);
                    if (!databaseManager.insertAssemble(leagueID, teamID)) {
                        JOptionPane.showMessageDialog(this, "Failed to assemble team to league!");
                        return;
                    }
                }
                JOptionPane.showMessageDialog(this, "Teams assembled to league successfully!");
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Failed to assemble teams to league: " + ex.getMessage());
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select a valid league and at least one team.");
        }

        dispose();
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == assembleButton) {
            assembleTeamToLeague();
        } else if (e.getSource() == cancelButton) {
            dispose();
        }
    }
}
