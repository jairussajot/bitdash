package ui;

import game.GameSession;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.AbstractDocument;
import javax.swing.text.DocumentFilter;
import java.awt.*;

/** The question screen. All rules live in GameSession; this only displays them. */
class GamePanel extends JPanel {

    private final MainWindow window;
    private GameSession session;

    private final Timer clock = new Timer(100, e -> updateTime());

    private final JLabel questionLabel = Theme.label(" ", Theme.sans(Font.BOLD, 16), Theme.MUTED);
    private final JLabel scoreLabel = Theme.label(" ", Theme.sans(Font.BOLD, 16), Theme.MUTED);
    private final JLabel timeLabel = Theme.label("0.0s", Theme.mono(Font.BOLD, 18), Theme.ACCENT);
    private final JLabel instructionLabel = Theme.label(" ", Theme.sans(Font.PLAIN, 20), Theme.MUTED);
    private final JLabel promptLabel = Theme.label(" ", Theme.mono(Font.BOLD, 64), Theme.TEXT);
    private final JLabel feedbackLabel = Theme.label(" ", Theme.sans(Font.BOLD, 18), Theme.MUTED);
    private final JProgressBar progress = new JProgressBar(0, GameSession.QUESTIONS);
    private final JTextField input = Theme.field(10);

    GamePanel(MainWindow window) {
        this.window = window;
        setBackground(Theme.BG);
        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(20, 32, 24, 32));

        // ---- top bar: question counter, score, time + progress bar ----
        JPanel bar = new JPanel(new BorderLayout());
        bar.setOpaque(false);
        bar.add(questionLabel, BorderLayout.WEST);
        scoreLabel.setHorizontalAlignment(SwingConstants.CENTER);
        bar.add(scoreLabel, BorderLayout.CENTER);
        bar.add(timeLabel, BorderLayout.EAST);

        progress.setBorderPainted(false);
        progress.setForeground(Theme.ACCENT);
        progress.setBackground(Theme.PANEL);
        progress.setPreferredSize(new Dimension(10, 8));

        JPanel top = new JPanel(new BorderLayout(0, 12));
        top.setOpaque(false);
        top.add(bar, BorderLayout.NORTH);
        top.add(progress, BorderLayout.SOUTH);
        add(top, BorderLayout.NORTH);

        // ---- centre: instruction, big prompt, input, feedback ----
        JPanel centre = new JPanel();
        centre.setOpaque(false);
        centre.setLayout(new BoxLayout(centre, BoxLayout.Y_AXIS));

        input.setFont(Theme.mono(Font.BOLD, 32));
        input.setHorizontalAlignment(JTextField.CENTER);
        input.setMaximumSize(new Dimension(320, 64));
        input.setPreferredSize(new Dimension(320, 64));
        input.addActionListener(e -> submit());
        SoundPlayer.playOnTyping(input, "typing.wav");
        SoundPlayer.playOnDelete(input, "backspace.wav");   

        for (JComponent c : new JComponent[] { instructionLabel, promptLabel, input, feedbackLabel }) {
            c.setAlignmentX(Component.CENTER_ALIGNMENT);
        }

        centre.add(Box.createVerticalGlue());
        centre.add(instructionLabel);
        centre.add(Box.createVerticalStrut(8));
        centre.add(promptLabel);
        centre.add(Box.createVerticalStrut(24));
        centre.add(input);
        centre.add(Box.createVerticalStrut(18));
        centre.add(feedbackLabel);
        centre.add(Box.createVerticalGlue());
        add(centre, BorderLayout.CENTER);

        // ---- bottom buttons ----
        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 0));
        buttons.setOpaque(false);
        Theme.Btn quit = new Theme.Btn("Quit to menu", false);
        quit.addActionListener(e -> {
            SoundPlayer.play("back.wav");
            clock.stop();
            window.showMenu();
        });
        Theme.Btn submit = new Theme.Btn("Submit", true);
        submit.addActionListener(e -> submit());
        buttons.add(quit);
        buttons.add(submit);
        add(buttons, BorderLayout.SOUTH);
    }

    /** Called by MainWindow when a new round starts. */
    void begin(GameSession newSession) {
        clock.stop();
        this.session = newSession;

        // Only allow characters that make sense for this mode.
        boolean binaryAnswer = session.getMode() == GameSession.Mode.DECIMAL_TO_BINARY;
        String allowed = binaryAnswer ? "01" : "0123456789";
        int maxLen = binaryAnswer ? session.getDifficulty().getBits()
                                  : String.valueOf(session.getDifficulty().getMaxValue()).length();
        ((AbstractDocument) input.getDocument()).setDocumentFilter(new LimitFilter(allowed, maxLen));

        feedbackLabel.setText(" ");
        refresh();
        clock.start();
        input.requestFocusInWindow();
    }

    private void refresh() {
        questionLabel.setText("Question " + session.getQuestionNumber() + " / " + GameSession.QUESTIONS);
        scoreLabel.setText("Score " + session.getScore());
        progress.setValue(session.getQuestionNumber() - 1);
        instructionLabel.setText(session.getInstruction());
        promptLabel.setText(session.getPrompt());
        input.setText("");
        input.requestFocusInWindow();
        updateTime();
    }

    private void updateTime() {
        if (session != null) {
            timeLabel.setText(String.format("%.1fs", session.getElapsedSeconds()));
        }
    }

    private void submit() {
        if (session == null || session.isFinished()) {
            return;
        }
        String text = input.getText().trim();
        if (text.isEmpty()) {
            return; // ignore an accidental empty Enter
        }

        String prompt = session.getPrompt();
        String correct = session.getCorrectAnswer();
        boolean ok = session.submit(text);

        // On the last question, afterround.wav plays instead (see MainWindow.finishGame)
        if (!session.isFinished()) {
            SoundPlayer.play(ok ? "correct.wav" : "wrong.wav");
        }

        if (ok) {
            feedbackLabel.setForeground(Theme.GOOD);
            feedbackLabel.setText("✓ Correct");
        } else {
            feedbackLabel.setForeground(Theme.BAD);
            feedbackLabel.setText("✗ Wrong: " + prompt + " = " + correct);
        }

        if (session.isFinished()) {
            clock.stop();
            window.finishGame(session);
        } else {
            refresh();
        }
    }

    /** Restricts typing to a set of characters and a maximum length. */
    private static class LimitFilter extends DocumentFilter {
        private final String allowed;
        private final int maxLen;

        LimitFilter(String allowed, int maxLen) {
            this.allowed = allowed;
            this.maxLen = maxLen;
        }

        @Override
        public void insertString(FilterBypass fb, int offset, String text, AttributeSet attr)
                throws BadLocationException {
            replace(fb, offset, 0, text, attr);
        }

        @Override
        public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs)
                throws BadLocationException {
            if (text == null) {
                super.replace(fb, offset, length, text, attrs);
                return;
            }
            StringBuilder ok = new StringBuilder();
            for (char c : text.toCharArray()) {
                if (allowed.indexOf(c) >= 0) {
                    ok.append(c);
                }
            }
            int newLength = fb.getDocument().getLength() - length + ok.length();
            if (newLength <= maxLen) {
                super.replace(fb, offset, length, ok.toString(), attrs);
            }
        }
    }
}
