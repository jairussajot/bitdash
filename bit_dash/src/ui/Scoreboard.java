package ui;

import java.util.ArrayList;
import java.util.List;
import java.io.FileWriter;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;

import game.Score;

public class Scoreboard {

    private List<Score> scores;

    public Scoreboard() {
        scores = new ArrayList<>();
        loadScores();
    }

    public void addScore(Score score) {

        scores.add(score);
        saveScore(score);
    }

    // Used by the GUI scoreboard screen
    public List<Score> getScores() {
        return java.util.Collections.unmodifiableList(scores);
    }

    private void saveScore(Score score) {

        try {

            FileWriter writer = new FileWriter("scores.csv", true);

            writer.write(
                score.name + "," +
                score.mode + "," +
                score.difficulty + "," +
                score.score + "," +
                score.time + "\n"
            );

            writer.close();

        } catch (IOException e) {

            System.out.println("Could not save score.");
        }
    }

    private void loadScores() {

        File file = new File("scores.csv");

        if (!file.exists()) {
            return;
        }

        try {

            Scanner reader = new Scanner(file);

            while (reader.hasNextLine()) {

                String line = reader.nextLine();

                // Skip blank or malformed lines instead of crashing.
                if (line.isBlank()) {
                    continue;
                }

                String[] data = line.split(",");

                if (data.length < 5) {
                    continue;
                }

                try {
                    String name = data[0];
                    String mode = data[1];
                    String difficulty = data[2];

                    int score = Integer.parseInt(data[3]);
                    double time = Double.parseDouble(data[4]);

                    scores.add(
                        new Score(
                            name,
                            mode,
                            difficulty,
                            score,
                            time
                        )
                    );
                } catch (NumberFormatException e) {
                    // bad number in this line, ignore it
                }
            }

            reader.close();

        } catch (IOException e) {

            System.out.println("Could not load scores.");
        }
    }

    // Show every score
    public void show() {

        if (scores.isEmpty()) {
            System.out.println("No scores yet.");
            return;
        }

        System.out.println("\n===== SCOREBOARD =====");

        for (Score score : scores) {

            System.out.println(
                score.name + " - " +
                score.mode + " - " +
                score.difficulty + " - " +
                score.score + " - " +
                score.time + "s"
            );
        }
    }

    // Show scores matching mode and difficulty
    public void show(String selectedMode, String selectedDifficulty) {

        boolean found = false;

        System.out.println("\n===== SCOREBOARD =====");

        for (Score score : scores) {

            boolean modeMatches =
                selectedMode.equals("All") ||
                score.mode.equalsIgnoreCase(selectedMode);

            boolean difficultyMatches =
                selectedDifficulty.equals("All") ||
                score.difficulty.equalsIgnoreCase(selectedDifficulty);

            if (modeMatches && difficultyMatches) {

                System.out.println(
                    score.name + " - " +
                    score.mode + " - " +
                    score.difficulty + " - " +
                    score.score + " - " +
                    score.time + "s"
                );

                found = true;
            }
        }

        if (!found) {
            System.out.println("No scores match those filters.");
        }
    }
}