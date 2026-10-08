package ui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;


final class Theme {

    private Theme() {}

    static final Color BG        = new Color(0x0B1020);
    static final Color PANEL     = new Color(0x141B34);
    static final Color PANEL_HI  = new Color(0x1F2850);
    static final Color TEXT      = new Color(0xE8ECFF);
    static final Color MUTED     = new Color(0x8D97C2);
    static final Color ACCENT    = new Color(0x38E8C6);
    static final Color GOOD      = new Color(0x4ADE80);
    static final Color BAD       = new Color(0xFF6B81);

    static Font sans(int style, int size) {
        return new Font(Font.SANS_SERIF, style, size);
    }

    static Font mono(int style, int size) {
        return new Font(Font.MONOSPACED, style, size);
    }

    static JLabel label(String text, Font font, Color color) {
        JLabel l = new JLabel(text);
        l.setFont(font);
        l.setForeground(color);
        return l;
    }

    static JTextField field(int columns) {
        JTextField f = new JTextField(columns);
        f.setFont(sans(Font.PLAIN, 18));
        f.setBackground(PANEL_HI);
        f.setForeground(TEXT);
        f.setCaretColor(ACCENT);
        f.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(PANEL_HI.brighter(), 1),
                new EmptyBorder(8, 12, 8, 12)));
        return f;
    }

    static class Btn extends JButton {
        private final boolean primary;

        Btn(String text, boolean primary) {
            super(text);
            this.primary = primary;
            setFont(sans(Font.BOLD, 16));
            setForeground(primary ? BG : TEXT);
            setFocusPainted(false);
            setBorderPainted(false);
            setContentAreaFilled(false);
            setOpaque(false);
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            setBorder(new EmptyBorder(12, 28, 12, 28));
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            Color base = primary ? ACCENT : PANEL_HI;
            if (!isEnabled()) {
                base = PANEL;
            } else if (getModel().isPressed()) {
                base = base.darker();
            } else if (getModel().isRollover()) {
                base = primary ? base.brighter() : new Color(0x2A3566);
            }

            g2.setColor(base);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 14, 14);
            g2.dispose();
            super.paintComponent(g);
        }
    }


    static class ChoiceBar extends JPanel {
        private final List<JToggleButton> buttons = new ArrayList<>();
        private Runnable onChange = () -> {};

        ChoiceBar(String... labels) {
            setOpaque(false);
            setLayout(new FlowLayout(FlowLayout.CENTER, 8, 0));
            ButtonGroup group = new ButtonGroup();

            for (int i = 0; i < labels.length; i++) {
                JToggleButton b = new JToggleButton(labels[i]) {
                    @Override
                    protected void paintComponent(Graphics g) {
                        Graphics2D g2 = (Graphics2D) g.create();
                        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                                RenderingHints.VALUE_ANTIALIAS_ON);
                        boolean on = isSelected();
                        Color c = on ? ACCENT
                                : getModel().isRollover() ? new Color(0x2A3566) : PANEL_HI;
                        g2.setColor(c);
                        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
                        g2.dispose();
                        setForeground(on ? BG : TEXT);
                        super.paintComponent(g);
                    }
                };
                b.setFont(sans(Font.BOLD, 14));
                b.setFocusPainted(false);
                b.setBorderPainted(false);
                b.setContentAreaFilled(false);
                b.setOpaque(false);
                b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                b.setBorder(new EmptyBorder(10, 20, 10, 20));
                b.addActionListener(e -> onChange.run());
                if (i == 0) {
                    b.setSelected(true);
                }
                group.add(b);
                buttons.add(b);
                add(b);
            }
        }

        int getSelectedIndex() {
            for (int i = 0; i < buttons.size(); i++) {
                if (buttons.get(i).isSelected()) {
                    return i;
                }
            }
            return 0;
        }

        void setOnChange(Runnable r) {
            this.onChange = r;
        }
    }
}
