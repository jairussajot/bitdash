import java.util.Scanner;
import ui.Menu;

public class App {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        Menu menu = new Menu(scanner);
        menu.show();

        scanner.close();
    }
}

