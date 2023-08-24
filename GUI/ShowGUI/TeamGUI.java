package GUI.ShowGUI;

import DataBase.DatabaseManager;
import Entity.Team;
import GUI.Common.LookAndFeelUtil;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

public class TeamGUI extends JDialog {
    private DatabaseManager databaseManager;
    private JTextField searchField;
    private JButton searchButton;
    private JTable teamTable;
    private DefaultTableModel teamTableModel;

    public TeamGUI(JFrame parentFrame, DatabaseManager databaseManager) {
        super(parentFrame, "Team Information", true);
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

        initializeTeamTable(mainPanel);

        getContentPane().add(mainPanel);
        setSize(width, height);
        setLocationRelativeTo(null);
    }

    private void setupUIComponents(JPanel mainPanel) {
        JLabel titleLabel = new JLabel(
                "<html><h1 style='color: #007bff; font-family: Arial, sans-serif;'>"
                        + "Team Information</h1></html>");
        titleLabel.setBounds(10, 10, 1080, 50);
        mainPanel.add(titleLabel);

        JPanel searchPanel = new JPanel(null);
        searchPanel.setBounds(480, 25, 290, 40);

        searchField = new JTextField();
        searchField.setBounds(0, 0, 200, 30);
        searchField.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    performSearch();
                }
            }
        });
        searchPanel.add(searchField);

        searchButton = new JButton("Search");
        searchButton.setBounds(210, 0, 80, 30);
        searchButton.addActionListener(e -> performSearch());
        searchPanel.add(searchButton);

        mainPanel.add(searchPanel);
    }

    private void initializeTeamTable(JPanel mainPanel) {
        int width = 800;
        int height = 600;

        String[] columnNames = { "Team ID", "Name", "Logo" };
        teamTableModel = new DefaultTableModel(columnNames, 0);
        teamTable = new JTable(teamTableModel);

        teamTable.setDefaultRenderer(Object.class, new CustomTableCellRenderer());
        teamTable.setDefaultEditor(Object.class, null);

        teamTable.setRowHeight(30);

        JScrollPane scrollPane = new JScrollPane(teamTable);
        scrollPane.setBounds(10, 80, width - 30, height - 155);
        mainPanel.add(scrollPane);

        TableColumnModel columnModel = teamTable.getColumnModel();
        columnModel.getColumn(0).setPreferredWidth(70);
        columnModel.getColumn(1).setPreferredWidth(200);
        columnModel.getColumn(2).setPreferredWidth(200);

        updateTeamTable(databaseManager.getAllTeams());
    }

    private void updateTeamTable(List<Team> teams) {
        teamTableModel.setRowCount(0);

        for (Team team : teams) {
            Object[] rowData = { team.getTeamID(), team.getTeamName(), team.getLogo() };
            teamTableModel.addRow(rowData);
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

        List<Team> allTeams = databaseManager.getAllTeams();
        List<Team> filteredTeams = new ArrayList<>();

        for (Team team : allTeams) {
            if (String.valueOf(team.getTeamID()).contains(searchTerm) ||
                    team.getTeamName().toLowerCase().contains(searchTerm)) {
                filteredTeams.add(team);
            }
        }

        updateTeamTable(filteredTeams);
    }
}
