package GUI.UpdateTableGUI;

import DataBase.DatabaseManager;
import Entity.Match;
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

public class MatchGUI extends JDialog implements ActionListener {
    private JTable matchTable;
    private JTextField dateField;
    private JTextField timeField;
    private JTextField venueField;
    private JButton updateButton, searchButton, refreshButton, cancelButton;
    private DefaultTableModel tableModel;
    private DatabaseManager databaseManager;

    public MatchGUI(JFrame parentFrame, DatabaseManager databaseManager) {
        super(parentFrame, "Update Match", true);
        this.databaseManager = databaseManager;
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        LookAndFeelUtil.setSubstanceNebulaLookAndFeel();

        setBounds(500, 500, 930, 800);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());

        createMatchTable(mainPanel);
        createMatchPanel(mainPanel);
        createButtonPanel(mainPanel);

        add(mainPanel);

        setLocationRelativeTo(parentFrame);
    }

    private void createMatchTable(JPanel mainPanel) {
        Vector<String> columnNames = new Vector<>();
        columnNames.add("Match ID");
        columnNames.add("Date");
        columnNames.add("Time");
        columnNames.add("Venue");

        tableModel = new DefaultTableModel(columnNames, 0) {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        matchTable = new JTable(tableModel);
        matchTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        matchTable.getSelectionModel().addListSelectionListener(e -> populateMatchInformation());

        matchTable.setRowHeight(35);
        matchTable.setFont(new Font("Arial", Font.PLAIN, 18));

        matchTable.getColumnModel().getColumn(0).setPreferredWidth(100);
        matchTable.getColumnModel().getColumn(1).setPreferredWidth(150);
        matchTable.getColumnModel().getColumn(2).setPreferredWidth(100);
        matchTable.getColumnModel().getColumn(3).setPreferredWidth(580);

        JScrollPane scrollPane = new JScrollPane(matchTable);
        mainPanel.add(scrollPane, BorderLayout.NORTH);

        refreshTable();
    }

    private void createMatchPanel(JPanel mainPanel) {
        JPanel panel = new JPanel();
        panel.setBorder(BorderFactory.createTitledBorder("Match Information"));
        panel.setLayout(null);

        JLabel dateLabel = new JLabel("Date (yyyy-MM-dd):");
        dateLabel.setBounds(10, 30, 130, 30);
        panel.add(dateLabel);

        dateField = new JTextField();
        dateField.setBounds(170, 30, 200, 30);
        panel.add(dateField);

        JLabel timeLabel = new JLabel("Time:");
        timeLabel.setBounds(10, 70, 100, 30);
        panel.add(timeLabel);

        timeField = new JTextField();
        timeField.setBounds(170, 70, 200, 30);
        panel.add(timeField);

        JLabel venueLabel = new JLabel("Venue:");
        venueLabel.setBounds(10, 110, 100, 30);
        panel.add(venueLabel);

        venueField = new JTextField();
        venueField.setBounds(170, 110, 200, 30);
        panel.add(venueField);

        panel.setBounds(10, 320, 400, 230);
        mainPanel.add(panel);
    }

    private void createButtonPanel(JPanel mainPanel) {
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBounds(10, 570, 400, 60);

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
        List<Match> allMatches;
        try {
            allMatches = databaseManager.getAllMatches();
            for (Match match : allMatches) {
                Vector<Object> rowData = new Vector<>();
                rowData.add(match.getMatchID());
                rowData.add(match.getDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
                rowData.add(match.getTime());
                rowData.add(match.getVenue());
                tableModel.addRow(rowData);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Failed to fetch matches from the database: " + ex.getMessage());
        }
    }

    private void populateMatchInformation() {
        int selectedRow = matchTable.getSelectedRow();
        if (selectedRow != -1) {
            // int matchID = (int) tableModel.getValueAt(selectedRow, 0);
            String date = (String) tableModel.getValueAt(selectedRow, 1);
            String time = (String) tableModel.getValueAt(selectedRow, 2);
            String venue = (String) tableModel.getValueAt(selectedRow, 3);

            dateField.setText(date);
            timeField.setText(time);
            venueField.setText(venue);
        }
    }

    private void updateMatchInfo() {
        int selectedRow = matchTable.getSelectedRow();
        if (selectedRow != -1) {
            int matchID = (int) tableModel.getValueAt(selectedRow, 0);
            LocalDate date = LocalDate.parse(dateField.getText(), DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            String time = timeField.getText();
            String venue = venueField.getText();

            Match updatedMatch = new Match(matchID, date, time, venue);
            try {
                if (databaseManager.updateMatch(updatedMatch)) {
                    JOptionPane.showMessageDialog(this, "Match updated successfully!");
                    refreshTable();
                    dateField.setText("");
                    timeField.setText("");
                    venueField.setText("");
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to update match!");
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Failed to update match: " + ex.getMessage());
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select a valid match.");
        }
    }

    private void searchMatch() {
        String searchQuery = JOptionPane.showInputDialog(this, "Enter Match Date or ID to Search:");

        if (searchQuery != null && !searchQuery.isEmpty()) {
            List<Match> foundMatches;
            try {
                if (searchQuery.matches("\\d+")) {
                    int matchId = Integer.parseInt(searchQuery);
                    Match foundMatch = databaseManager.getMatchById(matchId);
                    foundMatches = new ArrayList<>();
                    if (foundMatch != null) {
                        foundMatches.add(foundMatch);
                    }
                } else {
                    LocalDate date = LocalDate.parse(searchQuery, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                    foundMatches = databaseManager.getMatchesByDate(date);
                }
            } catch (SQLException ex) {
                foundMatches = new ArrayList<>();
                JOptionPane.showMessageDialog(this, "Failed to search matches: " + ex.getMessage());
            }

            tableModel.setRowCount(0);
            for (Match match : foundMatches) {
                Vector<Object> rowData = new Vector<>();
                rowData.add(match.getMatchID());
                rowData.add(match.getDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
                rowData.add(match.getTime());
                rowData.add(match.getVenue());
                tableModel.addRow(rowData);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please enter a valid search query.");
        }
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == updateButton) {
            updateMatchInfo();
        } else if (e.getSource() == searchButton) {
            searchMatch();
        } else if (e.getSource() == refreshButton) {
            refreshTable();
            dateField.setText("");
            timeField.setText("");
            venueField.setText("");
        } else if (e.getSource() == cancelButton) {
            dispose();
        }
    }
}
