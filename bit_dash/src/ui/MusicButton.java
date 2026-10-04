package ui;

import javax.swing.border.EmptyBorder;
import java.awt.Dimension;
import java.awt.Font;
import java.util.ArrayList;
import java.util.List;

/** A small button that turns the background music on and off. */
class MusicButton extends Theme.Btn {

    // Every music button that exists (setup screen + game screen), so they all show the same state.
    private static final List<MusicButton> ALL = new ArrayList<>();

    MusicButton() {
        super("", false);
        setFont(Theme.sans(Font.BOLD, 13));
        setBorder(new EmptyBorder(6, 14, 6, 14));
        setPreferredSize(new Dimension(130, 34));
        setFocusable(false);   // clicking it must not steal the cursor from the answer box

        ALL.add(this);
        refreshText();

        addActionListener(e -> {
            SoundPlayer.setMusicMuted(!SoundPlayer.isMusicMuted());
            for (MusicButton b : ALL) {
                b.refreshText();
            }
        });
    }

    private void refreshText() {
        if (SoundPlayer.isMusicMuted()) {
            setText("Music: Off");
            setForeground(Theme.MUTED);
        } else {
            setText("Music: On");
            setForeground(Theme.ACCENT);
        }
    }
}