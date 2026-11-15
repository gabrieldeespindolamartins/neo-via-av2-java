package br.edu.fmpsc.neovia.view;

import br.edu.fmpsc.neovia.controller.UsuarioController;
import br.edu.fmpsc.neovia.model.Usuario;
import br.edu.fmpsc.neovia.util.SessaoUsuario;
import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

public class TelaLogin extends JFrame {

    private final UsuarioController controller = new UsuarioController();
    private final JTextField campoEmail = new JTextField(24);
    private final JPasswordField campoSenha = new JPasswordField(24);

    public TelaLogin() {
        super("NeoVia - Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        montarTela();
        pack();
        setLocationRelativeTo(null);
    }

    private void montarTela() {
        JPanel painel = new JPanel(new BorderLayout(0, 16));
        painel.setBorder(BorderFactory.createEmptyBorder(32, 40, 32, 40));

        JLabel titulo = new JLabel("NeoVia", SwingConstants.CENTER);
        titulo.setFont(titulo.getFont().deriveFont(Font.BOLD, 28f));
        painel.add(titulo, BorderLayout.NORTH);

        JPanel formulario = new JPanel(new GridLayout(2, 2, 8, 8));
        formulario.add(new JLabel("E-mail"));
        formulario.add(campoEmail);
        formulario.add(new JLabel("Senha"));
        formulario.add(campoSenha);
        painel.add(formulario, BorderLayout.CENTER);

        JButton botaoEntrar = new JButton("Entrar");
        botaoEntrar.addActionListener(evento -> entrar());
        JButton botaoCriarConta = new JButton("Criar conta");
        botaoCriarConta.addActionListener(evento -> criarConta());

        JPanel botoes = new JPanel(new GridLayout(2, 1, 0, 8));
        botoes.add(botaoEntrar);
        botoes.add(botaoCriarConta);
        painel.add(botoes, BorderLayout.SOUTH);

        setContentPane(painel);
        getRootPane().setDefaultButton(botaoEntrar);
    }

    private void entrar() {
        try {
            Usuario usuario = controller.autenticar(campoEmail.getText(), new String(campoSenha.getPassword()));
            SessaoUsuario.setUsuarioLogado(usuario);
            new TelaPrincipal().setVisible(true);
            dispose();
        } catch (IllegalArgumentException e) {
            mostrarAviso(e.getMessage());
        } catch (RuntimeException e) {
            mostrarErro(e.getMessage());
        }
    }

    private void criarConta() {
        new TelaCadastroUsuario(this, null).setVisible(true);
    }

    private void mostrarAviso(String mensagem) {
        JOptionPane.showMessageDialog(this, mensagem, "Atenção", JOptionPane.WARNING_MESSAGE);
    }

    private void mostrarErro(String mensagem) {
        JOptionPane.showMessageDialog(this, mensagem, "Erro", JOptionPane.ERROR_MESSAGE);
    }
}
