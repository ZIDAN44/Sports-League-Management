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

public class MatchInfoGUI extends JDialog {
    private DatabaseManager databaseManager;
    private JTextField searchField;
    private JButton searchButton;
    private JTable matchInfoTable;
    private DefaultTableModel matchInfoTableModel;

    public MatchInfoGUI(JFrame parentFrame, DatabaseManager databaseManager) {
        super(parentFrame, "Match Information", true);
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

        initializeMatchInfoTable(mainPanel);

        getContentPane().add(mainPanel);
        setSize(width, height);
        setLocationRelativeTo(null);
    }

    private void setupUIComponents(JPanel mainPanel) {
        JLabel titleLabel = new JLabel(
                "<html><h1 style='color: #007bff; font-family: Arial, sans-serif;'>"
                        + "Match Information</h1></html>");
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

    private void initializeMatchInfoTable(JPanel mainPanel) {
        int width = 780;
        int height = 480;

        String[] columnNames = { "Match ID", "Date", "Time", "Venue" };
        matchInfoTableModel = new DefaultTableModel(columnNames, 0);
        matchInfoTable = new JTable(matchInfoTableModel);

        matchInfoTable.setDefaultRenderer(Object.class, new CustomTableCellRenderer());
        matchInfoTable.setDefaultEditor(Object.class, null);

        matchInfoTable.setRowHeight(30);

        JScrollPane scrollPane = new JScrollPane(matchInfoTable);
        scrollPane.setBounds(10, 80, width - 30, height - 160);
        mainPanel.add(scrollPane);

        TableColumnModel columnModel = matchInfoTable.getColumnModel();
        columnModel.getColumn(0).setPreferredWidth(70);
        columnModel.getColumn(1).setPreferredWidth(150);
        columnModel.getColumn(2).setPreferredWidth(100);
        columnModel.getColumn(3).setPreferredWidth(400);

        try {
            updateMatchInfoTable(databaseManager.getAllMatches());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void updateMatchInfoTable(List<Match> matchInfoList) {
        matchInfoTableModel.setRowCount(0);

        for (Match matchInfo : matchInfoList) {
            Object[] rowData = { matchInfo.getMatchID(), matchInfo.getDate(), matchInfo.getTime(),
                    matchInfo.getVenue() };
            matchInfoTableModel.addRow(rowData);
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

        List<Match> allMatchInfo;
        try {
            allMatchInfo = databaseManager.getAllMatches();
        } catch (SQLException ex) {
            allMatchInfo = new ArrayList<>();
            JOptionPane.showMessageDialog(this, "Failed to fetch match from the database: " + ex.getMessage());
        }
        List<Match> filteredMatchInfo = new ArrayList<>();

        for (Match matchInfo : allMatchInfo) {
            if (String.valueOf(matchInfo.getMatchID()).contains(searchTerm) ||
                    matchInfo.getTime().toLowerCase().contains(searchTerm) ||
                    matchInfo.getVenue().toLowerCase().contains(searchTerm)) {
                filteredMatchInfo.add(matchInfo);
            }
        }

        updateMatchInfoTable(filteredMatchInfo);
    }
}
