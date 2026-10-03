package game;

public class Score {
    public String name;
    public String mode;
    public String difficulty;
    public int score;
    public double time;

    public Score(String name, String mode, String difficulty, int score, double time) {
        this.name = name;
        this.mode = mode;
        this.difficulty = difficulty;
        this.score = score;
        this.time = time;
    }
}