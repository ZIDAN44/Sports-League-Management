package GUI;

import DataBase.DatabaseManager;
import Entity.CEO;
import GUI.Common.CustomCursor;
import GUI.Common.LookAndFeelUtil;
import GUI.ShowGUI.Profile;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CEO_GUI extends JFrame implements ActionListener {
    private DatabaseManager databaseManager;
    private JButton myProfileButton, createPanelButton, showPanelButton, updatePanelButton;
    private CEO loggedInCEO;

    public CEO_GUI(DatabaseManager databaseManager, CEO ceo) {
        this.databaseManager = databaseManager;
        this.loggedInCEO = ceo;

        LookAndFeelUtil.setSubstanceNebulaLookAndFeel();

        createGUI();
        setResizable(false);
    }

    private void createGUI() {
        setTitle("CEO GUI");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1100, 800);
        setLocationRelativeTo(null);
        setLayout(null);

        ImageIcon icon = new ImageIcon(getClass().getResource("/GUI/Pictures/Common/Icon.png"));
        setIconImage(icon.getImage());

        initButtons();
        initButtonColors();
        setCursorIcon();

        ImageIcon bgImage = new ImageIcon(getClass().getResource("/GUI/Pictures/CEO/Background.png"));
        JLabel bgLabel = new JLabel(bgImage);
        bgLabel.setBounds(0, 0, getWidth(), getHeight());
        add(bgLabel);

        setVisible(true);
    }

    private void initButtons() {

        myProfileButton = createButton("", 90, 70, 150, 150);
        ImageIcon myProfileIcon = new ImageIcon(getClass().getResource("/GUI/Pictures/CEO/My_Profile.png"));
        myProfileButton.setIcon(myProfileIcon);

        createPanelButton = createButton("Create DADATA", 420, 300, 250, 30);
        showPanelButton = createButton("View All Data", 420, 340, 250, 30);
        updatePanelButton = createButton("Update Panel", 420, 380, 250, 30);
    }

    private JButton createButton(String text, int x, int y, int width, int height) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 15));
        button.setForeground(Color.BLACK);
        button.setBounds(x, y, width, height);
        button.addActionListener(this);
        add(button);
        return button;
    }

    private void myProfile() {
        Profile myProfileDialog = new Profile(this, loggedInCEO);
        myProfileDialog.setVisible(true);
    }

    private void createPanel() {
        CreatePanelGUI createPanelGUI = new CreatePanelGUI(this, databaseManager, loggedInCEO);
        createPanelGUI.setVisible(true);
    }

    private void showTablePanel() {
        ShowPanelGUI showPanelGUI = new ShowPanelGUI(this, databaseManager);
        showPanelGUI.setVisible(true);
    }

    private void updateTablePanel() {
        UpdatePanelGUI updatePanelGUI = new UpdatePanelGUI(this, databaseManager, loggedInCEO);
        updatePanelGUI.setVisible(true);
    }

    private void setCursorIcon() {
        for (Component component : getContentPane().getComponents()) {
            if (component instanceof JButton) {
                CustomCursor.setCustomCursor(this);
                CustomCursor.setHandCursor((JButton) component);
            }
        }
        CustomCursor.setCustomCursor(this);
    }

    private void initButtonColors() {
        for (Component component : getContentPane().getComponents()) {
            if (component instanceof JButton) {
                component.setBackground(Color.decode("#431391"));
            }
        }
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == myProfileButton) {
            myProfile();
        } else if (e.getSource() == createPanelButton) {
            createPanel();
            dispose();
        } else if (e.getSource() == showPanelButton) {
            showTablePanel();
            dispose();
        } else if (e.getSource() == updatePanelButton) {
            updateTablePanel();
            dispose();
        }
    }
}
