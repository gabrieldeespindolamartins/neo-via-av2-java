package br.edu.fmpsc.neovia;

import br.edu.fmpsc.neovia.view.TelaPrincipal;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

/**
 * Ponto de partida do sistema NeoVia.
 *
 * A única tarefa desta classe é abrir a tela principal.
 * Toda a lógica fica nas camadas model, view, controller e dao.
 */
public class Main {

    public static void main(String[] args) {
        // Usa a aparência padrão do sistema operacional (no Windows, botões com cara de Windows).
        // Se der erro, o Java usa a aparência padrão dele e o programa segue normalmente.
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            System.err.println("Não foi possível aplicar a aparência do sistema: " + e.getMessage());
        }

        // Regra do Swing: telas devem ser criadas na "thread de eventos" (EDT).
        // O invokeLater garante isso.
        SwingUtilities.invokeLater(() -> new TelaPrincipal().setVisible(true));
    }
}
