package GUI.ShowGUI;

import DataBase.DatabaseManager;
import Entity.Team;
import GUI.Common.LookAndFeelUtil;
import Entity.Player;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AssociateGUI extends JDialog {
    private DatabaseManager databaseManager;
    private JTable associationTable;
    private DefaultTableModel associationTableModel;
    private JTextField searchField;
    private JButton searchButton;

    public AssociateGUI(JFrame parentFrame, DatabaseManager databaseManager) {
        super(parentFrame, "Team-Player Associations", true);
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

        initializeAssociationTable(mainPanel);

        getContentPane().add(mainPanel);
        setSize(width, height);
        setLocationRelativeTo(null);
    }

    private void setupUIComponents(JPanel mainPanel) {
        JLabel titleLabel = new JLabel("<html><h1 style='color: #007bff; font-family: Arial, sans-serif;'>"
                + "Team-Player Associations</h1></html>");
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

    private void initializeAssociationTable(JPanel mainPanel) {
        int width = 800;
        int height = 600;

        String[] columnNames = { "Team ID", "Player IDs" };
        associationTableModel = new DefaultTableModel(columnNames, 0);
        associationTable = new JTable(associationTableModel);

        associationTable.setDefaultRenderer(Object.class, new CustomTableCellRenderer());
        associationTable.setDefaultEditor(Object.class, null);

        associationTable.setRowHeight(30);

        JScrollPane scrollPane = new JScrollPane(associationTable);
        scrollPane.setBounds(10, 80, width - 30, height - 155);
        mainPanel.add(scrollPane);

        TableColumnModel columnModel = associationTable.getColumnModel();
        columnModel.getColumn(0).setPreferredWidth(70);
        columnModel.getColumn(1).setPreferredWidth(200);

        updateAssociationTable(databaseManager.getAllTeams());
    }

    private void updateAssociationTable(List<Team> teams) {
        associationTableModel.setRowCount(0);

        for (Team team : teams) {
            List<Player> players;
            try {
                players = databaseManager.getPlayersByTeamName(team.getTeamName());
            } catch (SQLException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Failed to fetch Player Information for the selected team!",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            StringBuilder playerIDs = new StringBuilder();

            for (Player player : players) {
                playerIDs.append(player.getPlayerID()).append(", ");
            }

            if (playerIDs.length() > 0) {
                playerIDs.setLength(playerIDs.length() - 2);
            }

            Object[] rowData = { team.getTeamID(), playerIDs.toString() };
            associationTableModel.addRow(rowData);
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
            if (String.valueOf(team.getTeamID()).contains(searchTerm)) {
                filteredTeams.add(team);
            }
        }

        updateAssociationTable(filteredTeams);
    }
}
