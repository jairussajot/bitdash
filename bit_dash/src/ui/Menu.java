package ui;

import java.util.Scanner;

import game.Game;
import game.Score;

public class Menu {

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

                showScoreboardMenu();

            } else if (menu_select.equalsIgnoreCase("Quit")) {

                System.out.println("Bye");
                break;

            } else {

                System.out.println("Not valid input, try again.");
            }
        }
    }

    private void showScoreboardMenu() {

        System.out.println("\n===== SCOREBOARD FILTER =====");

        System.out.println("\nSelect Mode:");
        System.out.println("1. All");
        System.out.println("2. Binary to Decimal");
        System.out.println("3. Decimal to Binary");
        System.out.print("Enter choice: ");

        String modeChoice = scanner.nextLine();

        String selectedMode;

        if (modeChoice.equals("1")) {

            selectedMode = "All";

        } else if (modeChoice.equals("2")) {

            selectedMode = "Binary to Decimal";

        } else if (modeChoice.equals("3")) {

            selectedMode = "Decimal to Binary";

        } else {

            System.out.println("Invalid choice.");
            return;
        }

        System.out.println("\nSelect Difficulty:");
        System.out.println("1. All");
        System.out.println("2. Easy");
        System.out.println("3. Medium");
        System.out.println("4. Difficult");
        System.out.print("Enter choice: ");

        String difficultyChoice = scanner.nextLine();

        String selectedDifficulty;

        if (difficultyChoice.equals("1")) {

            selectedDifficulty = "All";

        } else if (difficultyChoice.equals("2")) {

            selectedDifficulty = "Easy";

        } else if (difficultyChoice.equals("3")) {

            selectedDifficulty = "Medium";

        } else if (difficultyChoice.equals("4")) {

            selectedDifficulty = "Difficult";

        } else {

            System.out.println("Invalid choice.");
            return;
        }

        scoreboard.show(selectedMode, selectedDifficulty);
    }
}