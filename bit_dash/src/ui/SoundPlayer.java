package ui;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.HashMap;
import java.util.Map;


public class SoundPlayer {

    private static final String FOLDER = "sfx/";

    
    private static final Map<String, Clip> CLIPS = new HashMap<>();

    private static Clip music;                  
    private static boolean musicMuted = false;  


    public static void playOnDelete(JTextField field, String fileName) {
        loadClip(fileName);   
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


    public static void playOnTyping(JTextField field, String fileName) {
        loadClip(fileName);   
        field.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                play(fileName);   
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                
            }
        });
    }

    public static void startMusic(String fileName) {
        stopMusic();   
        try {
            music = AudioSystem.getClip();
            try (AudioInputStream in = AudioSystem.getAudioInputStream(new File(FOLDER + fileName))) {
                music.open(in);
            }
            try {
                FloatControl volume = (FloatControl) music.getControl(FloatControl.Type.MASTER_GAIN);
                volume.setValue(-10.0f);
            } catch (Exception e) {
            }
            if (!musicMuted) {
                music.loop(Clip.LOOP_CONTINUOUSLY);
            }
        } catch (Exception e) {
            music = null;   
            e.printStackTrace();   
        }
    }

    public static void stopMusic() {
        if (music != null) {
            music.stop();
            music.close();
            music = null;
        }
    }

    public static void setMusicMuted(boolean muted) {
        musicMuted = muted;
        if (music == null) {
            return;   
        }
        if (muted) {
            music.stop();                          
        } else {
            music.loop(Clip.LOOP_CONTINUOUSLY);    
        }
    }

    public static boolean isMusicMuted() {
        return musicMuted;
    }

    public static synchronized void play(String fileName) {
        Clip clip = loadClip(fileName);
        if (clip == null) {
            return;   
        }
        clip.stop();               
        clip.setFramePosition(0);   
        clip.start();
    }

    
    private static synchronized Clip loadClip(String fileName) {
        if (CLIPS.containsKey(fileName)) {
            return CLIPS.get(fileName);   
        }
        Clip clip = null;
        try {
            clip = AudioSystem.getClip();
            try (AudioInputStream in = AudioSystem.getAudioInputStream(new File(FOLDER + fileName))) {
                clip.open(in);
            }
        } catch (Exception e) {
            clip = null;
            System.out.println("Could not load sound " + fileName + ": " + e);   
        }
        CLIPS.put(fileName, clip);
        return clip;
    }
}