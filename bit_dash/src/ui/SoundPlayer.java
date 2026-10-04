package ui;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineEvent;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.io.File;

/**
 * Plays short sound effects from the "sfx" folder.
 * Usage:  SoundPlayer.play("correct.wav");
 *
 * If a file is missing or the computer has no sound device, nothing happens
 * (the game keeps working, just silently).
 */
public class SoundPlayer {

    private static final String FOLDER = "sfx/";

        /** Plays a sound when Backspace or Delete is pressed while the field has text. */
    public static void playOnDelete(JTextField field, String fileName) {
        field.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                boolean deleteKey = e.getKeyCode() == KeyEvent.VK_BACK_SPACE
                                 || e.getKeyCode() == KeyEvent.VK_DELETE;
                if (deleteKey && !field.getText().isEmpty()) {
                    play(fileName);
                }
            }
        });
    }

    /** Plays a sound every time a character is typed (or pasted) into the field. */
    public static void playOnTyping(JTextField field, String fileName) {
        field.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                play(fileName);   // text was added
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                // text was removed (backspace, or the game clearing the box): stay silent
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                // style changes, not used by plain text fields
            }
        });
    }

    public static void play(String fileName) {
        try {
            Clip clip = AudioSystem.getClip();
            try (AudioInputStream in = AudioSystem.getAudioInputStream(new File(FOLDER + fileName))) {
                clip.open(in);
            }
            // free the memory once the sound has finished playing
            clip.addLineListener(e -> {
                if (e.getType() == LineEvent.Type.STOP) {
                    clip.close();
                }
            });
            clip.start();
        } catch (Exception e) {
            // missing file / no audio device: ignore
        }
    }
}