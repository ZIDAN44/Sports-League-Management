package GUI.UpdateTableGUI;

import DataBase.DatabaseManager;
import Entity.Player;
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

public class PlayerGUI extends JDialog implements ActionListener {
    private JTable playerTable;
    private JTextField playerNameField;
    private JTextField ageField;
    private JTextField positionField;
    private JTextField statisticsField;
    private JButton updateButton, searchButton, refreshButton, cancelButton;
    private DefaultTableModel tableModel;
    private DatabaseManager databaseManager;

    public PlayerGUI(JFrame parentFrame, DatabaseManager databaseManager) {
        super(parentFrame, "Update Player", true);
        this.databaseManager = databaseManager;
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        LookAndFeelUtil.setSubstanceNebulaLookAndFeel();

        setBounds(500, 500, 930, 800);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());

        createPlayerTable(mainPanel);
        createPlayerPanel(mainPanel);
        createButtonPanel(mainPanel);

        add(mainPanel);

        setLocationRelativeTo(parentFrame);
    }

    private void createPlayerTable(JPanel mainPanel) {
        Vector<String> columnNames = new Vector<>();
        columnNames.add("Player ID");
        columnNames.add("Player Name");
        columnNames.add("Age");
        columnNames.add("Position");
        columnNames.add("Statistics");

        tableModel = new DefaultTableModel(columnNames, 0) {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        playerTable = new JTable(tableModel);
        playerTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        playerTable.getSelectionModel().addListSelectionListener(e -> populatePlayerInformation());

        playerTable.setRowHeight(35);
        playerTable.setFont(new Font("Arial", Font.PLAIN, 18));

        playerTable.getColumnModel().getColumn(0).setPreferredWidth(100);
        playerTable.getColumnModel().getColumn(1).setPreferredWidth(200);
        playerTable.getColumnModel().getColumn(2).setPreferredWidth(70);
        playerTable.getColumnModel().getColumn(3).setPreferredWidth(150);
        playerTable.getColumnModel().getColumn(4).setPreferredWidth(380);

        JScrollPane scrollPane = new JScrollPane(playerTable);
        mainPanel.add(scrollPane, BorderLayout.NORTH);

        refreshTable();
    }

    private void createPlayerPanel(JPanel mainPanel) {
        JPanel panel = new JPanel();
        panel.setBorder(BorderFactory.createTitledBorder("Player Information"));
        panel.setLayout(null);

        JLabel playerNameLabel = new JLabel("Player Name:");
        playerNameLabel.setBounds(10, 30, 100, 30);
        panel.add(playerNameLabel);

        playerNameField = new JTextField();
        playerNameField.setBounds(170, 30, 200, 30);
        panel.add(playerNameField);

        JLabel ageLabel = new JLabel("Age:");
        ageLabel.setBounds(10, 70, 100, 30);
        panel.add(ageLabel);

        ageField = new JTextField();
        ageField.setBounds(170, 70, 200, 30);
        panel.add(ageField);

        JLabel positionLabel = new JLabel("Position:");
        positionLabel.setBounds(10, 110, 100, 30);
        panel.add(positionLabel);

        positionField = new JTextField();
        positionField.setBounds(170, 110, 200, 30);
        panel.add(positionField);

        JLabel statisticsLabel = new JLabel("Statistics:");
        statisticsLabel.setBounds(10, 150, 100, 30);
        panel.add(statisticsLabel);

        statisticsField = new JTextField();
        statisticsField.setBounds(170, 150, 200, 60);
        panel.add(statisticsField);

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
        List<Player> allPlayers = databaseManager.getAllPlayers();
        for (Player player : allPlayers) {
            Vector<Object> rowData = new Vector<>();
            rowData.add(player.getPlayerID());
            rowData.add(player.getName());
            rowData.add(player.getAge());
            rowData.add(player.getPosition());
            rowData.add(player.getStatistics());
            tableModel.addRow(rowData);
        }
    }

    private void populatePlayerInformation() {
        int selectedRow = playerTable.getSelectedRow();
        if (selectedRow != -1) {
            String playerName = (String) tableModel.getValueAt(selectedRow, 1);
            int age = (int) tableModel.getValueAt(selectedRow, 2);
            String position = (String) tableModel.getValueAt(selectedRow, 3);
            String statistics = (String) tableModel.getValueAt(selectedRow, 4);
            playerNameField.setText(playerName);
            ageField.setText(String.valueOf(age));
            positionField.setText(position);
            statisticsField.setText(statistics);
        }
    }

    private void updatePlayerInfo() {
        int selectedRow = playerTable.getSelectedRow();
        if (selectedRow != -1) {
            int playerID = (int) tableModel.getValueAt(selectedRow, 0);
            String playerName = playerNameField.getText();
            int age = Integer.parseInt(ageField.getText());
            String position = positionField.getText();
            String statistics = statisticsField.getText();

            Player updatedPlayer = new Player(playerID, playerName, age, position, statistics);
            try {
                if (databaseManager.updatePlayer(updatedPlayer)) {
                    JOptionPane.showMessageDialog(this, "Player updated successfully!");
                    refreshTable();
                    playerNameField.setText("");
                    ageField.setText("");
                    positionField.setText("");
                    statisticsField.setText("");
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to update player!");
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Failed to update player: " + ex.getMessage());
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select a valid player.");
        }
    }

    private void searchPlayer() {
        String searchQuery = JOptionPane.showInputDialog(this, "Enter Player Name or ID to Search:");

        if (searchQuery != null && !searchQuery.isEmpty()) {
            List<Player> foundPlayers;
            try {
                if (searchQuery.matches("\\d+")) {
                    int playerId = Integer.parseInt(searchQuery);
                    Player foundPlayer = databaseManager.getPlayerById(playerId);
                    foundPlayers = new ArrayList<>();
                    if (foundPlayer != null) {
                        foundPlayers.add(foundPlayer);
                    }
                } else {
                    foundPlayers = databaseManager.getPlayersByName(searchQuery);
                }
            } catch (SQLException ex) {
                foundPlayers = new ArrayList<>();
                JOptionPane.showMessageDialog(this, "Failed to search players: " + ex.getMessage());
            }

            tableModel.setRowCount(0);
            for (Player player : foundPlayers) {
                Vector<Object> rowData = new Vector<>();
                rowData.add(player.getPlayerID());
                rowData.add(player.getName());
                rowData.add(player.getAge());
                rowData.add(player.getPosition());
                rowData.add(player.getStatistics());
                tableModel.addRow(rowData);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please enter a valid search query.");
        }
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == updateButton) {
            updatePlayerInfo();
        } else if (e.getSource() == searchButton) {
            searchPlayer();
        } else if (e.getSource() == refreshButton) {
            refreshTable();
            playerNameField.setText("");
            ageField.setText("");
            positionField.setText("");
            statisticsField.setText("");
        } else if (e.getSource() == cancelButton) {
            dispose();
        }
    }
}
