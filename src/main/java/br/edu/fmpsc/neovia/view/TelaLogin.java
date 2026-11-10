package br.edu.fmpsc.neovia.view;

import br.edu.fmpsc.neovia.model.Usuario;
import br.edu.fmpsc.neovia.util.SessaoUsuario;
import java.awt.BorderLayout;
import java.awt.Font;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

/**
 * TELA PROVISÓRIA (placeholder) - Login (primeira tela do sistema).
 * Responsável: Gustavo (fase 1, de 10 a 15/11).
 *
 * O Gustavo vai trocar esta tela pelo login de verdade
 * (e-mail e senha conferidos com o UsuarioDao). Pode substituir este arquivo inteiro.
 * Se for usar o editor visual do NetBeans, apague este arquivo e crie um
 * "JFrame Form" com o mesmo nome (TelaLogin) neste mesmo pacote (view).
 *
 * Atenção: como esta é a PRIMEIRA janela do sistema, fechar ela encerra o programa
 * (EXIT_ON_CLOSE). Nas outras telas o correto é DISPOSE (ver GUIA-DA-EQUIPE.md).
 * Quando o login der certo, chame SessaoUsuario.setUsuarioLogado(usuario),
 * abra a TelaPrincipal e feche a TelaLogin com dispose().
 */
public class TelaLogin extends JFrame {

    public TelaLogin() {
        super("NeoVia - Login");
        // EXIT_ON_CLOSE: fechar o login encerra o programa (é a primeira janela).
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel painel = new JPanel(new BorderLayout(0, 16));
        painel.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));

        JLabel aviso = new JLabel("Em desenvolvimento – responsável: Gustavo", SwingConstants.CENTER);
        aviso.setFont(aviso.getFont().deriveFont(Font.PLAIN, 16f));
        painel.add(aviso, BorderLayout.CENTER);

        // Botão provisório: só existe até o login de verdade ficar pronto.
        JButton botaoTeste = new JButton("Entrar como Usuário de Teste");
        botaoTeste.addActionListener(evento -> entrarComoUsuarioDeTeste());
        painel.add(botaoTeste, BorderLayout.SOUTH);

        setContentPane(painel);
        pack();
        setLocationRelativeTo(null);
    }

    private void entrarComoUsuarioDeTeste() {
        // Usuário de Teste: o mesmo do database/dados.sql (id 1).
        Usuario teste = new Usuario("Usuário de Teste", "00000000000",
                "teste@neovia.com", "hash-provisorio");
        teste.setId(1L);
        SessaoUsuario.setUsuarioLogado(teste);
        new TelaPrincipal().setVisible(true);
        dispose(); // fecha só o login; o programa continua com o menu aberto
    }
}
