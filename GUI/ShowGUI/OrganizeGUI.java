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

public class OrganizeGUI extends JDialog {
    private DatabaseManager databaseManager;
    private JTextField searchField;
    private JButton searchButton;
    private JTable organizeTable;
    private DefaultTableModel organizeTableModel;

    public OrganizeGUI(JFrame parentFrame, DatabaseManager databaseManager) {
        super(parentFrame, "Organized Leagues", true);
        this.databaseManager = databaseManager;
        LookAndFeelUtil.setSubstanceNebulaLookAndFeel();
        createGUI();
    }

    private void createGUI() {
        int width = 850;
        int height = 600;

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(null);

        setupUIComponents(mainPanel);

        initializeOrganizeTable(mainPanel);

        getContentPane().add(mainPanel);
        setSize(width, height);
        setLocationRelativeTo(null);
    }

    private void setupUIComponents(JPanel mainPanel) {
        JLabel titleLabel = new JLabel(
                "<html><h1 style='color: #007bff; font-family: Arial, sans-serif;'>"
                        + "Organized Leagues</h1></html>");
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

    private void initializeOrganizeTable(JPanel mainPanel) {
        int width = 850;
        int height = 600;

        String[] columnNames = { "League ID", "League Name", "Season", "Start Date", "End Date", "CEO ID" };
        organizeTableModel = new DefaultTableModel(columnNames, 0);
        organizeTable = new JTable(organizeTableModel);

        organizeTable.setDefaultRenderer(Object.class, new CustomTableCellRenderer());
        organizeTable.setDefaultEditor(Object.class, null);

        organizeTable.setRowHeight(30);

        JScrollPane scrollPane = new JScrollPane(organizeTable);
        scrollPane.setBounds(10, 80, width - 30, height - 155);
        mainPanel.add(scrollPane);

        TableColumnModel columnModel = organizeTable.getColumnModel();
        columnModel.getColumn(0).setPreferredWidth(70);
        columnModel.getColumn(1).setPreferredWidth(195);
        columnModel.getColumn(2).setPreferredWidth(100);
        columnModel.getColumn(3).setPreferredWidth(100);
        columnModel.getColumn(4).setPreferredWidth(100);
        columnModel.getColumn(5).setPreferredWidth(150);

        try {
            updateOrganizeTable(databaseManager.getAllLeagues());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void updateOrganizeTable(List<League> organizedLeagues) {
        organizeTableModel.setRowCount(0);

        for (League organize : organizedLeagues) {
            Object[] rowData = {
                    organize.getLeagueID(),
                    organize.getLeagueName(),
                    organize.getSeason(),
                    organize.getStartDate(),
                    organize.getEndDate(),
                    organize.getCEO().getID()
            };
            organizeTableModel.addRow(rowData);
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

        List<League> allOrganizedLeagues;
        try {
            allOrganizedLeagues = databaseManager.getAllLeagues();
        } catch (SQLException e) {
            allOrganizedLeagues = new ArrayList<>();
            e.printStackTrace();
        }
        List<League> filteredLeagues = new ArrayList<>();

        for (League organize : allOrganizedLeagues) {
            if (String.valueOf(organize.getLeagueID()).contains(searchTerm) ||
                    organize.getLeagueName().toLowerCase().contains(searchTerm)) {
                filteredLeagues.add(organize);
            }
        }

        updateOrganizeTable(filteredLeagues);
    }
}
