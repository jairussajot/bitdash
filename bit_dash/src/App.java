import java.util.Scanner;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.io.FileWriter;
import java.io.File;
import java.io.IOException;

public class App {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        Menu menu = new Menu(scanner);
        menu.show();

        scanner.close();
    }
}

class Menu {
    private Scanner scanner;
    private Game game;
    private Scoreboard scoreboard;

    public Menu(Scanner scanner) {
        this.scanner = scanner;
        scoreboard = new Scoreboard();
        this.game = new Game(scanner);
    }

    public void show() {

        while (true) {

            System.out.println("\n==BitDash==");
            System.out.println("Start");
            System.out.println("Instruction");
            System.out.println("Scoreboard");
            System.out.println("Quit");
            System.out.print("Please select an option: ");

            String menu_select = scanner.nextLine();

            if (menu_select.equalsIgnoreCase("Start")) {

                System.out.print("Enter your name: ");
                String name = scanner.nextLine();

                Score result = game.selectMode(name);

                if (result != null) {
                    scoreboard.addScore(result);
                }


            } else if (menu_select.equalsIgnoreCase("Instruction")) {

                System.out.println("Just answer chud.");

            } else if (menu_select.equalsIgnoreCase("Scoreboard")) {

                scoreboard.show();

            } else if (menu_select.equalsIgnoreCase("Quit")) {

                System.out.println("Bye");
                break;

            } else {

                System.out.println("Not valid input, try again.");
            }
        }
    }
    
}



class Game {

    private Scanner scanner;

    public Game(Scanner scanner) {
        this.scanner = scanner;
    }

    public Score selectMode(String name) {
        System.out.println("---Select Mode---");
        System.out.println("+++Binary to Decimal (Enter 1)+++");
        System.out.println("+++Decimal to Binary (Enter 2)+++");
        System.out.print("Select mode: ");
        String mode = scanner.nextLine();

        System.out.println("\n-Select Difficulty-");
        System.out.println("Easy");
        System.out.println("Medium");
        System.out.println("Difficult");
        System.out.print("Enter Difficulty: ");

        String slct_dff = scanner.nextLine();

        int bits = selectDifficulty(slct_dff);

        if (bits == 0) {
            System.out.println("Invalid difficulty.");
            return null;
        } 


        if (mode.equals("1")) {
            return binarytoDecimal(name,bits);
        } else if (mode.equals("2")) {
            return decimaltoBinary(name,bits);
        } else {
            System.out.println("Invalid mode.");
            return null;
        }
    }
    private int selectDifficulty(String dfclty) {
        if (dfclty.equalsIgnoreCase("Easy")) {
            return 4;
        } else if (dfclty.equalsIgnoreCase("Medium")) {
            return 6;
        } else if (dfclty.equalsIgnoreCase("Difficult")) {
            return 8;
        } else {
            return 0;
        }
    }

    public List<Integer> generateProblems(int bits) {

        List<Integer> numbers = new ArrayList<>();

        int max = (1 << bits) - 1;

        for (int i = 0; i <= max; i++) {
            numbers.add(i);
        }

        Collections.shuffle(numbers);

        return numbers;
    }


    private Score binarytoDecimal(String name, int bits) {
        int count = 0;
        int score = 0;

        List<Integer> problems = generateProblems(bits);

        long startTime = System.nanoTime();

        while (count < 10) {

            int decimal = problems.get(count);
            String binary = Integer.toBinaryString(decimal);

            System.out.println("Convert to decimal: " + binary);
            System.out.print("Enter your Answer: ");
            String answer = scanner.nextLine();

            if (answer.equals(String.valueOf(decimal))) {
                System.out.println("----Correct----");
                score++;
            } else {
                System.out.println("----Wrong----");
                System.out.println("Correct Answer: " + decimal);
                System.out.println("-------------");
            }

            count++;
        }
        
        double time = timeElapsed(startTime);

        System.out.println("+++++++++++++++++");
        System.out.printf("Time: %.2f seconds%n", time);
        System.out.println("Score: " + score);
        System.out.println("+++++++++++++++++");

        return new Score(name, score, time);
    }

    private Score decimaltoBinary(String name, int bits) {

        int count = 0;
        int score = 0;

        List<Integer> problems = generateProblems(bits);

        long startTime = System.nanoTime();

        while (count < 10) {

            int randomDecimal = problems.get(count);

            String binaryStr = Integer.toBinaryString(randomDecimal);

            System.out.println("Convert to binary: " + randomDecimal);
            System.out.print("Enter your Answer: ");
            String binaryAnswer = scanner.nextLine();

            if (binaryAnswer.equals(binaryStr)) {

                System.out.println("----Correct----");
                score += 1;

            } else {

                System.out.println("----Wrong----");
                System.out.println("Correct Answer: " + binaryStr);
                System.out.println("-------------");
            }

            count++;
        }

        double time = timeElapsed(startTime);

        System.out.println("+++++++++++++++++");
        System.out.printf("Time: %.2f seconds%n", time);
        System.out.println("Score: " + score);
        System.out.println("+++++++++++++++++");

        return new Score(name, score, time);
    }


    private double timeElapsed(long startTime) {
        long endTime = System.nanoTime();

        return (endTime - startTime) / 1_000_000_000.0;
    }

}

class Score {

    String name;
    int score;
    double time;

    public Score(String name, int score, double time) {
        this.name = name;
        this.score = score;
        this.time = time;
    }
}


class Scoreboard {

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
            System.out.println("Error saving score.");
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
            System.out.println("Error loading scores.");
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
