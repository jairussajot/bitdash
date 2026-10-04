package ui;

import javax.swing.*;
import java.awt.*;

/** Title screen with Start / Instructions / Scoreboard / Quit. */
class MenuPanel extends JPanel {

    MenuPanel(MainWindow window) {
        setBackground(Theme.BG);
        setLayout(new GridBagLayout());

        JPanel box = new JPanel();
        box.setOpaque(false);
        box.setLayout(new BoxLayout(box, BoxLayout.Y_AXIS));

        JLabel bits = Theme.label("01000010 01001001 01010100", Theme.mono(Font.PLAIN, 14), Theme.MUTED);
        JLabel title = Theme.label("BitDash", Theme.mono(Font.BOLD, 72), Theme.ACCENT);
        JLabel tag = Theme.label("A mental math game for binary and decimal", Theme.sans(Font.PLAIN, 16), Theme.MUTED);

        for (JComponent c : new JComponent[] { bits, title, tag }) {
            c.setAlignmentX(Component.CENTER_ALIGNMENT);
            box.add(c);
        }
        box.add(Box.createVerticalStrut(36));

        addButton(box, "Start", true, "menubuttons.wav", window::showSetup);
        addButton(box, "Instructions", false, "menubuttons.wav", window::showInstructions);
        addButton(box, "Scoreboard", false, "menubuttons.wav", window::showScoreboard);
        addButton(box, "Quit", false, "quit.wav", window::quit);

        add(box);
    }

    private void addButton(JPanel box, String text, boolean primary, String sound, Runnable action) {
        Theme.Btn b = new Theme.Btn(text, primary);
        b.setAlignmentX(Component.CENTER_ALIGNMENT);
        b.setMaximumSize(new Dimension(260, 48));
        b.setPreferredSize(new Dimension(260, 48));
        b.addActionListener(e -> {
            SoundPlayer.play(sound);   // play this button's sound first
            action.run();
        });
        box.add(b);
        box.add(Box.createVerticalStrut(12));
    }
}