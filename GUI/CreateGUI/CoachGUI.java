package GUI.CreateGUI;

import DataBase.DatabaseManager;
import Entity.CEO;
import Entity.Coach;
import GUI.Common.LookAndFeelUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.time.LocalDate;

public class CoachGUI extends JDialog implements ActionListener {
    private JTextField nameField;
    private JTextField salaryField;
    private JTextField positionField;
    private JButton addButton, cancelButton;
    private DatabaseManager databaseManager;
    private CEO loggedInCEO;

    public CoachGUI(JFrame parentFrame, DatabaseManager databaseManager, CEO loggedInCEO) {
        super(parentFrame, "Create Coach", true);
        this.databaseManager = databaseManager;
        this.loggedInCEO = loggedInCEO;

        LookAndFeelUtil.setSubstanceMistSilverLookAndFeel();

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel coachPanel = createCoachPanel();
        mainPanel.add(coachPanel);

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

    private JPanel createCoachPanel() {
        JPanel panel = new JPanel();
        panel.setBorder(BorderFactory.createTitledBorder("Coach"));
        panel.setLayout(new GridLayout(4, 2, 5, 5));

        JLabel nameLabel = new JLabel("Name:");
        nameField = new JTextField();

        JLabel salaryLabel = new JLabel("Salary:");
        salaryField = new JTextField();

        JLabel positionLabel = new JLabel("Position:");
        positionField = new JTextField();

        panel.add(nameLabel);
        panel.add(nameField);
        panel.add(salaryLabel);
        panel.add(salaryField);
        panel.add(positionLabel);
        panel.add(positionField);

        return panel;
    }

    private void addCoach() {
        String name = nameField.getText().trim();
        double salary = Double.parseDouble(salaryField.getText());
        String position = positionField.getText().trim();
        LocalDate hireDate = LocalDate.now();

        Coach coach = new Coach(name, position, salary);

        try {
            if (databaseManager.insertCoachWithHire(coach, hireDate, loggedInCEO.getID())) {
                JOptionPane.showMessageDialog(this, "Coach added successfully!");
            } else {
                JOptionPane.showMessageDialog(this, "Failed to add the coach!");
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Failed to add the coach: " + ex.getMessage());
        }

        dispose();
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == addButton) {
            addCoach();
        } else if (e.getSource() == cancelButton) {
            dispose();
        }
    }
}
