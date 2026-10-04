package ui;

import game.GameSession;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/** Shown after the 10th question: score, time and a per-question review. */
class ResultPanel extends JPanel {

    private final JLabel headline = Theme.label(" ", Theme.sans(Font.BOLD, 20), Theme.MUTED);
    private final JLabel scoreLabel = Theme.label(" ", Theme.mono(Font.BOLD, 72), Theme.ACCENT);
    private final JLabel detail = Theme.label(" ", Theme.sans(Font.PLAIN, 16), Theme.MUTED);
    private final JPanel reviewList = new JPanel();

    ResultPanel(MainWindow window) {
        setBackground(Theme.BG);
        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(28, 32, 24, 32));

        JPanel top = new JPanel();
        top.setOpaque(false);
        top.setLayout(new BoxLayout(top, BoxLayout.Y_AXIS));
        for (JComponent c : new JComponent[] { headline, scoreLabel, detail }) {
            c.setAlignmentX(Component.CENTER_ALIGNMENT);
            top.add(c);
        }
        top.add(Box.createVerticalStrut(16));
        add(top, BorderLayout.NORTH);

        reviewList.setLayout(new BoxLayout(reviewList, BoxLayout.Y_AXIS));
        reviewList.setBackground(Theme.PANEL);
        reviewList.setBorder(new EmptyBorder(10, 16, 10, 16));

        JScrollPane scroll = new JScrollPane(reviewList);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.getViewport().setBackground(Theme.PANEL);
        scroll.getVerticalScrollBar().setUnitIncrement(14);
        add(scroll, BorderLayout.CENTER);

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 0));
        buttons.setOpaque(false);
        buttons.setBorder(new EmptyBorder(16, 0, 0, 0));
        Theme.Btn again = new Theme.Btn("Play again", true);
        again.addActionListener(e -> {
            SoundPlayer.play("menubuttons.wav");
            window.showSetup();
        });
        Theme.Btn board = new Theme.Btn("Scoreboard", false);
        board.addActionListener(e -> {
            SoundPlayer.play("menubuttons.wav");
            window.showScoreboard();
        });
        Theme.Btn menu = new Theme.Btn("Menu", false);
        menu.addActionListener(e -> {
            SoundPlayer.play("back.wav");
            window.showMenu();
        });
        buttons.add(again);
        buttons.add(board);
        buttons.add(menu);
        add(buttons, BorderLayout.SOUTH);
    }

    void show(GameSession s) {
        int total = GameSession.QUESTIONS;
        headline.setText(s.getScore() == total ? "Perfect round!" : "Round complete");
        scoreLabel.setText(s.getScore() + " / " + total);
        detail.setText(String.format("%s  ·  %s  ·  %.2f seconds  ·  saved as %s",
                s.getMode().getLabel(), s.getDifficulty().getLabel(),
                s.getElapsedSeconds(), s.getName()));

        reviewList.removeAll();
        int n = 1;
        for (GameSession.Attempt a : s.getAttempts()) {
            String text;
            if (a.right) {
                text = "✓  " + String.format("%2d", n) + ".  " + a.prompt + " = " + a.correct;
            } else {
                String yours = a.given.isEmpty() ? "(blank)" : a.given;
                text = "✗  " + String.format("%2d", n) + ".  " + a.prompt + " = " + a.correct + "   (you answered " + yours + ")";
            }
            JLabel row = Theme.label(text, Theme.mono(Font.PLAIN, 15), a.right ? Theme.GOOD : Theme.BAD);
            row.setBorder(new EmptyBorder(3, 0, 3, 0));
            reviewList.add(row);
            n++;
        }
        reviewList.revalidate();
        reviewList.repaint();
    }
}
