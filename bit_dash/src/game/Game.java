package game;

import java.util.Scanner;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Game {
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

        String difficulty = scanner.nextLine();

        int bits = selectDifficulty(difficulty);

        if (bits == 0) {
            System.out.println("Invalid difficulty.");
            return null;
        }

        if (mode.equals("1")) {

            return binarytoDecimal(
                name,
                "Binary to Decimal",
                difficulty,
                bits
            );

        } else if (mode.equals("2")) {

            return decimaltoBinary(
                name,
                "Decimal to Binary",
                difficulty,
                bits
            );

        } else {

            System.out.println("Invalid mode.");
            return null;
        }
    }

    private int selectDifficulty(String difficulty) {

        if (difficulty.equalsIgnoreCase("Easy")) {
            return 4;

        } else if (difficulty.equalsIgnoreCase("Medium")) {
            return 6;

        } else if (difficulty.equalsIgnoreCase("Difficult")) {
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

    private Score binarytoDecimal(
        String name,
        String mode,
        String difficulty,
        int bits
    ) {

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

        return new Score(
            name,
            mode,
            difficulty,
            score,
            time
        );
    }

    private Score decimaltoBinary(
        String name,
        String mode,
        String difficulty,
        int bits
    ) {

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
                score++;

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
        System.out.println("+++++++++++++++++");
        System.out.println("Score: " + score);

        return new Score(
            name,
            mode,
            difficulty,
            score,
            time
        );
    }

    private double timeElapsed(long startTime) {

        long endTime = System.nanoTime();

        return (endTime - startTime) / 1_000_000_000.0;
    }
}