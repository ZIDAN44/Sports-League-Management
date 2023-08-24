package GUI.UpdateTableGUI;

import DataBase.DatabaseManager;
import Entity.Coach;
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

public class CoachGUI extends JDialog implements ActionListener {
    private JTable coachTable;
    private JTextField nameField;
    private JTextField positionField;
    private JTextField salaryField;
    private JButton updateButton, searchButton, refreshButton, cancelButton;
    private DefaultTableModel tableModel;
    private DatabaseManager databaseManager;

    public CoachGUI(JFrame parentFrame, DatabaseManager databaseManager) {
        super(parentFrame, "Update Coach", true);
        this.databaseManager = databaseManager;
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        LookAndFeelUtil.setSubstanceNebulaLookAndFeel();

        setBounds(500, 500, 930, 800);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());

        createCoachTable(mainPanel);
        createCoachPanel(mainPanel);
        createButtonPanel(mainPanel);

        add(mainPanel);

        setLocationRelativeTo(parentFrame);
    }

    private void createCoachTable(JPanel mainPanel) {
        Vector<String> columnNames = new Vector<>();
        columnNames.add("Coach ID");
        columnNames.add("Name");
        columnNames.add("Position");
        columnNames.add("Salary");

        tableModel = new DefaultTableModel(columnNames, 0) {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        coachTable = new JTable(tableModel);
        coachTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        coachTable.getSelectionModel().addListSelectionListener(e -> populateCoachInformation());

        coachTable.setRowHeight(35);
        coachTable.setFont(new Font("Arial", Font.PLAIN, 18));

        coachTable.getColumnModel().getColumn(0).setPreferredWidth(100);
        coachTable.getColumnModel().getColumn(1).setPreferredWidth(150);
        coachTable.getColumnModel().getColumn(2).setPreferredWidth(150);
        coachTable.getColumnModel().getColumn(3).setPreferredWidth(150);

        JScrollPane scrollPane = new JScrollPane(coachTable);
        mainPanel.add(scrollPane, BorderLayout.NORTH);

        refreshTable();
    }

    private void createCoachPanel(JPanel mainPanel) {
        JPanel panel = new JPanel();
        panel.setBorder(BorderFactory.createTitledBorder("Coach Information"));
        panel.setLayout(null);

        JLabel nameLabel = new JLabel("Name:");
        nameLabel.setBounds(10, 30, 100, 30);
        panel.add(nameLabel);

        nameField = new JTextField();
        nameField.setBounds(170, 30, 200, 30);
        panel.add(nameField);

        JLabel positionLabel = new JLabel("Position:");
        positionLabel.setBounds(10, 70, 100, 30);
        panel.add(positionLabel);

        positionField = new JTextField();
        positionField.setBounds(170, 70, 200, 30);
        panel.add(positionField);

        JLabel salaryLabel = new JLabel("Salary:");
        salaryLabel.setBounds(10, 110, 100, 30);
        panel.add(salaryLabel);

        salaryField = new JTextField();
        salaryField.setBounds(170, 110, 200, 30);
        panel.add(salaryField);

        panel.setBounds(10, 320, 400, 180);
        mainPanel.add(panel);
    }

    private void createButtonPanel(JPanel mainPanel) {
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBounds(10, 520, 400, 60);

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
        List<Coach> allCoaches;
        try {
            allCoaches = databaseManager.getAllCoaches();
            for (Coach coach : allCoaches) {
                Vector<Object> rowData = new Vector<>();
                rowData.add(coach.getCoachID());
                rowData.add(coach.getName());
                rowData.add(coach.getPosition());
                rowData.add(coach.getSalary());
                tableModel.addRow(rowData);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Failed to fetch coaches from the database: " + ex.getMessage());
        }
    }

    private void populateCoachInformation() {
        int selectedRow = coachTable.getSelectedRow();
        if (selectedRow != -1) {
            String name = (String) tableModel.getValueAt(selectedRow, 1);
            String position = (String) tableModel.getValueAt(selectedRow, 2);
            double salary = (double) tableModel.getValueAt(selectedRow, 3);

            nameField.setText(name);
            positionField.setText(position);
            salaryField.setText(String.valueOf(salary));
        }
    }

    private void updateCoachInfo() {
        int selectedRow = coachTable.getSelectedRow();
        if (selectedRow != -1) {
            int coachID = (int) tableModel.getValueAt(selectedRow, 0);
            String name = nameField.getText();
            String position = positionField.getText();
            double salary = Double.parseDouble(salaryField.getText());

            Coach updatedCoach = new Coach(coachID, name, position, salary);
            try {
                if (databaseManager.updateCoach(updatedCoach)) {
                    JOptionPane.showMessageDialog(this, "Coach updated successfully!");
                    refreshTable();
                    nameField.setText("");
                    positionField.setText("");
                    salaryField.setText("");
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to update coach!");
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Failed to update coach: " + ex.getMessage());
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select a valid coach.");
        }
    }

    private void searchCoach() {
        String searchQuery = JOptionPane.showInputDialog(this, "Enter Coach Name or ID to Search:");

        if (searchQuery != null && !searchQuery.isEmpty()) {
            List<Coach> foundCoaches;
            try {
                if (searchQuery.matches("\\d+")) {
                    int coachId = Integer.parseInt(searchQuery);
                    Coach foundCoach = databaseManager.getCoachById(coachId);
                    foundCoaches = new ArrayList<>();
                    if (foundCoach != null) {
                        foundCoaches.add(foundCoach);
                    }
                } else {
                    foundCoaches = databaseManager.getCoachesByName(searchQuery);
                }
            } catch (SQLException ex) {
                foundCoaches = new ArrayList<>();
                JOptionPane.showMessageDialog(this, "Failed to search coaches: " + ex.getMessage());
            }

            tableModel.setRowCount(0);
            for (Coach coach : foundCoaches) {
                Vector<Object> rowData = new Vector<>();
                rowData.add(coach.getCoachID());
                rowData.add(coach.getName());
                rowData.add(coach.getPosition());
                rowData.add(coach.getSalary());
                tableModel.addRow(rowData);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please enter a valid search query.");
        }
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == updateButton) {
            updateCoachInfo();
        } else if (e.getSource() == searchButton) {
            searchCoach();
        } else if (e.getSource() == refreshButton) {
            refreshTable();
            nameField.setText("");
            positionField.setText("");
            salaryField.setText("");
        } else if (e.getSource() == cancelButton) {
            dispose();
        }
    }
}
