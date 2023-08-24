package GUI.CreateGUI;

import DataBase.DatabaseManager;
import Entity.CEO;
import Entity.Match;
import Entity.MatchOfficial;
import Entity.Team;
import GUI.Common.LookAndFeelUtil;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Vector;

public class MatchGUI extends JDialog implements ActionListener {
    private JTextField dateField;
    private JTextField timeField;
    private JTextField venueField;
    private JTable teamTable1;
    private JTable teamTable2;
    private JTable matchOfficialTable;
    private JButton addButton, cancelButton;
    private DatabaseManager databaseManager;
    private CEO loggedInCEO;
    private List<MatchOfficial> matchOfficials;
    private List<Team> teams;

    public MatchGUI(JFrame parentFrame, DatabaseManager databaseManager, CEO loggedInCEO) {
        super(parentFrame, "Create Match", true);
        this.databaseManager = databaseManager;
        this.loggedInCEO = loggedInCEO;
        this.matchOfficials = databaseManager.getAllMatchOfficials();
        this.teams = databaseManager.getAllTeams();
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        LookAndFeelUtil.setSubstanceNebulaLookAndFeel();

        setSize(960, 750);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(null);

        createDateField(mainPanel);
        createTimeField(mainPanel);
        createVenueField(mainPanel);
        createTeamTable1(mainPanel);
        createTeamTable2(mainPanel);
        createMatchOfficialTable(mainPanel);
        createButtonPanel(mainPanel);

        add(mainPanel);

        setLocationRelativeTo(parentFrame);
    }

    private void createDateField(JPanel mainPanel) {
        JLabel dateLabel = new JLabel("Date:");
        dateField = new JTextField();
        dateLabel.setBounds(10, 440, 100, 30);
        dateField.setBounds(120, 440, 150, 30);
        mainPanel.add(dateLabel);
        mainPanel.add(dateField);
    }

    private void createTimeField(JPanel mainPanel) {
        JLabel timeLabel = new JLabel("Time:");
        timeField = new JTextField();
        timeLabel.setBounds(10, 480, 100, 30);
        timeField.setBounds(120, 480, 150, 30);
        mainPanel.add(timeLabel);
        mainPanel.add(timeField);
    }

    private void createVenueField(JPanel mainPanel) {
        JLabel venueLabel = new JLabel("Venue:");
        venueField = new JTextField();
        venueLabel.setBounds(10, 520, 100, 30);
        venueField.setBounds(120, 520, 150, 30);
        mainPanel.add(venueLabel);
        mainPanel.add(venueField);
    }

    private void createTeamTable1(JPanel mainPanel) {
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
        teamTable1 = new JTable(teamTableModel);
        teamTable1.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        teamTable1.setRowHeight(30);
        teamTable1.setFont(new Font("Arial", Font.PLAIN, 16));

        JScrollPane scrollPane = new JScrollPane(teamTable1);
        scrollPane.setBounds(10, 20, 455, 395);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Team 1"));
        mainPanel.add(scrollPane);
    }

    private void createTeamTable2(JPanel mainPanel) {
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
        teamTable2 = new JTable(teamTableModel);
        teamTable2.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        teamTable2.setRowHeight(30);
        teamTable2.setFont(new Font("Arial", Font.PLAIN, 16));

        JScrollPane scrollPane = new JScrollPane(teamTable2);
        scrollPane.setBounds(480, 20, 455, 395);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Team 2"));
        mainPanel.add(scrollPane);
    }

    private void createMatchOfficialTable(JPanel mainPanel) {
        Vector<String> matchOfficialColumnNames = new Vector<>();
        matchOfficialColumnNames.add("Match Official ID");
        matchOfficialColumnNames.add("Match Official Name");

        Vector<Vector<Object>> matchOfficialData = new Vector<>();
        for (MatchOfficial matchOfficial : matchOfficials) {
            Vector<Object> rowData = new Vector<>();
            rowData.add(matchOfficial.getOfficialID());
            rowData.add(matchOfficial.getName());
            matchOfficialData.add(rowData);
        }

        DefaultTableModel matchOfficialTableModel = new DefaultTableModel(matchOfficialData, matchOfficialColumnNames) {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        matchOfficialTable = new JTable(matchOfficialTableModel);
        matchOfficialTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        matchOfficialTable.setRowHeight(30);
        matchOfficialTable.setFont(new Font("Arial", Font.PLAIN, 16));

        JScrollPane scrollPane = new JScrollPane(matchOfficialTable);
        scrollPane.setBounds(340, 425, 600, 130);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Match Officials"));
        mainPanel.add(scrollPane);
    }

    private void createButtonPanel(JPanel mainPanel) {
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER));

        addButton = new JButton("Add");
        addButton.addActionListener(this);
        cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(this);
        buttonPanel.add(addButton);
        buttonPanel.add(cancelButton);

        buttonPanel.setBounds(90, 570, 800, 40);
        mainPanel.add(buttonPanel);
    }

    private void addMatch() {
        LocalDate date = LocalDate.parse(dateField.getText().trim());
        String time = timeField.getText();
        String venue = venueField.getText();

        int selectedTeam1Index = teamTable1.getSelectedRow();
        int selectedTeam2Index = teamTable2.getSelectedRow();

        if (selectedTeam1Index >= 0 && selectedTeam2Index >= 0) {
            int selectedTeam1ID = (int) teamTable1.getValueAt(selectedTeam1Index, 0);
            int selectedTeam2ID = (int) teamTable2.getValueAt(selectedTeam2Index, 0);

            Team selectedTeam1 = teams.stream()
                    .filter(team -> team.getTeamID() == selectedTeam1ID)
                    .findFirst()
                    .orElse(null);

            Team selectedTeam2 = teams.stream()
                    .filter(team -> team.getTeamID() == selectedTeam2ID)
                    .findFirst()
                    .orElse(null);

            MatchOfficial selectedMatchOfficial = matchOfficials.get(matchOfficialTable.getSelectedRow());

            Match match = new Match(date, time, venue);

            try {
                if (databaseManager.insertMatch(match)) {
                    int currentMatchId = databaseManager.getLatestMatchId();

                    // Insert team 1 and team 2 into participate table
                    databaseManager.insertParticipate(selectedTeam1.getTeamID(), selectedTeam2.getTeamID(),
                            currentMatchId,
                            selectedTeam1.getTeamName() + " VS " + selectedTeam2.getTeamName());

                    // Insert the association into maintain table
                    databaseManager.insertMaintain(selectedMatchOfficial, currentMatchId, "Official rules");

                    // Insert the schedule information
                    match.setMatchID(currentMatchId);
                    if (databaseManager.insertSchedule(currentMatchId, match,
                            selectedTeam1.getTeamName() + " VS " + selectedTeam2.getTeamName(), loggedInCEO.getID())) {
                        JOptionPane.showMessageDialog(this, "Match added successfully!");
                    } else {
                        JOptionPane.showMessageDialog(this, "Failed to add the match!");
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to add the match!");
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Failed to add the match: " + ex.getMessage());
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select teams from both tables.");
        }

        dispose();
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == addButton) {
            addMatch();
        } else if (e.getSource() == cancelButton) {
            dispose();
        }
    }
}
