import java.util.Scanner;

public class App {
    public static void main(String[] args) throws Exception {
        Scanner scanner = new Scanner(System.in);
        
        System.out.println("==BitDash==");
        System.out.println("Start");
        System.out.println("Instruction");
        System.out.println("Scoreboard");
        System.out.println("Quit");
        System.out.print("Please select and option: ");
        String menu_select = scanner.nextLine();

        if (menu_select.equals("Start")) {
            System.out.println("Start Game Here");
        }else if (menu_select.equals("Instruction")) {
            System.out.println("Just answer chud");
        }else if (menu_select.equals("Scoreboard")) {
            System.out.println("No scoreboard yet");
        }else if (menu_select.equals("Quit")) {
            System.out.println("Bye");

            System.exit(0);
        }
        scanner.close();
    }
}
