import library.ui.MainWindow;
import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MainWindow());
    }
}
