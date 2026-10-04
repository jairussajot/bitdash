package ui;

import game.Score;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/** Filterable scoreboard. Best score first, ties broken by faster time. */
class ScoreboardPanel extends JPanel {

    private static final String[] MODE_FILTERS = { "All", "Binary to Decimal", "Decimal to Binary" };
    private static final String[] DIFF_FILTERS = { "All", "Easy", "Medium", "Difficult" };
    private static final String[] COLUMNS = { "#", "Name", "Mode", "Difficulty", "Score", "Time" };

    private final Scoreboard scoreboard;
    private final Theme.ChoiceBar modeBar = new Theme.ChoiceBar(MODE_FILTERS);
    private final Theme.ChoiceBar diffBar = new Theme.ChoiceBar(DIFF_FILTERS);

    private final DefaultTableModel model = new DefaultTableModel(COLUMNS, 0) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };
    private final JTable table = new JTable(model);

    private final CardLayout bodyCards = new CardLayout();
    private final JPanel body = new JPanel(bodyCards);

    ScoreboardPanel(MainWindow window, Scoreboard scoreboard) {
        this.scoreboard = scoreboard;
        setBackground(Theme.BG);
        setLayout(new BorderLayout(0, 14));
        setBorder(new EmptyBorder(24, 32, 24, 32));

        // ---- filters ----
        JPanel filters = new JPanel();
        filters.setOpaque(false);
        filters.setLayout(new BoxLayout(filters, BoxLayout.Y_AXIS));
        JLabel title = Theme.label("Scoreboard", Theme.sans(Font.BOLD, 32), Theme.TEXT);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        filters.add(title);
        filters.add(Box.createVerticalStrut(16));
        modeBar.setAlignmentX(Component.CENTER_ALIGNMENT);
        diffBar.setAlignmentX(Component.CENTER_ALIGNMENT);
        filters.add(modeBar);
        filters.add(Box.createVerticalStrut(8));
        filters.add(diffBar);
        add(filters, BorderLayout.NORTH);

        modeBar.setOnChange(this::filterChanged);
        diffBar.setOnChange(this::filterChanged);

        // ---- table ----
        styleTable();
        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.getViewport().setBackground(Theme.PANEL);

        JLabel empty = Theme.label("No scores match those filters.", Theme.sans(Font.PLAIN, 18), Theme.MUTED);
        empty.setHorizontalAlignment(SwingConstants.CENTER);
        JPanel emptyPanel = new JPanel(new BorderLayout());
        emptyPanel.setBackground(Theme.PANEL);
        emptyPanel.add(empty, BorderLayout.CENTER);

        body.add(scroll, "table");
        body.add(emptyPanel, "empty");
        add(body, BorderLayout.CENTER);

        // ---- back ----
        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        buttons.setOpaque(false);
        Theme.Btn back = new Theme.Btn("Back", false);
        back.addActionListener(e -> {
            SoundPlayer.play("back.wav");
            window.showMenu();
        });
        buttons.add(back);
        add(buttons, BorderLayout.SOUTH);
    }

    /** A filter button was clicked: click sound, then update the table. */
    private void filterChanged() {
        SoundPlayer.play("scoreboardfilterbuttons.wav");
        refresh();
    }

    /** Re-reads the scores and applies the current filters. */
    void refresh() {
        String mode = MODE_FILTERS[modeBar.getSelectedIndex()];
        String difficulty = DIFF_FILTERS[diffBar.getSelectedIndex()];

        List<Score> shown = new ArrayList<>();
        for (Score s : scoreboard.getScores()) {
            boolean modeOk = mode.equals("All") || s.mode.equalsIgnoreCase(mode);
            boolean diffOk = difficulty.equals("All") || s.difficulty.equalsIgnoreCase(difficulty);
            if (modeOk && diffOk) {
                shown.add(s);
            }
        }

        shown.sort(Comparator.<Score>comparingInt(s -> -s.score).thenComparingDouble(s -> s.time));

        model.setRowCount(0);
        int rank = 1;
        for (Score s : shown) {
            model.addRow(new Object[] {
                rank++, s.name, s.mode, s.difficulty, s.score + " / " + game.GameSession.QUESTIONS,
                String.format("%.2fs", s.time)
            });
        }

        bodyCards.show(body, shown.isEmpty() ? "empty" : "table");
    }

    private void styleTable() {
        table.setBackground(Theme.PANEL);
        table.setForeground(Theme.TEXT);
        table.setFont(Theme.sans(Font.PLAIN, 15));
        table.setRowHeight(34);
        table.setShowGrid(false);
        table.setIntercellSpacing(new Dimension(0, 0));
        table.setSelectionBackground(Theme.PANEL_HI);
        table.setSelectionForeground(Theme.ACCENT);
        table.setFillsViewportHeight(true);
        table.getTableHeader().setReorderingAllowed(false);
        table.getTableHeader().setResizingAllowed(false);

        table.getColumnModel().getColumn(0).setPreferredWidth(40);
        table.getColumnModel().getColumn(1).setPreferredWidth(160);
        table.getColumnModel().getColumn(2).setPreferredWidth(150);

        DefaultTableCellRenderer cell = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable t, Object value, boolean sel,
                    boolean focus, int row, int col) {
                super.getTableCellRendererComponent(t, value, sel, false, row, col);
                setHorizontalAlignment(col == 1 ? SwingConstants.LEFT : SwingConstants.CENTER);
                setBorder(new EmptyBorder(0, 10, 0, 10));
                if (!sel) {
                    setBackground(row % 2 == 0 ? Theme.PANEL : new Color(0x182040));
                    setForeground(row == 0 && col == 0 ? Theme.ACCENT : Theme.TEXT);
                }
                return this;
            }
        };
        table.setDefaultRenderer(Object.class, cell);

        DefaultTableCellRenderer header = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable t, Object value, boolean sel,
                    boolean focus, int row, int col) {
                super.getTableCellRendererComponent(t, value, false, false, row, col);
                setBackground(Theme.PANEL_HI);
                setForeground(Theme.MUTED);
                setFont(Theme.sans(Font.BOLD, 13));
                setHorizontalAlignment(col == 1 ? SwingConstants.LEFT : SwingConstants.CENTER);
                setBorder(new EmptyBorder(8, 10, 8, 10));
                return this;
            }
        };
        table.getTableHeader().setDefaultRenderer(header);
        table.getTableHeader().setBackground(Theme.PANEL_HI);
    }
}
