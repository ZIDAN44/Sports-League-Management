package GUI.ShowGUI;

import DataBase.DatabaseManager;
import Entity.Match;
import GUI.Common.LookAndFeelUtil;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ScheduleGUI extends JDialog {
    private DatabaseManager databaseManager;
    private JTextField searchField;
    private JButton searchButton;
    private JTable matchTable;
    private DefaultTableModel matchTableModel;

    public ScheduleGUI(JFrame parentFrame, DatabaseManager databaseManager) {
        super(parentFrame, "Match Schedule", true);
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

        initializeMatchTable(mainPanel);

        getContentPane().add(mainPanel);
        setSize(width, height);
        setLocationRelativeTo(null);
    }

    private void setupUIComponents(JPanel mainPanel) {
        JLabel titleLabel = new JLabel(
                "<html><h1 style='color: #007bff; font-family: Arial, sans-serif;'>"
                        + "Match Schedule</h1></html>");
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

    private void initializeMatchTable(JPanel mainPanel) {
        int width = 800;
        int height = 600;

        String[] columnNames = { "Schedule No", "Date", "Time", "Venue", "Match ID", "CEO ID" };
        matchTableModel = new DefaultTableModel(columnNames, 0);
        matchTable = new JTable(matchTableModel);

        matchTable.setDefaultRenderer(Object.class, new CustomTableCellRenderer());
        matchTable.setDefaultEditor(Object.class, null);

        matchTable.setRowHeight(30);

        JScrollPane scrollPane = new JScrollPane(matchTable);
        scrollPane.setBounds(10, 80, width - 30, height - 155);
        mainPanel.add(scrollPane);

        TableColumnModel columnModel = matchTable.getColumnModel();
        columnModel.getColumn(0).setPreferredWidth(100);
        columnModel.getColumn(1).setPreferredWidth(120);
        columnModel.getColumn(2).setPreferredWidth(90);
        columnModel.getColumn(3).setPreferredWidth(220);
        columnModel.getColumn(4).setPreferredWidth(100);
        columnModel.getColumn(5).setPreferredWidth(100);

        try {
            updateMatchTable(databaseManager.getAllSchedules());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void updateMatchTable(List<Match> matches) {
        matchTableModel.setRowCount(0);

        for (Match match : matches) {
            Object[] rowData = {
                    match.getSchduleNo(),
                    match.getDate(),
                    match.getTime(),
                    match.getVenue(),
                    match.getMatchID(),
                    match.getceoID()
            };
            matchTableModel.addRow(rowData);
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

        List<Match> allMatches;
        try {
            allMatches = databaseManager.getAllSchedules();
        } catch (SQLException e) {
            allMatches = new ArrayList<>();
            e.printStackTrace();
        }
        List<Match> filteredMatches = new ArrayList<>();

        for (Match match : allMatches) {
            if (String.valueOf(match.getSchduleNo()).contains(searchTerm) ||
                    match.getVenue().toLowerCase().contains(searchTerm)) {
                filteredMatches.add(match);
            }
        }

        updateMatchTable(filteredMatches);
    }
}
