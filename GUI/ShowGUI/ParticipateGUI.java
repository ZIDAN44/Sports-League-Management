package GUI.ShowGUI;

import DataBase.DatabaseManager;
import Entity.Team;
import GUI.Common.LookAndFeelUtil;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ParticipateGUI extends JDialog {
    private DatabaseManager databaseManager;
    private JTextField searchField;
    private JButton searchButton;
    private JTable participateTable;
    private DefaultTableModel participateTableModel;

    public ParticipateGUI(JFrame parentFrame, DatabaseManager databaseManager) {
        super(parentFrame, "Participating Teams", true);
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

        initializeParticipateTable(mainPanel);

        getContentPane().add(mainPanel);
        setSize(width, height);
        setLocationRelativeTo(null);
    }

    private void setupUIComponents(JPanel mainPanel) {
        JLabel titleLabel = new JLabel(
                "<html><h1 style='color: #007bff; font-family: Arial, sans-serif;'>"
                        + "Participating Teams</h1></html>");
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

    private void initializeParticipateTable(JPanel mainPanel) {
        int width = 800;
        int height = 600;

        String[] columnNames = { "Team ID", "Match ID", "Participate" };
        participateTableModel = new DefaultTableModel(columnNames, 0);
        participateTable = new JTable(participateTableModel);

        participateTable.setDefaultRenderer(Object.class, new CustomTableCellRenderer());
        participateTable.setDefaultEditor(Object.class, null);

        participateTable.setRowHeight(30);

        JScrollPane scrollPane = new JScrollPane(participateTable);
        scrollPane.setBounds(10, 80, width - 30, height - 155);
        mainPanel.add(scrollPane);

        TableColumnModel columnModel = participateTable.getColumnModel();
        columnModel.getColumn(0).setPreferredWidth(30);
        columnModel.getColumn(1).setPreferredWidth(30);
        columnModel.getColumn(2).setPreferredWidth(210);

        try {
            updateParticipateTable(databaseManager.getAllParticipates());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void updateParticipateTable(List<Team> teams) {
        participateTableModel.setRowCount(0);

        for (Team team : teams) {
            Object[] rowData = {
                    team.getTeamID(),
                    team.getMatchID(),
                    team.getPTName()
            };
            participateTableModel.addRow(rowData);
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

        List<Team> allTeams;
        try {
            allTeams = databaseManager.getAllParticipates();
        } catch (SQLException e) {
            allTeams = new ArrayList<>();
            e.printStackTrace();
        }
        List<Team> filteredTeams = new ArrayList<>();

        for (Team team : allTeams) {
            if (String.valueOf(team.getTeamID()).contains(searchTerm) ||
                    team.getPTName().toLowerCase().contains(searchTerm)) {
                filteredTeams.add(team);
            }
        }

        updateParticipateTable(filteredTeams);
    }
}
