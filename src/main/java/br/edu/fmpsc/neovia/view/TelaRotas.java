package br.edu.fmpsc.neovia.view;

import java.awt.Font;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

/**
 * TELA PROVISÓRIA (placeholder) - Rotas.
 * Responsável: Gustavo (fase 1, até 12/11).
 *
 * Gustavo: pode substituir este arquivo inteiro.
 * Se for usar o editor visual do NetBeans, apague este arquivo e crie um
 * "JFrame Form" com o mesmo nome (TelaRotas) neste mesmo pacote (view).
 * Lembre de mudar defaultCloseOperation para DISPOSE (ver GUIA-DA-EQUIPE.md).
 */
public class TelaRotas extends JFrame {

    public TelaRotas() {
        super("NeoVia - Rotas");
        // DISPOSE fecha só esta janela; o programa continua aberto.
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JLabel aviso = new JLabel("Em desenvolvimento – responsável: Gustavo", SwingConstants.CENTER);
        aviso.setFont(aviso.getFont().deriveFont(Font.PLAIN, 16f));
        aviso.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));
        add(aviso);

        pack();
        setLocationRelativeTo(null);
    }
}
