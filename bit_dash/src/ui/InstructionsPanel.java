package ui;

import javax.swing.*;
import java.awt.*;

/** Short explanation of how to play. */
class InstructionsPanel extends JPanel {

    InstructionsPanel(MainWindow window) {
        setBackground(Theme.BG);
        setLayout(new GridBagLayout());

        JPanel box = new JPanel();
        box.setOpaque(false);
        box.setLayout(new BoxLayout(box, BoxLayout.Y_AXIS));

        JLabel title = Theme.label("How to play", Theme.sans(Font.BOLD, 32), Theme.TEXT);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        box.add(title);
        box.add(Box.createVerticalStrut(20));

        String[] lines = {
            "Each round has 10 questions, and the clock runs the whole time.",
            "Binary to Decimal: you see a binary number, type its decimal value.",
            "Decimal to Binary: you see a decimal number, type it in binary.",
            "Type binary answers without leading zeros (13 is 1101, not 01101).",
            "Press Enter to submit. Wrong answers show the correct one.",
            "Easy uses 4 bits (0-15), Medium 6 bits (0-63), Difficult 8 bits (0-255).",
            "Your score and time are saved to the scoreboard."
        };

        for (String line : lines) {
            JLabel l = Theme.label(line, Theme.sans(Font.PLAIN, 16), Theme.MUTED);
            l.setAlignmentX(Component.CENTER_ALIGNMENT);
            box.add(l);
            box.add(Box.createVerticalStrut(10));
        }

        box.add(Box.createVerticalStrut(20));
        Theme.Btn back = new Theme.Btn("Back", false);
        back.setAlignmentX(Component.CENTER_ALIGNMENT);
        back.addActionListener(e -> window.showMenu());
        box.add(back);

        add(box);
    }
}
