package br.edu.fmpsc.neovia.view;

import java.awt.Font;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

/**
 * TELA PROVISÓRIA (placeholder) - Entrar / Cadastrar (login e cadastro do usuário).
 * Responsável: Gustavo (fase 2, de 13 a 19/11).
 *
 * Gustavo: pode substituir este arquivo inteiro.
 * Se for usar o editor visual do NetBeans, apague este arquivo e crie um
 * "JFrame Form" com o mesmo nome (TelaLogin) neste mesmo pacote (view).
 * Lembre de mudar defaultCloseOperation para DISPOSE (ver GUIA-DA-EQUIPE.md).
 * Quando o login der certo, chame SessaoUsuario.setUsuarioLogado(usuario).
 */
public class TelaLogin extends JFrame {

    public TelaLogin() {
        super("NeoVia - Entrar / Cadastrar");
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
