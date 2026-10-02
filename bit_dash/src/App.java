import java.util.Scanner;
import java.util.Random;

public class App {
    public static void main(String[] args) throws Exception {
        Scanner scanner = new Scanner(System.in);
        Menu menu = new Menu();
        Game game = new Game();

        menu.show();


        String menu_select = scanner.nextLine();

        if (menu_select.equals("Start")) {
            game.generateEasyProblem();
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

class Menu{
    public void show(){
        System.out.println("==BitDash==");
        System.out.println("Start");
        System.out.println("Instruction");
        System.out.println("Scoreboard");
        System.out.println("Quit");
        System.out.print("Please select and option: ");
    }

}

class Game{
    public void generateEasyProblem(){
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
        
    }

}