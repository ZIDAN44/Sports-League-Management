package GUI.ShowGUI;

import DataBase.DatabaseManager;
import Entity.Coach;
import GUI.Common.LookAndFeelUtil;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class HireGUI extends JDialog {
    private DatabaseManager databaseManager;

    private JTextField searchField;
    private JButton searchButton;
    private JTable hireTable;
    private DefaultTableModel hireTableModel;

    public HireGUI(JFrame parentFrame, DatabaseManager databaseManager) {
        super(parentFrame, "Coach Hiring Information", true);
        this.databaseManager = databaseManager;
        LookAndFeelUtil.setSubstanceNebulaLookAndFeel();
        createGUI();
    }

    private void createGUI() {
        int width = 810;
        int height = 600;

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(null);

        setupUIComponents(mainPanel);

        initializeHireTable(mainPanel);

        getContentPane().add(mainPanel);
        setSize(width, height);
        setLocationRelativeTo(null);
    }

    private void setupUIComponents(JPanel mainPanel) {
        JLabel titleLabel = new JLabel(
                "<html><h1 style='color: #007bff; font-family: Arial, sans-serif;'>"
                        + "Coach Hiring Information</h1></html>");
        titleLabel.setBounds(10, 10, 1080, 50);
        mainPanel.add(titleLabel);

        JPanel searchPanel = new JPanel(null);
        searchPanel.setBounds(480, 25, 290, 40);

        searchField = new JTextField();
        searchField.setBounds(0, 0, 200, 30);
        searchPanel.add(searchField);

        searchButton = new JButton("Search");
        searchButton.setBounds(210, 0, 80, 30);
        searchButton.addActionListener(e -> performSearch());
        searchPanel.add(searchButton);

        mainPanel.add(searchPanel);
    }

    private void initializeHireTable(JPanel mainPanel) {
        int width = 810;
        int height = 600;

        String[] columnNames = { "Coach ID", "Name", "Salary", "Position", "Hire Date" };
        hireTableModel = new DefaultTableModel(columnNames, 0);
        hireTable = new JTable(hireTableModel);

        hireTable.setDefaultRenderer(Object.class, new CustomTableCellRenderer());
        hireTable.setDefaultEditor(Object.class, null);

        hireTable.setRowHeight(30);

        JScrollPane scrollPane = new JScrollPane(hireTable);
        scrollPane.setBounds(10, 80, width, height);
        mainPanel.add(scrollPane);

        TableColumnModel columnModel = hireTable.getColumnModel();
        columnModel.getColumn(0).setPreferredWidth(70);
        columnModel.getColumn(1).setPreferredWidth(180);
        columnModel.getColumn(2).setPreferredWidth(100);
        columnModel.getColumn(3).setPreferredWidth(100);
        columnModel.getColumn(4).setPreferredWidth(90);

        updateHireTable(databaseManager.getAllHires());
    }

    private void updateHireTable(List<Coach> coaches) {
        hireTableModel.setRowCount(0);

        for (Coach coach : coaches) {
            Object[] rowData = { coach.getCoachID(), coach.getName(), coach.getSalary(), coach.getPosition(),
                    coach.getHireDate() };
            hireTableModel.addRow(rowData);
        }
    }

    private static class CustomTableCellRenderer extends DefaultTableCellRenderer {
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
                int row, int column) {
            Component component = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            component.setFont(new Font("Arial", Font.PLAIN, 16));
            return component;
        }
    }

    private void performSearch() {
        String searchTerm = searchField.getText().toLowerCase();

        List<Coach> allCoaches = databaseManager.getAllHires();
        List<Coach> filteredCoaches = new ArrayList<>();

        for (Coach coach : allCoaches) {
            if (String.valueOf(coach.getCoachID()).contains(searchTerm) ||
                    coach.getName().toLowerCase().contains(searchTerm)) {
                filteredCoaches.add(coach);
            }
        }

        updateHireTable(filteredCoaches);
    }
}
