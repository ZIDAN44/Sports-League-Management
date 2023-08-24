package GUI.UpdateTableGUI;

import DataBase.DatabaseManager;
import Entity.CEO;
import Entity.League;
import GUI.Common.LookAndFeelUtil;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class LeagueGUI extends JDialog implements ActionListener {
    private JTable leagueTable;
    private JTextField leagueNameField;
    private JTextField seasonField;
    private JTextField startDateField;
    private JTextField endDateField;
    private JTextField rulesField;
    private JButton updateButton, searchButton, refreshButton, cancelButton;
    private DefaultTableModel tableModel;
    private DatabaseManager databaseManager;
    private CEO loggedInCEO;

    public LeagueGUI(JFrame parentFrame, DatabaseManager databaseManager, CEO loggedInCEO) {
        super(parentFrame, "Update League", true);
        this.databaseManager = databaseManager;
        this.loggedInCEO = loggedInCEO;
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        LookAndFeelUtil.setSubstanceNebulaLookAndFeel();

        setBounds(500, 500, 930, 800);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());

        createLeagueTable(mainPanel);
        createLeaguePanel(mainPanel);
        createButtonPanel(mainPanel);

        add(mainPanel);

        setLocationRelativeTo(parentFrame);
    }

    private void createLeagueTable(JPanel mainPanel) {
        Vector<String> columnNames = new Vector<>();
        columnNames.add("League ID");
        columnNames.add("League Name");
        columnNames.add("Season");
        columnNames.add("Start Date");
        columnNames.add("End Date");

        tableModel = new DefaultTableModel(columnNames, 0) {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        leagueTable = new JTable(tableModel);
        leagueTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        leagueTable.getSelectionModel().addListSelectionListener(e -> populateLeagueInformation());

        leagueTable.setRowHeight(35);
        leagueTable.setFont(new Font("Arial", Font.PLAIN, 18));

        leagueTable.getColumnModel().getColumn(0).setPreferredWidth(80);
        leagueTable.getColumnModel().getColumn(1).setPreferredWidth(150);
        leagueTable.getColumnModel().getColumn(2).setPreferredWidth(100);
        leagueTable.getColumnModel().getColumn(3).setPreferredWidth(120);
        leagueTable.getColumnModel().getColumn(4).setPreferredWidth(120);

        JScrollPane scrollPane = new JScrollPane(leagueTable);
        mainPanel.add(scrollPane, BorderLayout.NORTH);

        refreshTable();
    }

    private void createLeaguePanel(JPanel mainPanel) {
        JPanel panel = new JPanel();
        panel.setBorder(BorderFactory.createTitledBorder("League Information"));
        panel.setLayout(null);

        JLabel leagueNameLabel = new JLabel("League Name:");
        leagueNameLabel.setBounds(10, 30, 100, 30);
        panel.add(leagueNameLabel);

        leagueNameField = new JTextField();
        leagueNameField.setBounds(170, 30, 200, 30);
        panel.add(leagueNameField);

        JLabel seasonLabel = new JLabel("Season:");
        seasonLabel.setBounds(10, 70, 100, 30);
        panel.add(seasonLabel);

        seasonField = new JTextField();
        seasonField.setBounds(170, 70, 200, 30);
        panel.add(seasonField);

        JLabel startDateLabel = new JLabel("Start Date (yyyy-MM-dd):");
        startDateLabel.setBounds(10, 110, 200, 30);
        panel.add(startDateLabel);

        startDateField = new JTextField();
        startDateField.setBounds(170, 110, 200, 30);
        panel.add(startDateField);

        JLabel endDateLabel = new JLabel("End Date (yyyy-MM-dd):");
        endDateLabel.setBounds(10, 150, 200, 30);
        panel.add(endDateLabel);

        endDateField = new JTextField();
        endDateField.setBounds(170, 150, 200, 30);
        panel.add(endDateField);

        JLabel rulesLabel = new JLabel("Rules and Regulations:");
        rulesLabel.setBounds(10, 190, 150, 30);
        panel.add(rulesLabel);

        rulesField = new JTextField();
        rulesField.setBounds(170, 190, 200, 60);
        panel.add(rulesField);

        panel.setBounds(10, 320, 400, 270);
        mainPanel.add(panel);
    }

    private void createButtonPanel(JPanel mainPanel) {
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBounds(10, 600, 400, 60);

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
        List<League> allLeagues;
        try {
            allLeagues = databaseManager.getAllLeagues();
            for (League league : allLeagues) {
                Vector<Object> rowData = new Vector<>();
                rowData.add(league.getLeagueID());
                rowData.add(league.getLeagueName());
                rowData.add(league.getSeason());
                rowData.add(league.getStartDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
                rowData.add(league.getEndDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
                tableModel.addRow(rowData);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Failed to fetch leagues from the database: " + ex.getMessage());
        }
    }

    private void populateLeagueInformation() {
        int selectedRow = leagueTable.getSelectedRow();
        if (selectedRow != -1) {
            String leagueName = (String) tableModel.getValueAt(selectedRow, 1);
            String season = (String) tableModel.getValueAt(selectedRow, 2);
            String startDate = (String) tableModel.getValueAt(selectedRow, 3);
            String endDate = (String) tableModel.getValueAt(selectedRow, 4);

            leagueNameField.setText(leagueName);
            seasonField.setText(season);
            startDateField.setText(startDate);
            endDateField.setText(endDate);
            rulesField.setText("");
        }
    }

    private void updateLeagueInfo() {
        int selectedRow = leagueTable.getSelectedRow();
        if (selectedRow != -1) {
            int leagueID = (int) tableModel.getValueAt(selectedRow, 0);
            String leagueName = leagueNameField.getText();
            String season = seasonField.getText();
            LocalDate startDate = LocalDate.parse(startDateField.getText(), DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            LocalDate endDate = LocalDate.parse(endDateField.getText(), DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            String rules = rulesField.getText();

            League updatedLeague = new League(leagueID, leagueName, season, startDate, endDate, rules, loggedInCEO);
            try {
                if (databaseManager.updateLeagueAndOrganize(updatedLeague)) {
                    JOptionPane.showMessageDialog(this, "League updated successfully!");
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to update league!");
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Failed to update league: " + ex.getMessage());
            }
            refreshTable();
            clearFields();
        } else {
            JOptionPane.showMessageDialog(this, "Please select a valid league.");
        }
    }

    private void searchLeague() {
        String searchQuery = JOptionPane.showInputDialog(this, "Enter League Name or ID to Search:");
        if (searchQuery != null && !searchQuery.isEmpty()) {
            List<League> foundLeagues;
            try {
                if (searchQuery.matches("\\d+")) {
                    int leagueId = Integer.parseInt(searchQuery);
                    League foundLeague = databaseManager.getLeagueById(leagueId);
                    foundLeagues = new ArrayList<>();
                    if (foundLeague != null) {
                        foundLeagues.add(foundLeague);
                    }
                } else {
                    foundLeagues = databaseManager.getLeaguesByName(searchQuery);
                }
                tableModel.setRowCount(0);
                for (League league : foundLeagues) {
                    Vector<Object> rowData = new Vector<>();
                    rowData.add(league.getLeagueID());
                    rowData.add(league.getLeagueName());
                    rowData.add(league.getSeason());
                    rowData.add(league.getStartDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
                    rowData.add(league.getEndDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
                    tableModel.addRow(rowData);
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Failed to search leagues: " + ex.getMessage());
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please enter a valid search query.");
        }
    }

    private void clearFields() {
        leagueNameField.setText("");
        seasonField.setText("");
        startDateField.setText("");
        endDateField.setText("");
        rulesField.setText("");
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == refreshButton) {
            refreshTable();
            clearFields();
        } else if (e.getSource() == updateButton) {
            updateLeagueInfo();
        } else if (e.getSource() == searchButton) {
            searchLeague();
        } else if (e.getSource() == cancelButton) {
            dispose();
        }
    }
}
