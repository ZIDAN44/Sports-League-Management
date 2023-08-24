package GUI;

import DataBase.DatabaseManager;
import GUI.Common.CustomCursor;
import GUI.ShowGUI.AssembleGUI;
import GUI.ShowGUI.AssociateGUI;
import GUI.ShowGUI.CoachGUI;
import GUI.ShowGUI.HireGUI;
import GUI.ShowGUI.LeagueGUI;
import GUI.ShowGUI.MaintainGUI;
import GUI.ShowGUI.MatchInfoGUI;
import GUI.ShowGUI.MatchOfficialGUI;
import GUI.ShowGUI.OrganizeGUI;
import GUI.ShowGUI.ParticipateGUI;
import GUI.ShowGUI.PlayerGUI;
import GUI.ShowGUI.ScheduleGUI;
import GUI.ShowGUI.SuperviseGUI;
import GUI.ShowGUI.TeamGUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ShowPanelGUI extends JFrame implements ActionListener {
    private DatabaseManager databaseManager;
    private JButton showAssembleButton, showAssociateButton, showCoachButton, showHireButton, showLeagueButton,
            showMaintainButton, showMatchInfoButton, showMatchOfficialButton, showOrganizeButton,
            showParticipateButton, showPlayerButton, showScheduleButton, showSuperviseButton, showTeamButton,
            backButton;
    private CEO_GUI parentFrame;

    public ShowPanelGUI(CEO_GUI parentFrame, DatabaseManager databaseManager) {
        this.databaseManager = databaseManager;
        this.parentFrame = parentFrame;

        setTitle("Show Panel");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(1100, 800);
        setLocationRelativeTo(null);
        setLayout(null);

        initButtons();
        initButtonColors();
        setCursorIcon();
        setResizable(false);

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
        int startY = 50;
        int gapY = 40;

        showAssembleButton = createButton("Show Assemble", startX, startY, buttonWidth, buttonHeight);
        startY += gapY;
        showAssociateButton = createButton("Show Associate", startX, startY, buttonWidth, buttonHeight);
        startY += gapY;
        showCoachButton = createButton("Show Coach", startX, startY, buttonWidth, buttonHeight);
        startY += gapY;
        showHireButton = createButton("Show Hire", startX, startY, buttonWidth, buttonHeight);
        startY += gapY;
        showLeagueButton = createButton("Show League", startX, startY, buttonWidth, buttonHeight);
        startY += gapY;
        showMaintainButton = createButton("Show Maintain", startX, startY, buttonWidth, buttonHeight);
        startY += gapY;
        showMatchInfoButton = createButton("Show Match Info", startX, startY, buttonWidth, buttonHeight);
        startY += gapY;
        showMatchOfficialButton = createButton("Show Match Official", startX, startY, buttonWidth, buttonHeight);
        startY += gapY;
        showOrganizeButton = createButton("Show Organize", startX, startY, buttonWidth, buttonHeight);
        startY += gapY;
        showParticipateButton = createButton("Show Participate", startX, startY, buttonWidth, buttonHeight);
        startY += gapY;
        showPlayerButton = createButton("Show Player", startX, startY, buttonWidth, buttonHeight);
        startY += gapY;
        showScheduleButton = createButton("Show Schedule", startX, startY, buttonWidth, buttonHeight);
        startY += gapY;
        showSuperviseButton = createButton("Show Supervise", startX, startY, buttonWidth, buttonHeight);
        startY += gapY;
        showTeamButton = createButton("Show Team", startX, startY, buttonWidth, buttonHeight);
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
        if (e.getSource() == showAssembleButton) {
            showAssemble();
        } else if (e.getSource() == showAssociateButton) {
            showAssociate();
        } else if (e.getSource() == showCoachButton) {
            showCoach();
        } else if (e.getSource() == showHireButton) {
            showHire();
        } else if (e.getSource() == showLeagueButton) {
            showLeague();
        } else if (e.getSource() == showMaintainButton) {
            showMaintain();
        } else if (e.getSource() == showMatchInfoButton) {
            showMatchInfo();
        } else if (e.getSource() == showMatchOfficialButton) {
            showMatchOfficial();
        } else if (e.getSource() == showOrganizeButton) {
            showOrganize();
        } else if (e.getSource() == showParticipateButton) {
            showParticipate();
        } else if (e.getSource() == showPlayerButton) {
            showPlayer();
        } else if (e.getSource() == showScheduleButton) {
            showSchedule();
        } else if (e.getSource() == showSuperviseButton) {
            showSupervise();
        } else if (e.getSource() == showTeamButton) {
            showTeam();
        } else if (e.getSource() == backButton) {
            dispose();
            parentFrame.setVisible(true);
        }
    }

    private void showAssemble() {
        AssembleGUI assembleGUI = new AssembleGUI(this, databaseManager);
        assembleGUI.setVisible(true);
    }

    private void showAssociate() {
        AssociateGUI associateGUI = new AssociateGUI(this, databaseManager);
        associateGUI.setVisible(true);
    }

    private void showCoach() {
        CoachGUI coachGUI = new CoachGUI(this, databaseManager);
        coachGUI.setVisible(true);
    }

    private void showHire() {
        HireGUI hireGUI = new HireGUI(this, databaseManager);
        hireGUI.setVisible(true);
    }

    private void showLeague() {
        LeagueGUI leagueGUI = new LeagueGUI(this, databaseManager);
        leagueGUI.setVisible(true);
    }

    private void showMaintain() {
        MaintainGUI MaintainGUI = new MaintainGUI(this, databaseManager);
        MaintainGUI.setVisible(true);
    }

    private void showMatchInfo() {
        MatchInfoGUI matchInfoGUI = new MatchInfoGUI(this, databaseManager);
        matchInfoGUI.setVisible(true);
    }

    private void showMatchOfficial() {
        MatchOfficialGUI MatchOfficialGUI = new MatchOfficialGUI(this, databaseManager);
        MatchOfficialGUI.setVisible(true);
    }

    private void showOrganize() {
        OrganizeGUI OrganizeGUI = new OrganizeGUI(this, databaseManager);
        OrganizeGUI.setVisible(true);
    }

    private void showParticipate() {
        ParticipateGUI participateGUI = new ParticipateGUI(this, databaseManager);
        participateGUI.setVisible(true);
    }

    private void showPlayer() {
        PlayerGUI playerGUI = new PlayerGUI(this, databaseManager);
        playerGUI.setVisible(true);
    }

    private void showSchedule() {
        ScheduleGUI ScheduleGUI = new ScheduleGUI(this, databaseManager);
        ScheduleGUI.setVisible(true);
    }

    private void showSupervise() {
        SuperviseGUI SuperviseGUI = new SuperviseGUI(this, databaseManager);
        SuperviseGUI.setVisible(true);
    }

    private void showTeam() {
        TeamGUI TeamGUI = new TeamGUI(this, databaseManager);
        TeamGUI.setVisible(true);
    }
}
