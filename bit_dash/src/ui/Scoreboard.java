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

    private void saveScore(Score score) {
        try {
            FileWriter writer = new FileWriter("scores.csv", true);

            writer.write(
                score.name + "," +
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
                String[] data = line.split(",");

                String name = data[0];
                int score = Integer.parseInt(data[1]);
                double time = Double.parseDouble(data[2]);

                scores.add(new Score(name, score, time));
            }

            reader.close();

        } catch (IOException e) {
            System.out.println("Could not load scores.");
        }
    }

    public void show() {
        if (scores.isEmpty()) {
            System.out.println("No scores yet.");
            return;
        }

        System.out.println("\n===== SCOREBOARD =====");

        for (Score score : scores) {
            System.out.println(
                score.name + " - " +
                score.score + " - " +
                score.time + "s"
            );
        }
    }
}