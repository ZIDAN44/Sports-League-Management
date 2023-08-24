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

public class CoachGUI extends JDialog {
    private DatabaseManager databaseManager;
    private JTable coachTable;
    private DefaultTableModel coachTableModel;
    private JTextField searchField;
    private JButton searchButton;

    public CoachGUI(JFrame parentFrame, DatabaseManager databaseManager) {
        super(parentFrame, "Coach Information", true);
        this.databaseManager = databaseManager;
        LookAndFeelUtil.setSubstanceNebulaLookAndFeel();
        createGUI();
    }

    private void createGUI() {
        int width = 800;
        int height = 600;

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(null);

        setupUIComponents(mainPanel);

        initializeCoachTable(mainPanel);

        getContentPane().add(mainPanel);
        setSize(width, height);
        setLocationRelativeTo(null);
    }

    private void setupUIComponents(JPanel mainPanel) {
        JLabel titleLabel = new JLabel(
                "<html><h1 style='color: #007bff; font-family: Arial, sans-serif;'>"
                        + "Coach Information</h1></html>");
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

    private void initializeCoachTable(JPanel mainPanel) {
        int width = 800;
        int height = 600;

        String[] columnNames = { "Coach ID", "Name", "Salary", "Position" };
        coachTableModel = new DefaultTableModel(columnNames, 0);
        coachTable = new JTable(coachTableModel);

        coachTable.setDefaultRenderer(Object.class, new CustomTableCellRenderer());
        coachTable.setDefaultEditor(Object.class, null);

        coachTable.setRowHeight(30);

        JScrollPane scrollPane = new JScrollPane(coachTable);
        scrollPane.setBounds(10, 80, width - 30, height - 155);
        mainPanel.add(scrollPane);

        TableColumnModel columnModel = coachTable.getColumnModel();
        columnModel.getColumn(0).setPreferredWidth(70);
        columnModel.getColumn(1).setPreferredWidth(200);
        columnModel.getColumn(2).setPreferredWidth(100);
        columnModel.getColumn(3).setPreferredWidth(100);

        updateCoachTable(databaseManager.getAllCoaches());
    }

    private void updateCoachTable(List<Coach> coaches) {
        coachTableModel.setRowCount(0);

        for (Coach coach : coaches) {
            Object[] rowData = { coach.getCoachID(), coach.getName(), coach.getSalary(), coach.getPosition() };
            coachTableModel.addRow(rowData);
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

        List<Coach> allCoaches = databaseManager.getAllCoaches();
        List<Coach> filteredCoaches = new ArrayList<>();

        for (Coach coach : allCoaches) {
            if (String.valueOf(coach.getCoachID()).contains(searchTerm) ||
                    coach.getName().toLowerCase().contains(searchTerm)) {
                filteredCoaches.add(coach);
            }
        }

        updateCoachTable(filteredCoaches);
    }
}
