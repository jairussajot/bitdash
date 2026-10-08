package ui;

import javax.swing.border.EmptyBorder;
import java.awt.Dimension;
import java.awt.Font;
import java.util.ArrayList;
import java.util.List;


class MusicButton extends Theme.Btn {

    
    private static final List<MusicButton> ALL = new ArrayList<>();

    MusicButton() {
        super("", false);
        setFont(Theme.sans(Font.BOLD, 13));
        setBorder(new EmptyBorder(6, 14, 6, 14));
        setPreferredSize(new Dimension(130, 34));
        setFocusable(false);   

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