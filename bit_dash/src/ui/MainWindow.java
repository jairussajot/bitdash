package ui;

import game.GameSession;
import javax.swing.*;
import java.awt.*;

/** The single application window. Swaps between screens with a CardLayout. */
public class MainWindow extends JFrame {

    private static final String MENU = "menu";
    private static final String SETUP = "setup";
    private static final String GAME = "game";
    private static final String RESULT = "result";
    private static final String SCORES = "scores";
    private static final String HELP = "help";

    private final CardLayout cards = new CardLayout();
    private final JPanel root = new JPanel(cards);

    private final Scoreboard scoreboard = new Scoreboard();

    private final SetupPanel setupPanel;
    private final GamePanel gamePanel;
    private final ResultPanel resultPanel;
    private final ScoreboardPanel scoreboardPanel;

    public MainWindow() {
        super("BitDash");

        setupPanel = new SetupPanel(this);
        gamePanel = new GamePanel(this);
        resultPanel = new ResultPanel(this);
        scoreboardPanel = new ScoreboardPanel(this, scoreboard);

        root.setBackground(Theme.BG);
        root.add(new MenuPanel(this), MENU);
        root.add(setupPanel, SETUP);
        root.add(gamePanel, GAME);
        root.add(resultPanel, RESULT);
        root.add(scoreboardPanel, SCORES);
        root.add(new InstructionsPanel(this), HELP);

        setContentPane(root);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(720, 560));
        setSize(800, 620);
        setLocationRelativeTo(null);
    }

    // ---- navigation -------------------------------------------------------

    void showMenu() {
        cards.show(root, MENU);
    }

    void showSetup() {
        cards.show(root, SETUP);
        setupPanel.onShow();
    }

    void showInstructions() {
        cards.show(root, HELP);
    }

    void showScoreboard() {
        scoreboardPanel.refresh();
        cards.show(root, SCORES);
    }

    void startGame(String name, GameSession.Mode mode, GameSession.Difficulty difficulty) {
        cards.show(root, GAME);
        gamePanel.begin(new GameSession(name, mode, difficulty));
    }

    /** Called by GamePanel when all 10 questions are answered. */
    void finishGame(GameSession session) {
        scoreboard.addScore(session.toScore());
        resultPanel.show(session);
        cards.show(root, RESULT);
    }

    void quit() {
        dispose();
        System.exit(0);
    }
}
