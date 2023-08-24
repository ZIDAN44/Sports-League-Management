package GUI.CreateGUI;

import DataBase.DatabaseManager;
import Entity.MatchOfficial;
import GUI.Common.LookAndFeelUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

public class MatchOfficialGUI extends JDialog implements ActionListener {
    private JTextField nameField;
    private JButton addButton, cancelButton;
    private DatabaseManager databaseManager;

    public MatchOfficialGUI(JFrame parentFrame, DatabaseManager databaseManager) {
        super(parentFrame, "Create Match Official", true);
        this.databaseManager = databaseManager;
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        LookAndFeelUtil.setSubstanceMistSilverLookAndFeel();

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel matchOfficialPanel = createMatchOfficialPanel();
        mainPanel.add(matchOfficialPanel);

        JPanel buttonPanel = new JPanel();
        addButton = new JButton("Add");
        addButton.addActionListener(this);
        cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(this);
        buttonPanel.add(addButton);
        buttonPanel.add(cancelButton);

        mainPanel.add(buttonPanel);
        add(mainPanel);

        pack();
        setLocationRelativeTo(parentFrame);
    }

    private JPanel createMatchOfficialPanel() {
        JPanel panel = new JPanel();
        panel.setBorder(BorderFactory.createTitledBorder("Match Official"));
        panel.setLayout(new GridLayout(1, 2, 5, 5));

        JLabel nameLabel = new JLabel("Name:");
        nameField = new JTextField();

        panel.add(nameLabel);
        panel.add(nameField);

        return panel;
    }

    private void addMatchOfficial() {
        String matchOfficialName = nameField.getText();
        MatchOfficial matchOfficial = new MatchOfficial(matchOfficialName);

        try {
            if (databaseManager.insertMatchOfficial(matchOfficial)) {
                JOptionPane.showMessageDialog(this, "Match Official added successfully!");
            } else {
                JOptionPane.showMessageDialog(this, "Failed to add the Match Official!");
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Failed to add the Match Official: " + ex.getMessage());
        }

        dispose();
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == addButton) {
            addMatchOfficial();
        } else if (e.getSource() == cancelButton) {
            dispose();
        }
    }
}
