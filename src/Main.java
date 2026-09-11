import javax.swing.SwingUtilities;

public class Main {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            TelaFinanciamento tela = new TelaFinanciamento();
            tela.setVisible(true);
        });
    }
}
