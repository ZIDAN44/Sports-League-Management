package GUI.CreateGUI;

import DataBase.DatabaseManager;
import Entity.Coach;
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

public class AssignGUI extends JDialog implements ActionListener {
    private JTable teamTable;
    private JTable coachTable;
    private JButton assignButton, cancelButton;
    private DatabaseManager databaseManager;

    public AssignGUI(JFrame parentFrame, DatabaseManager databaseManager) {
        super(parentFrame, "Assign Coach to Team", true);
        this.databaseManager = databaseManager;
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        LookAndFeelUtil.setSubstanceNebulaLookAndFeel();

        setSize(1032, 750);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(null);

        createTeamTable(mainPanel);
        createCoachTable(mainPanel);
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

    private void createCoachTable(JPanel mainPanel) {
        List<Coach> coaches;
        coaches = databaseManager.getAllCoaches();

        Vector<String> coachColumnNames = new Vector<>();
        coachColumnNames.add("Coach ID");
        coachColumnNames.add("Coach Name");

        Vector<Vector<Object>> coachData = new Vector<>();
        for (Coach coach : coaches) {
            Vector<Object> rowData = new Vector<>();
            rowData.add(coach.getCoachID());
            rowData.add(coach.getName());
            coachData.add(rowData);
        }

        DefaultTableModel coachTableModel = new DefaultTableModel(coachData, coachColumnNames) {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        coachTable = new JTable(coachTableModel);
        coachTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        coachTable.setRowHeight(30);
        coachTable.setFont(new Font("Arial", Font.PLAIN, 16));

        coachTable.setRowHeight(30);
        coachTable.setFont(new Font("Arial", Font.PLAIN, 16));

        JScrollPane scrollPane = new JScrollPane(coachTable);
        scrollPane.setBounds(10, 300, 1000, 260);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Coach"));
        mainPanel.add(scrollPane);
    }

    private void createButtonPanel(JPanel mainPanel) {
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER));

        assignButton = new JButton("Assign");
        assignButton.addActionListener(this);
        cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(this);
        buttonPanel.add(assignButton);
        buttonPanel.add(cancelButton);

        buttonPanel.setBounds(10, 580, 1000, 40);
        mainPanel.add(buttonPanel);
    }

    private void assignCoachToTeam() {
        int selectedTeamRow = teamTable.getSelectedRow();
        int selectedCoachRow = coachTable.getSelectedRow();

        if (selectedTeamRow != -1 && selectedCoachRow != -1) {
            String selectedTeamID = teamTable.getValueAt(selectedTeamRow, 0).toString();
            String selectedCoachID = coachTable.getValueAt(selectedCoachRow, 0).toString();

            try {
                int teamID = Integer.parseInt(selectedTeamID);
                int coachID = Integer.parseInt(selectedCoachID);

                if (databaseManager.insertSupervise(coachID, teamID)) {
                    JOptionPane.showMessageDialog(this, "Coach assigned to the team successfully!");
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to assign coach to the team!");
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Invalid team or coach ID.");
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Failed to assign coach to the team: " + ex.getMessage());
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select a valid team and coach.");
        }

        dispose();
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == assignButton) {
            assignCoachToTeam();
        } else if (e.getSource() == cancelButton) {
            dispose();
        }
    }
}
