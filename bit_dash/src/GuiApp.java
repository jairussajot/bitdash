import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import ui.MainWindow;


public class GuiApp {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
            } catch (Exception e) {
            }
            new MainWindow().setVisible(true);
        });
    }
}
