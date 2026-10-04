import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import ui.MainWindow;

/** Launches the Swing version of BitDash. (App.java still runs the console version.) */
public class GuiApp {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                // Metal looks the same on every OS, so the custom colours always apply.
                UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
            } catch (Exception e) {
                // fall back to the default look and feel
            }
            new MainWindow().setVisible(true);
        });
    }
}
