import java.util.Scanner;
import java.util.Random;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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
    public void selectMode(){
        System.out.println("---Select Mode---");
        System.out.println("+++Bimary to Decimal+++");
        System.out.println("+++Decimal to Binary+++");
    }
    private void selectDifficulty(String dfclty){
        int bits = 0;
        
        if (dfclty.equals("easy")) {
            bits = 4;
        }else if (dfclty.equals("medium")) {
            bits = 6;
        }else if (dfclty.equals("difficult")) {
            bits = 8;
        }else{
            System.err.println("Invalid input");
        }

        return bits;
    }
    private List<Integer> generateProblems(int bits){
        List<Integer> numbers = new ArrayList<>();

        int max = (1 << bits) - 1;

        for (int i = 0; i <= max; i++) {
            numbers.add(i);
        }

        Collections.shuffle(numbers);

        return numbers;
    }

    private void binarytoDecimal(){

    }
    private void decimaltoBinary(){

    }
    private void timeElapsed(){

    }
}