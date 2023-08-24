package GUI;

import DataBase.DatabaseManager;
import Entity.CEO;
import GUI.Common.CustomCursor;
import GUI.UpdateTableGUI.CoachGUI;
import GUI.UpdateTableGUI.LeagueGUI;
import GUI.UpdateTableGUI.MatchGUI;
import GUI.UpdateTableGUI.MatchOfficialGUI;
import GUI.UpdateTableGUI.PlayerGUI;
import GUI.UpdateTableGUI.TeamGUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class UpdatePanelGUI extends JFrame implements ActionListener {
    private DatabaseManager databaseManager;
    private JButton updateTeamButton, updatePlayerButton, updateMatchButton, updateCoachButton, updateLeagueButton,
            updateMatchOfficialButton, backButton;
    private CEO_GUI parentFrame;
    private CEO loggedInCEO;

    public UpdatePanelGUI(CEO_GUI parentFrame, DatabaseManager databaseManager, CEO ceo) {
        this.parentFrame = parentFrame;
        this.databaseManager = databaseManager;
        this.loggedInCEO = ceo;

        createGUI();
        setResizable(false);
    }

    private void createGUI() {
        setTitle("Update Panel");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(1100, 800);
        setLocationRelativeTo(null);
        setLayout(null);

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
        int buttonWidth = 250;
        int buttonHeight = 30;
        int startX = 420;
        int startY = 250;
        int gapY = 40;

        updateTeamButton = createButton("Update Team", startX, startY, buttonWidth, buttonHeight);
        startY += gapY;
        updatePlayerButton = createButton("Update Player", startX, startY, buttonWidth, buttonHeight);
        startY += gapY;
        updateMatchButton = createButton("Update Match", startX, startY, buttonWidth, buttonHeight);
        startY += gapY;
        updateCoachButton = createButton("Update Coach", startX, startY, buttonWidth, buttonHeight);
        startY += gapY;
        updateLeagueButton = createButton("Update League", startX, startY, buttonWidth, buttonHeight);
        startY += gapY;
        updateMatchOfficialButton = createButton("Update Match Official", startX, startY, buttonWidth, buttonHeight);
        backButton = createIconButton("/GUI/Pictures/Common/Exit.png", 1015, 30, 45, 45);
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

    private JButton createIconButton(String iconPath, int x, int y, int width, int height) {
        JButton button = new JButton();
        ImageIcon icon = new ImageIcon(getClass().getResource(iconPath));
        button = new JButton(icon);
        button.setBounds(x, y, width, height);
        button.addActionListener(this);
        add(button);
        return button;
    }

    private void setCursorIcon() {
        for (Component component : getContentPane().getComponents()) {
            if (component instanceof JButton) {
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
        if (e.getSource() == updateTeamButton) {
            updateTeam();
        } else if (e.getSource() == updatePlayerButton) {
            updatePlayer();
        } else if (e.getSource() == updateMatchButton) {
            updateMatch();
        } else if (e.getSource() == updateCoachButton) {
            updateCoach();
        } else if (e.getSource() == updateLeagueButton) {
            updateLeague();
        } else if (e.getSource() == updateMatchOfficialButton) {
            updateMatchOfficial();
        } else if (e.getSource() == backButton) {
            dispose();
            parentFrame.setVisible(true);
        }
    }

    private void updateTeam() {
        TeamGUI updateTeamGUI = new TeamGUI(this, databaseManager);
        updateTeamGUI.setVisible(true);
    }

    private void updatePlayer() {
        PlayerGUI updatePlayerGUI = new PlayerGUI(this, databaseManager);
        updatePlayerGUI.setVisible(true);
    }

    private void updateMatch() {
        MatchGUI updateMatchGUI = new MatchGUI(this, databaseManager);
        updateMatchGUI.setVisible(true);
    }

    private void updateCoach() {
        CoachGUI updateCoachGUI = new CoachGUI(this, databaseManager);
        updateCoachGUI.setVisible(true);
    }

    private void updateLeague() {
        LeagueGUI updateLeagueGUI = new LeagueGUI(this, databaseManager, loggedInCEO);
        updateLeagueGUI.setVisible(true);
    }

    private void updateMatchOfficial() {
        MatchOfficialGUI updateMatchOfficialGUI = new MatchOfficialGUI(this, databaseManager);
        updateMatchOfficialGUI.setVisible(true);
    }
}
