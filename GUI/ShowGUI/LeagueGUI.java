package GUI.ShowGUI;

import DataBase.DatabaseManager;
import Entity.League;
import GUI.Common.LookAndFeelUtil;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class LeagueGUI extends JDialog {
    private DatabaseManager databaseManager;
    private JTextField searchField;
    private JButton searchButton;
    private JTable leagueTable;
    private DefaultTableModel leagueTableModel;

    public LeagueGUI(JFrame parentFrame, DatabaseManager databaseManager) {
        super(parentFrame, "League Information", true);
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

        initializeLeagueTable(mainPanel);

        getContentPane().add(mainPanel);
        setSize(width, height);
        setLocationRelativeTo(null);
    }

    private void setupUIComponents(JPanel mainPanel) {
        JLabel titleLabel = new JLabel(
                "<html><h1 style='color: #007bff; font-family: Arial, sans-serif;'>"
                        + "League Information</h1></html>");
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

    private void initializeLeagueTable(JPanel mainPanel) {
        int width = 800;
        int height = 600;

        String[] columnNames = { "League ID", "Name", "Season", "Start Date", "End Date" };
        leagueTableModel = new DefaultTableModel(columnNames, 0);
        leagueTable = new JTable(leagueTableModel);

        leagueTable.setDefaultRenderer(Object.class, new CustomTableCellRenderer());
        leagueTable.setDefaultEditor(Object.class, null);

        leagueTable.setRowHeight(30);

        JScrollPane scrollPane = new JScrollPane(leagueTable);
        scrollPane.setBounds(10, 80, width - 30, height - 155);
        mainPanel.add(scrollPane);

        TableColumnModel columnModel = leagueTable.getColumnModel();
        columnModel.getColumn(0).setPreferredWidth(70);
        columnModel.getColumn(1).setPreferredWidth(200);
        columnModel.getColumn(2).setPreferredWidth(100);
        columnModel.getColumn(3).setPreferredWidth(100);
        columnModel.getColumn(4).setPreferredWidth(100);

        try {
            updateLeagueTable(databaseManager.getAllLeagues());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void updateLeagueTable(List<League> leagues) {
        leagueTableModel.setRowCount(0);

        for (League league : leagues) {
            Object[] rowData = { league.getLeagueID(), league.getLeagueName(), league.getSeason(),
                    league.getStartDate(), league.getEndDate() };
            leagueTableModel.addRow(rowData);
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

        List<League> allLeagues;
        try {
            allLeagues = databaseManager.getAllLeagues();
        } catch (SQLException ex) {
            allLeagues = new ArrayList<>();
            JOptionPane.showMessageDialog(this, "Failed to fetch leagues from the database: " + ex.getMessage());
        }
        List<League> filteredLeagues = new ArrayList<>();

        for (League league : allLeagues) {
            if (String.valueOf(league.getLeagueID()).contains(searchTerm) ||
                    league.getLeagueName().toLowerCase().contains(searchTerm)) {
                filteredLeagues.add(league);
            }
        }

        updateLeagueTable(filteredLeagues);
    }
}
