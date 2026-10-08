package game;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class GameSession {

    public static final int QUESTIONS = 10;


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


        List<Integer> all = new ArrayList<>();
        for (int i = 0; i <= difficulty.getMaxValue(); i++) {
            all.add(i);
        }
        Collections.shuffle(all);
        this.problems = new ArrayList<>(all.subList(0, QUESTIONS));

        this.startTime = System.nanoTime();
    }


    public String getPrompt() {
        return promptFor(problems.get(index));
    }


    public String getCorrectAnswer() {
        return answerFor(problems.get(index));
    }

    public String getInstruction() {
        return mode.getInstruction();
    }


    public int getQuestionNumber() {
        return Math.min(index + 1, QUESTIONS);
    }


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


    public double getElapsedSeconds() {
        long end = isFinished() ? endTime : System.nanoTime();
        return (end - startTime) / 1_000_000_000.0;
    }


    public Score toScore() {
        return new Score(name, mode.getLabel(), difficulty.getLabel(),
                score, getElapsedSeconds());
    }



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
