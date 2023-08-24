package GUI.ShowGUI;

import DataBase.DatabaseManager;
import Entity.MatchOfficial;
import GUI.Common.LookAndFeelUtil;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class MatchOfficialGUI extends JDialog {
    private DatabaseManager databaseManager;
    private JTextField searchField;
    private JButton searchButton;
    private JTable matchOfficialTable;
    private DefaultTableModel matchOfficialTableModel;

    public MatchOfficialGUI(JFrame parentFrame, DatabaseManager databaseManager) {
        super(parentFrame, "Match Officials", true);
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

        initializeMatchOfficialTable(mainPanel);

        getContentPane().add(mainPanel);
        setSize(width, height);
        setLocationRelativeTo(null);
    }

    private void setupUIComponents(JPanel mainPanel) {
        JLabel titleLabel = new JLabel(
                "<html><h1 style='color: #007bff; font-family: Arial, sans-serif;'>"
                        + "Match Officials</h1></html>");
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

    private void initializeMatchOfficialTable(JPanel mainPanel) {
        int width = 800;
        int height = 600;

        String[] columnNames = { "Match Official ID", "Name" };
        matchOfficialTableModel = new DefaultTableModel(columnNames, 0);
        matchOfficialTable = new JTable(matchOfficialTableModel);

        matchOfficialTable.setDefaultRenderer(Object.class, new CustomTableCellRenderer());
        matchOfficialTable.setDefaultEditor(Object.class, null);

        matchOfficialTable.setRowHeight(30);

        JScrollPane scrollPane = new JScrollPane(matchOfficialTable);
        scrollPane.setBounds(10, 80, width - 30, height - 155);
        mainPanel.add(scrollPane);

        TableColumnModel columnModel = matchOfficialTable.getColumnModel();
        columnModel.getColumn(0).setPreferredWidth(100);
        columnModel.getColumn(1).setPreferredWidth(300);

        updateMatchOfficialTable(databaseManager.getAllMatchOfficials());
    }

    private void updateMatchOfficialTable(List<MatchOfficial> matchOfficials) {
        matchOfficialTableModel.setRowCount(0);

        for (MatchOfficial matchOfficial : matchOfficials) {
            Object[] rowData = { matchOfficial.getOfficialID(), matchOfficial.getName() };
            matchOfficialTableModel.addRow(rowData);
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

        List<MatchOfficial> allOfficials = databaseManager.getAllMatchOfficials();
        List<MatchOfficial> filteredOfficials = new ArrayList<>();

        for (MatchOfficial official : allOfficials) {
            if (String.valueOf(official.getOfficialID()).contains(searchTerm) ||
                    official.getName().toLowerCase().contains(searchTerm)) {
                filteredOfficials.add(official);
            }
        }

        updateMatchOfficialTable(filteredOfficials);
    }
}
