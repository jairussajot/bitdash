import java.util.Scanner;
import java.util.Random;

public class App {
    public static void main(String[] args) throws Exception {
        Menu menu = new Menu();
        menu.show();

    }
}

class Menu{
    public void show(){
        Scanner scanner = new Scanner(System.in);
        Game game = new Game();

        System.out.println("==BitDash==");
        System.out.println("Start");
        System.out.println("Instruction");
        System.out.println("Scoreboard");
        System.out.println("Quit");
        System.out.print("Please select and option: ");
        String menu_select = scanner.nextLine();

                if (menu_select.equals("Start")) {
            System.out.println("-Select Difficulty- ");
            System.out.println("Easy");
            System.out.println("Medium");
            System.out.println("Difficult");
            System.out.print("Enter Difficulty: ");
            String slct_dff = scanner.nextLine();
            if (slct_dff.equals("Easy")) {
                game.generateEasyProblem();
            }else if (slct_dff.equals("Medium")) {
                game.generateMediumProblem();
            }
        }else if (menu_select.equals("Instruction")) {
            System.out.println("Just answer chud");
        }else if (menu_select.equals("Scoreboard")) {
            System.out.println("No scoreboard yet");
        }else if (menu_select.equals("Quit")) {
            System.out.println("Bye");
            System.exit(0);
        }else{
            System.out.println("Not valid input");
        }
        scanner.close();
    }

}

class Game{
    public void generateEasyProblem(){
        Menu menu = new Menu();
        Random rand = new Random();
        Scanner scanner = new Scanner(System.in);
        int count = 0;
        int score = 0;

        while (count < 10) {
            int randomDecimal = rand.nextInt(16);
            String binaryStr = Integer.toBinaryString(randomDecimal);
            System.out.println("Convert to binary: "+ randomDecimal);
            String binaryAnswer = scanner.nextLine();

            if (binaryAnswer.equals(binaryStr)) {
                System.out.println("Correct");
                score += 1;
            }else{
                System.out.println("Wrong");
                System.out.println(binaryStr);
            }
            
            count += 1;
        }
        
        System.out.println("+++++++++++++++++");
        System.out.println("Score: " + score);
        menu.show();
    }

    public void generateMediumProblem(){
        Menu menu = new Menu();
        Random rand = new Random();
        Scanner scanner = new Scanner(System.in);
        int count = 0;
        int score = 0;

        while (count < 10) {
            int randomDecimal = rand.nextInt(64);
            String binaryStr = Integer.toBinaryString(randomDecimal);
            System.out.println("Convert to binary: "+ randomDecimal);
            String binaryAnswer = scanner.nextLine();

            if (binaryAnswer.equals(binaryStr)) {
                System.out.println("Correct");
                score += 1;
            }else{
                System.out.println("Wrong");
                System.out.println(binaryStr);
            }
            
            count += 1;
        }
        
        System.out.println("+++++++++++++++++");
        System.out.println("Score: " + score);
        menu.show();
    }
    public void generateDifficultProblem(){
        Menu menu = new Menu();
        Random rand = new Random();
        Scanner scanner = new Scanner(System.in);
        int count = 0;
        int score = 0;

        while (count < 10) {
            int randomDecimal = rand.nextInt(256);
            String binaryStr = Integer.toBinaryString(randomDecimal);
            System.out.println("Convert to binary: "+ randomDecimal);
            String binaryAnswer = scanner.nextLine();

            if (binaryAnswer.equals(binaryStr)) {
                System.out.println("Correct");
                score += 1;
            }else{
                System.out.println("Wrong");
                System.out.println(binaryStr);
            }
            
            count += 1;
        }
        
        System.out.println("+++++++++++++++++");
        System.out.println("Score: " + score);
        menu.show();
    }
}