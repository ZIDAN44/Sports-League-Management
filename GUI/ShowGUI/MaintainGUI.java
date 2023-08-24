package GUI.ShowGUI;

import DataBase.DatabaseManager;
import Entity.MatchOfficial;
import GUI.Common.LookAndFeelUtil;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MaintainGUI extends JDialog {
    private DatabaseManager databaseManager;
    private JTextField searchField;
    private JButton searchButton;
    private JTable maintainTable;
    private DefaultTableModel maintainTableModel;

    public MaintainGUI(JFrame parentFrame, DatabaseManager databaseManager) {
        super(parentFrame, "Maintenance Information", true);
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

        initializeMaintainTable(mainPanel);

        getContentPane().add(mainPanel);
        setSize(width, height);
        setLocationRelativeTo(null);
    }

    private void setupUIComponents(JPanel mainPanel) {
        JLabel titleLabel = new JLabel(
                "<html><h1 style='color: #007bff; font-family: Arial, sans-serif;'>"
                        + "Maintenance Information</h1></html>");
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

    private void initializeMaintainTable(JPanel mainPanel) {
        int width = 800;
        int height = 600;

        String[] columnNames = { "Official ID", "Match ID", "Maintenance Rules" };
        maintainTableModel = new DefaultTableModel(columnNames, 0);
        maintainTable = new JTable(maintainTableModel);

        maintainTable.setDefaultRenderer(Object.class, new CustomTableCellRenderer());
        maintainTable.setDefaultEditor(Object.class, null);

        maintainTable.setRowHeight(30);

        JScrollPane scrollPane = new JScrollPane(maintainTable);
        scrollPane.setBounds(10, 80, width - 30, height - 155);
        mainPanel.add(scrollPane);

        TableColumnModel columnModel = maintainTable.getColumnModel();
        columnModel.getColumn(0).setPreferredWidth(100);
        columnModel.getColumn(1).setPreferredWidth(100);
        columnModel.getColumn(2).setPreferredWidth(400);

        try {
            updateMaintainTable(databaseManager.getAllMaintenance());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void updateMaintainTable(List<MatchOfficial> maintenanceList) {
        maintainTableModel.setRowCount(0);

        for (MatchOfficial maintain : maintenanceList) {
            Object[] rowData = { maintain.getOfficialID(), maintain.getmatchID(), maintain.getOrules() };
            maintainTableModel.addRow(rowData);
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

        List<MatchOfficial> allMaintenance;
        try {
            allMaintenance = databaseManager.getAllMaintenance();
        } catch (SQLException e) {
            allMaintenance = new ArrayList<>();
            e.printStackTrace();
        }
        List<MatchOfficial> filteredMaintenance = new ArrayList<>();

        for (MatchOfficial maintain : allMaintenance) {
            if (String.valueOf(maintain.getmatchID()).contains(searchTerm) ||
                    String.valueOf(maintain.getOfficialID()).contains(searchTerm) ||
                    maintain.getOrules().toLowerCase().contains(searchTerm)) {
                filteredMaintenance.add(maintain);
            }
        }

        updateMaintainTable(filteredMaintenance);
    }
}
