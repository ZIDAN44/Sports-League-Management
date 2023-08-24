package GUI.UpdateTableGUI;

import DataBase.DatabaseManager;
import Entity.MatchOfficial;
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

public class MatchOfficialGUI extends JDialog implements ActionListener {
    private JTable officialTable;
    private JTextField nameField;
    private JButton updateButton, searchButton, refreshButton, cancelButton;
    private DefaultTableModel tableModel;
    private DatabaseManager databaseManager;

    public MatchOfficialGUI(JFrame parentFrame, DatabaseManager databaseManager) {
        super(parentFrame, "Update Match Official", true);
        this.databaseManager = databaseManager;
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        LookAndFeelUtil.setSubstanceNebulaLookAndFeel();

        setBounds(500, 500, 930, 800);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());

        createOfficialTable(mainPanel);
        createOfficialPanel(mainPanel);
        createButtonPanel(mainPanel);

        add(mainPanel);

        setLocationRelativeTo(parentFrame);
    }

    private void createOfficialTable(JPanel mainPanel) {
        Vector<String> columnNames = new Vector<>();
        columnNames.add("Official ID");
        columnNames.add("Name");

        tableModel = new DefaultTableModel(columnNames, 0) {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        officialTable = new JTable(tableModel);
        officialTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        officialTable.getSelectionModel().addListSelectionListener(e -> populateOfficialInformation());

        officialTable.setRowHeight(35);
        officialTable.setFont(new Font("Arial", Font.PLAIN, 18));

        officialTable.getColumnModel().getColumn(0).setPreferredWidth(100);
        officialTable.getColumnModel().getColumn(1).setPreferredWidth(200);

        JScrollPane scrollPane = new JScrollPane(officialTable);
        mainPanel.add(scrollPane, BorderLayout.NORTH);

        refreshTable();
    }

    private void createOfficialPanel(JPanel mainPanel) {
        JPanel panel = new JPanel();
        panel.setBorder(BorderFactory.createTitledBorder("Match Official Information"));
        panel.setLayout(null);

        JLabel nameLabel = new JLabel("Name:");
        nameLabel.setBounds(10, 30, 100, 30);
        panel.add(nameLabel);

        nameField = new JTextField();
        nameField.setBounds(170, 30, 200, 30);
        panel.add(nameField);

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
        List<MatchOfficial> allOfficials;
        try {
            allOfficials = databaseManager.getAllMatchOfficials();
            for (MatchOfficial official : allOfficials) {
                Vector<Object> rowData = new Vector<>();
                rowData.add(official.getOfficialID());
                rowData.add(official.getName());
                tableModel.addRow(rowData);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Failed to fetch match officials from the database: " + ex.getMessage());
        }
    }

    private void populateOfficialInformation() {
        int selectedRow = officialTable.getSelectedRow();
        if (selectedRow != -1) {
            String name = (String) tableModel.getValueAt(selectedRow, 1);
            nameField.setText(name);
        }
    }

    private void updateOfficialInfo() {
        int selectedRow = officialTable.getSelectedRow();
        if (selectedRow != -1) {
            int officialID = (int) tableModel.getValueAt(selectedRow, 0);
            String name = nameField.getText();

            MatchOfficial updatedOfficial = new MatchOfficial(officialID, name);
            try {
                if (databaseManager.updateMatchOfficial(updatedOfficial)) {
                    JOptionPane.showMessageDialog(this, "Match official updated successfully!");
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to update match official!");
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Failed to update match official: " + ex.getMessage());
            }
            refreshTable();
            clearFields();
        } else {
            JOptionPane.showMessageDialog(this, "Please select a valid match official.");
        }
    }

    private void searchOfficial() {
        String searchQuery = JOptionPane.showInputDialog(this, "Enter Match Official Name or ID to Search:");
        if (searchQuery != null && !searchQuery.isEmpty()) {
            List<MatchOfficial> foundOfficials;
            try {
                if (searchQuery.matches("\\d+")) {
                    int officialId = Integer.parseInt(searchQuery);
                    MatchOfficial foundOfficial = databaseManager.getMatchOfficialById(officialId);
                    foundOfficials = new ArrayList<>();
                    if (foundOfficial != null) {
                        foundOfficials.add(foundOfficial);
                    }
                } else {
                    foundOfficials = databaseManager.getMatchOfficialsByName(searchQuery);
                }
                tableModel.setRowCount(0);
                for (MatchOfficial official : foundOfficials) {
                    Vector<Object> rowData = new Vector<>();
                    rowData.add(official.getOfficialID());
                    rowData.add(official.getName());
                    tableModel.addRow(rowData);
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Failed to search match officials: " + ex.getMessage());
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please enter a valid search query.");
        }
    }

    private void clearFields() {
        nameField.setText("");
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == refreshButton) {
            refreshTable();
            clearFields();
        } else if (e.getSource() == updateButton) {
            updateOfficialInfo();
        } else if (e.getSource() == searchButton) {
            searchOfficial();
        } else if (e.getSource() == cancelButton) {
            dispose();
        }
    }
}
