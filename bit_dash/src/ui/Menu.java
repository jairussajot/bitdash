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