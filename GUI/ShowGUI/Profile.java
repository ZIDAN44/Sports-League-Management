package GUI.ShowGUI;

import Entity.CEO;
import GUI.CEO_GUI;
import GUI.Common.CustomCursor;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Profile extends JDialog implements ActionListener {
    private CEO currentCEO;
    private JButton closeButton;
    private JLabel idLabel, nameLabel, emailLabel, phoneLabel;

    public Profile(CEO_GUI parent, CEO ceo) {
        super(parent, "My Profile", true);
        this.currentCEO = ceo;

        setSize(500, 300);
        setLayout(null);
        setLocationRelativeTo(parent);
        setModal(true);
        setResizable(false);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        Font labelFont = new Font("Arial", Font.PLAIN, 14);
        Font valueFont = new Font("Arial", Font.BOLD, 15);

        idLabel = new JLabel("ID: " + currentCEO.getID());
        idLabel.setBounds(30, 30, 300, 30);
        idLabel.setFont(valueFont);
        add(idLabel);

        nameLabel = new JLabel("Name: " + currentCEO.getName());
        nameLabel.setBounds(30, 70, 300, 30);
        nameLabel.setFont(valueFont);
        add(nameLabel);

        emailLabel = new JLabel("Email: " + currentCEO.getEmail());
        emailLabel.setBounds(30, 110, 300, 30);
        emailLabel.setFont(valueFont);
        add(emailLabel);

        phoneLabel = new JLabel("Phone: " + currentCEO.getPhoneNumber());
        phoneLabel.setBounds(30, 150, 300, 30);
        phoneLabel.setFont(valueFont);
        add(phoneLabel);

        closeButton = new JButton("Close");
        closeButton.setBounds(195, 203, 100, 30);
        closeButton.addActionListener(this);
        closeButton.setFont(labelFont);
        closeButton.setBackground(new Color(59, 89, 152));
        closeButton.setForeground(Color.BLACK);
        add(closeButton);

        setCursorIcon();
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == closeButton) {
            dispose();
        }
    }

    private void setCursorIcon() {
        CustomCursor.setCustomCursor(this);
        CustomCursor.setHandCursor(closeButton);
    }
}
