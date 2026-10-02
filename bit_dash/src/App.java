import java.util.Scanner;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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

    public Menu(Scanner scanner) {
        this.scanner = scanner;
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

                System.out.println("\n-Select Difficulty-");
                System.out.println("Easy");
                System.out.println("Medium");
                System.out.println("Difficult");
                System.out.print("Enter Difficulty: ");

                String slct_dff = scanner.nextLine();

                int bits = selectDifficulty(slct_dff);

                if (bits == 0) {
                    System.out.println("Invalid difficulty.");
                } else {
                    game.selectMode(bits);
                }

            } else if (menu_select.equalsIgnoreCase("Instruction")) {

                System.out.println("Just answer chud.");

            } else if (menu_select.equalsIgnoreCase("Scoreboard")) {

                System.out.println("No scoreboard yet.");

            } else if (menu_select.equalsIgnoreCase("Quit")) {

                System.out.println("Bye");
                break;

            } else {

                System.out.println("Not valid input, try again.");
            }
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
}


class Game {

    private Scanner scanner;

    public Game(Scanner scanner) {
        this.scanner = scanner;
    }

    public void selectMode(int bits) {
        System.out.println("---Select Mode---");
        System.out.println("+++Binary to Decimal+++");
        System.out.println("+++Decimal to Binary+++");
        System.out.print("Select mode: ");
        String mode = scanner.nextLine();

        if (mode.equals("1")) {
            binarytoDecimal(bits);
        } else if (mode.equals("2")) {
            decimaltoBinary(bits);
        } else {
            System.out.println("Invalid mode.");
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


    private void binarytoDecimal(int bits) {
        int count = 0;
        int score = 0;

        List<Integer> problems = generateProblems(bits);

        long startTime = System.nanoTime();

        while (count < 10) {

            int decimal = problems.get(count);
            String binary = Integer.toBinaryString(decimal);

            System.out.println("Convert to decimal: " + binary);

            String answer = scanner.nextLine();

            if (answer.equals(String.valueOf(decimal))) {
                System.out.println("Correct");
                score++;
            } else {
                System.out.println("Wrong");
                System.out.println("Answer: " + decimal);
            }

            count++;
        }
        
        double time = timeElapsed(startTime);

        System.out.println("+++++++++++++++++");
        System.out.printf("Time: %.2f seconds%n", time);
        System.out.println("Score: " + score);
    }

    private void decimaltoBinary(int bits) {

        int count = 0;
        int score = 0;

        List<Integer> problems = generateProblems(bits);

        long startTime = System.nanoTime();

        while (count < 10) {

            int randomDecimal = problems.get(count);

            String binaryStr = Integer.toBinaryString(randomDecimal);

            System.out.println("Convert to binary: " + randomDecimal);

            String binaryAnswer = scanner.nextLine();

            if (binaryAnswer.equals(binaryStr)) {

                System.out.println("Correct");
                score += 1;

            } else {

                System.out.println("Wrong");
                System.out.println("Answer: " + binaryStr);
            }

            count++;
        }

        double time = timeElapsed(startTime);

        System.out.println("+++++++++++++++++");
        System.out.printf("Time: %.2f seconds%n", time);
        System.out.println("Score: " + score);
    }


    private double timeElapsed(long startTime) {
        long endTime = System.nanoTime();

        return (endTime - startTime) / 1_000_000_000.0;
    }
}