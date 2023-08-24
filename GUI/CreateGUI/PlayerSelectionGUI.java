package GUI.CreateGUI;

import DataBase.DatabaseManager;
import Entity.Player;
import Entity.Team;
import GUI.Common.LookAndFeelUtil;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.List;
import java.util.Vector;

public class PlayerSelectionGUI extends JDialog implements ActionListener {
    private JTable teamTable;
    private JTable playerTable;
    private JButton addButton, cancelButton;
    private DatabaseManager databaseManager;

    public PlayerSelectionGUI(JFrame parentFrame, DatabaseManager databaseManager) {
        super(parentFrame, "Add Player to Team", true);
        this.databaseManager = databaseManager;
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        LookAndFeelUtil.setSubstanceNebulaLookAndFeel();

        setSize(1032, 750);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(null);

        createTeamTable(mainPanel);
        createPlayerTable(mainPanel);
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
        teamTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        teamTable.setRowHeight(30);
        teamTable.setFont(new Font("Arial", Font.PLAIN, 16));

        JScrollPane scrollPane = new JScrollPane(teamTable);
        scrollPane.setBounds(10, 10, 1000, 260);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Teams"));
        mainPanel.add(scrollPane);
    }

    private void createPlayerTable(JPanel mainPanel) {
        List<Player> players;
        players = databaseManager.getAllPlayers();

        Vector<String> playerColumnNames = new Vector<>();
        playerColumnNames.add("Player ID");
        playerColumnNames.add("Player Name");

        Vector<Vector<Object>> playerData = new Vector<>();
        for (Player player : players) {
            Vector<Object> rowData = new Vector<>();
            rowData.add(player.getPlayerID());
            rowData.add(player.getName());
            playerData.add(rowData);
        }

        DefaultTableModel playerTableModel = new DefaultTableModel(playerData, playerColumnNames) {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        playerTable = new JTable(playerTableModel);
        playerTable.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

        playerTable.setRowHeight(30);
        playerTable.setFont(new Font("Arial", Font.PLAIN, 16));

        JScrollPane scrollPane = new JScrollPane(playerTable);
        scrollPane.setBounds(10, 300, 1000, 260);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Leagues"));
        mainPanel.add(scrollPane);
    }

    private void createButtonPanel(JPanel mainPanel) {
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER));

        addButton = new JButton("Add");
        addButton.addActionListener(this);
        buttonPanel.add(addButton);

        cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(this);
        buttonPanel.add(cancelButton);

        buttonPanel.setBounds(10, 580, 1000, 40);
        mainPanel.add(buttonPanel);
    }

    private void addPlayer() {
        int selectedTeamRow = teamTable.getSelectedRow();
        int selectedPlayerRow = playerTable.getSelectedRow();

        if (selectedTeamRow != -1 && selectedPlayerRow != -1) {
            String selectedTeamID = teamTable.getValueAt(selectedTeamRow, 0).toString();
            String selectedPlayerID = playerTable.getValueAt(selectedPlayerRow, 0).toString();

            try {
                int teamID = Integer.parseInt(selectedTeamID);
                int playerID = Integer.parseInt(selectedPlayerID);

                if (databaseManager.insertAssociates(teamID, playerID)) {
                    JOptionPane.showMessageDialog(this, "Player added to the team successfully!");
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to add player to the team!");
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Invalid team or player ID.");
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Failed to add player to the team: " + ex.getMessage());
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select a valid team and player.");
        }

        dispose();
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == addButton) {
            addPlayer();
        } else if (e.getSource() == cancelButton) {
            dispose();
        }
    }
}