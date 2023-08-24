package GUI.ShowGUI;

import DataBase.DatabaseManager;
import Entity.Player;
import GUI.Common.LookAndFeelUtil;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class PlayerGUI extends JDialog {
    private DatabaseManager databaseManager;
    private JTextField searchField;
    private JButton searchButton;
    private JTable playerTable;
    private DefaultTableModel playerTableModel;

    public PlayerGUI(JFrame parentFrame, DatabaseManager databaseManager) {
        super(parentFrame, "Player Information", true);
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

        initializePlayerTable(mainPanel);

        getContentPane().add(mainPanel);
        setSize(width, height);
        setLocationRelativeTo(null);
    }

    private void setupUIComponents(JPanel mainPanel) {
        JLabel titleLabel = new JLabel(
                "<html><h1 style='color: #007bff; font-family: Arial, sans-serif;'>"
                        + "Player Information</h1></html>");
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

    private void initializePlayerTable(JPanel mainPanel) {
        int width = 800;
        int height = 600;

        String[] columnNames = { "Player ID", "Name", "Age", "Position", "Statistics" };
        playerTableModel = new DefaultTableModel(columnNames, 0);
        playerTable = new JTable(playerTableModel);

        playerTable.setDefaultRenderer(Object.class, new CustomTableCellRenderer());
        playerTable.setDefaultEditor(Object.class, null);

        playerTable.setRowHeight(30);

        JScrollPane scrollPane = new JScrollPane(playerTable);
        scrollPane.setBounds(10, 80, width - 30, height - 155);
        mainPanel.add(scrollPane);

        TableColumnModel columnModel = playerTable.getColumnModel();
        columnModel.getColumn(0).setPreferredWidth(70);
        columnModel.getColumn(1).setPreferredWidth(200);
        columnModel.getColumn(2).setPreferredWidth(50);
        columnModel.getColumn(3).setPreferredWidth(100);
        columnModel.getColumn(4).setPreferredWidth(200);

        updatePlayerTable(databaseManager.getAllPlayers());
    }

    private void updatePlayerTable(List<Player> players) {
        playerTableModel.setRowCount(0);

        for (Player player : players) {
            Object[] rowData = {
                    player.getPlayerID(),
                    player.getName(),
                    player.getAge(),
                    player.getPosition(),
                    player.getStatistics()
            };
            playerTableModel.addRow(rowData);
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

        List<Player> allPlayers = databaseManager.getAllPlayers();
        List<Player> filteredPlayers = new ArrayList<>();

        for (Player player : allPlayers) {
            if (String.valueOf(player.getPlayerID()).contains(searchTerm) ||
                    player.getName().toLowerCase().contains(searchTerm)) {
                filteredPlayers.add(player);
            }
        }

        updatePlayerTable(filteredPlayers);
    }
}
