package ui;

import game.GameSession.Difficulty;
import game.GameSession.Mode;

import javax.swing.*;
import java.awt.*;


class SetupPanel extends JPanel {

    private static final Mode[] MODES = Mode.values();
    private static final Difficulty[] DIFFICULTIES = Difficulty.values();

    private final JTextField nameField = Theme.field(16);
    private final Theme.ChoiceBar modeBar;
    private final Theme.ChoiceBar difficultyBar;
    private final JLabel difficultyHint =
            Theme.label(" ", Theme.sans(Font.PLAIN, 14), Theme.MUTED);

    SetupPanel(MainWindow window) {
        setBackground(Theme.BG);
        setLayout(new BorderLayout());

        String[] modeLabels = new String[MODES.length];
        for (int i = 0; i < MODES.length; i++) {
            modeLabels[i] = MODES[i].getLabel();
        }
        String[] diffLabels = new String[DIFFICULTIES.length];
        for (int i = 0; i < DIFFICULTIES.length; i++) {
            diffLabels[i] = DIFFICULTIES[i].getLabel();
        }

        SoundPlayer.playOnTyping(nameField, "typing.wav");
        SoundPlayer.playOnDelete(nameField, "backspace.wav");

        modeBar = new Theme.ChoiceBar(modeLabels);
        difficultyBar = new Theme.ChoiceBar(diffLabels);
        difficultyBar.setOnChange(() -> {
            SoundPlayer.play("selectdifficulty.wav");
            updateHint();
        });
        modeBar.setOnChange(() -> SoundPlayer.play("selectmode.wav"));
        updateHint();

        JPanel box = new JPanel();
        box.setOpaque(false);
        box.setLayout(new BoxLayout(box, BoxLayout.Y_AXIS));

        addCentered(box, Theme.label("New game", Theme.sans(Font.BOLD, 32), Theme.TEXT));
        box.add(Box.createVerticalStrut(24));

        addCentered(box, Theme.label("Your name", Theme.sans(Font.BOLD, 14), Theme.MUTED));
        box.add(Box.createVerticalStrut(6));
        nameField.setMaximumSize(new Dimension(320, 44));
        nameField.setHorizontalAlignment(JTextField.CENTER);
        nameField.addActionListener(e -> start(window));
        addCentered(box, nameField);
        box.add(Box.createVerticalStrut(24));

        addCentered(box, Theme.label("Mode", Theme.sans(Font.BOLD, 14), Theme.MUTED));
        box.add(Box.createVerticalStrut(6));
        addCentered(box, modeBar);
        box.add(Box.createVerticalStrut(24));

        addCentered(box, Theme.label("Difficulty", Theme.sans(Font.BOLD, 14), Theme.MUTED));
        box.add(Box.createVerticalStrut(6));
        addCentered(box, difficultyBar);
        box.add(Box.createVerticalStrut(8));
        addCentered(box, difficultyHint);
        box.add(Box.createVerticalStrut(28));

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 0));
        buttons.setOpaque(false);
        Theme.Btn back = new Theme.Btn("Back", false);
        back.addActionListener(e -> {
            SoundPlayer.play("back.wav");
            window.showMenu();
        });
        Theme.Btn go = new Theme.Btn("Start", true);
        go.addActionListener(e -> start(window));
        buttons.add(back);
        buttons.add(go);
        addCentered(box, buttons);

        
        JPanel centre = new JPanel(new GridBagLayout());
        centre.setOpaque(false);
        centre.add(box);
        add(centre, BorderLayout.CENTER);

        
        JPanel topBar = new JPanel(new FlowLayout(FlowLayout.RIGHT, 16, 12));
        topBar.setOpaque(false);
        topBar.add(new MusicButton());
        add(topBar, BorderLayout.NORTH);
    }

    void onShow() {
        nameField.requestFocusInWindow();
        nameField.selectAll();
    }

    private void start(MainWindow window) {
        SoundPlayer.play("Start.wav");
        String name = nameField.getText().replace(',', ' ').trim();
        if (name.isEmpty()) {
            name = "Player";
        }
        window.startGame(
                name,
                MODES[modeBar.getSelectedIndex()],
                DIFFICULTIES[difficultyBar.getSelectedIndex()]);
    }

    private void updateHint() {
        Difficulty d = DIFFICULTIES[difficultyBar.getSelectedIndex()];
        difficultyHint.setText(d.getBits() + "-bit numbers (0 to " + d.getMaxValue() + ")");
    }

    private void addCentered(JPanel box, JComponent c) {
        c.setAlignmentX(Component.CENTER_ALIGNMENT);
        box.add(c);
    }
}