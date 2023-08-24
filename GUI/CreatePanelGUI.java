package GUI;

import Entity.CEO;
import DataBase.DatabaseManager;
import GUI.Common.CustomCursor;
import GUI.CreateGUI.AssembleGUI;
import GUI.CreateGUI.AssignGUI;
import GUI.CreateGUI.CoachGUI;
import GUI.CreateGUI.LeagueGUI;
import GUI.CreateGUI.MatchGUI;
import GUI.CreateGUI.MatchOfficialGUI;
import GUI.CreateGUI.PlayerGUI;
import GUI.CreateGUI.PlayerSelectionGUI;
import GUI.CreateGUI.TeamGUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CreatePanelGUI extends JFrame implements ActionListener {
    private DatabaseManager databaseManager;
    private JButton createLeagueButton, createTeamButton, createPlayerButton, assignPlayersButton, assignCoachButton,
            assignTeamButton, createCoachButton, createMatchButton, createMatchOfficialButton, backButton;
    private CEO_GUI parentFrame;
    private CEO loggedInCEO;

    public CreatePanelGUI(CEO_GUI parentFrame, DatabaseManager databaseManager, CEO ceo) {
        this.databaseManager = databaseManager;
        this.parentFrame = parentFrame;
        this.loggedInCEO = ceo;

        createGUI();
    }

    private void createGUI() {
        setTitle("Create Panel");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
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
        setResizable(false);
    }

    private void initButtons() {
        createTeamButton = createButton("Create Team", 420, 180, 250, 30);
        createPlayerButton = createButton("Create Player", 420, 220, 250, 30);
        assignPlayersButton = createButton("Assign Players", 420, 260, 250, 30);
        assignCoachButton = createButton("Assign Coach", 420, 300, 250, 30);
        assignTeamButton = createButton("Assign Team", 420, 340, 250, 30);
        createCoachButton = createButton("Create Coach", 420, 380, 250, 30);
        createMatchButton = createButton("Create Match", 420, 420, 250, 30);
        createLeagueButton = createButton("Create League", 420, 460, 250, 30);
        createMatchOfficialButton = createButton("Create Match Official", 420, 500, 250, 30);
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

    private void leaguePanel() {
        LeagueGUI createLeagueGUI = new LeagueGUI(this, databaseManager, loggedInCEO);
        createLeagueGUI.setVisible(true);
    }

    private void createTeam() {
        TeamGUI createTeamGUI = new TeamGUI(this, databaseManager);
        createTeamGUI.setVisible(true);
    }

    private void createPlayer() {
        PlayerGUI createPlayerGUI = new PlayerGUI(this, databaseManager);
        createPlayerGUI.setVisible(true);
    }

    private void assignPlayers() {
        PlayerSelectionGUI createPlayerSelectionGUI = new PlayerSelectionGUI(this, databaseManager);
        createPlayerSelectionGUI.setVisible(true);
    }

    private void assignCoach() {
        AssignGUI assignCoachGUI = new AssignGUI(this, databaseManager);
        assignCoachGUI.setVisible(true);
    }

    private void assignTeam() {
        AssembleGUI assembleTeamToLeagueGUI = new AssembleGUI(this, databaseManager);
        assembleTeamToLeagueGUI.setVisible(true);
    }

    private void createCoach() {
        CoachGUI createCoachGUI = new CoachGUI(this, databaseManager, loggedInCEO);
        createCoachGUI.setVisible(true);
    }

    private void createMatch() {
        MatchGUI createMatchGUI = new MatchGUI(this, databaseManager, loggedInCEO);
        createMatchGUI.setVisible(true);
    }

    private void createMatchOfficial() {
        MatchOfficialGUI createMatchOfficialGUI = new MatchOfficialGUI(this, databaseManager);
        createMatchOfficialGUI.setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == createLeagueButton) {
            leaguePanel();
        } else if (e.getSource() == createTeamButton) {
            createTeam();
        } else if (e.getSource() == createPlayerButton) {
            createPlayer();
        } else if (e.getSource() == assignPlayersButton) {
            assignPlayers();
        } else if (e.getSource() == assignCoachButton) {
            assignCoach();
        } else if (e.getSource() == assignTeamButton) {
            assignTeam();
        } else if (e.getSource() == createCoachButton) {
            createCoach();
        } else if (e.getSource() == createMatchButton) {
            createMatch();
        } else if (e.getSource() == createMatchOfficialButton) {
            createMatchOfficial();
        } else if (e.getSource() == backButton) {
            dispose();
            parentFrame.setVisible(true);
        }
    }
}
