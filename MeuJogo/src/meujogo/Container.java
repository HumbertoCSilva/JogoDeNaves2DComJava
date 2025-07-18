package meujogo;

import meujogo.Modelo.Fase;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class Container extends JFrame {
    public Container() {
        Fase fase = new Fase();
        add(fase);

        setTitle("Meu Jogo");
        setSize(1024, 768);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowActivated(WindowEvent e) {
                System.out.println("Container: Janela ativada. Solicitando foco para a Fase.");
                fase.requestFocusInWindow();
            }

            @Override
            public void windowOpened(WindowEvent e) {
                System.out.println("Container: Janela aberta. Tentando solicitar foco.");
                fase.requestFocusInWindow();
            }
        });

        SwingUtilities.invokeLater(() -> {
            setVisible(true);
            System.out.println("Container: setVisible(true) chamado via EDT.");
        });
    }

    public static void main(String[] args) {
        new Container();
    }
}