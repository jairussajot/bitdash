package game;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * One round of BitDash (10 questions). Contains only game logic, with no
 * console or GUI code, so any front end can drive it.
 *
 * Typical use:
 *   GameSession s = new GameSession(name, Mode.BINARY_TO_DECIMAL, Difficulty.EASY);
 *   while (!s.isFinished()) {
 *       show(s.getPrompt());
 *       s.submit(userInput);
 *   }
 *   Score result = s.toScore();
 */
public class GameSession {

    public static final int QUESTIONS = 10;

    /** The label strings match what Game.java already writes to scores.csv. */
    public enum Mode {
        BINARY_TO_DECIMAL("Binary to Decimal", "Convert to decimal"),
        DECIMAL_TO_BINARY("Decimal to Binary", "Convert to binary");

        private final String label;
        private final String instruction;

        Mode(String label, String instruction) {
            this.label = label;
            this.instruction = instruction;
        }

        public String getLabel() { return label; }
        public String getInstruction() { return instruction; }
    }

    public enum Difficulty {
        EASY("Easy", 4),
        MEDIUM("Medium", 6),
        DIFFICULT("Difficult", 8);

        private final String label;
        private final int bits;

        Difficulty(String label, int bits) {
            this.label = label;
            this.bits = bits;
        }

        public String getLabel() { return label; }
        public int getBits() { return bits; }
        public int getMaxValue() { return (1 << bits) - 1; }
    }

    /** A record of one answered question, for the end-of-round review. */
    public static class Attempt {
        public final String prompt;
        public final String given;
        public final String correct;
        public final boolean right;

        Attempt(String prompt, String given, String correct, boolean right) {
            this.prompt = prompt;
            this.given = given;
            this.correct = correct;
            this.right = right;
        }
    }

    private final String name;
    private final Mode mode;
    private final Difficulty difficulty;
    private final List<Integer> problems;
    private final List<Attempt> attempts = new ArrayList<>();

    private int index = 0;
    private int score = 0;
    private final long startTime;
    private long endTime = -1;

    public GameSession(String name, Mode mode, Difficulty difficulty) {
        this.name = name;
        this.mode = mode;
        this.difficulty = difficulty;

        // Same idea as Game.generateProblems: shuffle every value the bit
        // width allows, then take the first 10 so no question repeats.
        List<Integer> all = new ArrayList<>();
        for (int i = 0; i <= difficulty.getMaxValue(); i++) {
            all.add(i);
        }
        Collections.shuffle(all);
        this.problems = new ArrayList<>(all.subList(0, QUESTIONS));

        this.startTime = System.nanoTime();
    }

    // ---- current question -------------------------------------------------

    /** What the player sees: a binary string or a decimal number. */
    public String getPrompt() {
        return promptFor(problems.get(index));
    }

    /** The expected answer for the current question. */
    public String getCorrectAnswer() {
        return answerFor(problems.get(index));
    }

    public String getInstruction() {
        return mode.getInstruction();
    }

    /** 1-based number of the question currently being asked. */
    public int getQuestionNumber() {
        return Math.min(index + 1, QUESTIONS);
    }

    // ---- answering --------------------------------------------------------

    /**
     * Checks the answer for the current question and moves to the next one.
     * Matching rules are the same as the console version: exact string match
     * (so binary answers have no leading zeros), ignoring surrounding spaces.
     *
     * @return true if the answer was correct
     */
    public boolean submit(String answer) {
        if (isFinished()) {
            throw new IllegalStateException("Round already finished");
        }

        String given = answer == null ? "" : answer.trim();
        int value = problems.get(index);
        String correct = answerFor(value);
        boolean right = given.equals(correct);

        if (right) {
            score++;
        }
        attempts.add(new Attempt(promptFor(value), given, correct, right));

        index++;
        if (index >= QUESTIONS) {
            endTime = System.nanoTime();
        }
        return right;
    }

    // ---- state ------------------------------------------------------------

    public boolean isFinished() {
        return index >= QUESTIONS;
    }

    public int getScore() {
        return score;
    }

    public String getName() { return name; }
    public Mode getMode() { return mode; }
    public Difficulty getDifficulty() { return difficulty; }

    public List<Attempt> getAttempts() {
        return Collections.unmodifiableList(attempts);
    }

    /** Seconds since the round began; stops counting once it is finished. */
    public double getElapsedSeconds() {
        long end = isFinished() ? endTime : System.nanoTime();
        return (end - startTime) / 1_000_000_000.0;
    }

    /** The finished round as a Score, ready for Scoreboard.addScore. */
    public Score toScore() {
        return new Score(name, mode.getLabel(), difficulty.getLabel(),
                score, getElapsedSeconds());
    }

    // ---- helpers ----------------------------------------------------------

    private String promptFor(int value) {
        return mode == Mode.BINARY_TO_DECIMAL
                ? Integer.toBinaryString(value)
                : String.valueOf(value);
    }

    private String answerFor(int value) {
        return mode == Mode.BINARY_TO_DECIMAL
                ? String.valueOf(value)
                : Integer.toBinaryString(value);
    }
}
