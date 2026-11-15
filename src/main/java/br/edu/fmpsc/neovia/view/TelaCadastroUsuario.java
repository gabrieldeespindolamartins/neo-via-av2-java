package br.edu.fmpsc.neovia.view;

import br.edu.fmpsc.neovia.controller.UsuarioController;
import br.edu.fmpsc.neovia.model.Usuario;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.Window;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class TelaCadastroUsuario extends JDialog {

    private final UsuarioController controller = new UsuarioController();
    private final Usuario usuario;
    private final JTextField campoNome = new JTextField(24);
    private final JTextField campoCpf = new JTextField(24);
    private final JTextField campoEmail = new JTextField(24);
    private final JPasswordField campoSenha = new JPasswordField(24);

    public TelaCadastroUsuario(Window pai, Usuario usuario) {
        super(pai, titulo(usuario), ModalityType.APPLICATION_MODAL);
        this.usuario = usuario;
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        montarTela();
        preencher();
        pack();
        setLocationRelativeTo(pai);
    }

    private static String titulo(Usuario usuario) {
        if (usuario != null && usuario.getId() != null) {
            return "NeoVia - Editar usuário";
        }
        return "NeoVia - Cadastro de usuário";
    }

    private void montarTela() {
        JPanel painel = new JPanel(new BorderLayout(0, 16));
        painel.setBorder(BorderFactory.createEmptyBorder(24, 24, 24, 24));

        JPanel formulario = new JPanel(new GridLayout(0, 2, 8, 8));
        formulario.add(new JLabel("Nome"));
        formulario.add(campoNome);
        formulario.add(new JLabel("CPF"));
        formulario.add(campoCpf);
        formulario.add(new JLabel("E-mail"));
        formulario.add(campoEmail);
        formulario.add(new JLabel("Senha"));
        formulario.add(campoSenha);
        if (usuario != null && usuario.getId() != null) {
            formulario.add(new JLabel(""));
            formulario.add(new JLabel("Em branco mantém a senha atual."));
        }
        painel.add(formulario, BorderLayout.CENTER);

        JButton botaoSalvar = new JButton("Salvar");
        botaoSalvar.addActionListener(evento -> salvar());
        JButton botaoCancelar = new JButton("Cancelar");
        botaoCancelar.addActionListener(evento -> dispose());

        JPanel botoes = new JPanel(new GridLayout(1, 2, 8, 0));
        botoes.add(botaoSalvar);
        botoes.add(botaoCancelar);
        painel.add(botoes, BorderLayout.SOUTH);

        setContentPane(painel);
        getRootPane().setDefaultButton(botaoSalvar);
    }

    private void preencher() {
        if (usuario == null) {
            return;
        }
        campoNome.setText(usuario.getNome());
        campoCpf.setText(formatarCpf(usuario.getCpf()));
        campoEmail.setText(usuario.getEmail());
    }

    private void salvar() {
        try {
            String senha = new String(campoSenha.getPassword());
            if (usuario != null && usuario.getId() != null) {
                controller.atualizar(usuario.getId(), campoNome.getText(), campoCpf.getText(), campoEmail.getText(), senha);
                JOptionPane.showMessageDialog(this, "Usuário atualizado com sucesso.");
            } else {
                controller.cadastrar(campoNome.getText(), campoCpf.getText(), campoEmail.getText(), senha);
                JOptionPane.showMessageDialog(this, "Usuário cadastrado com sucesso.");
            }
            dispose();
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Atenção", JOptionPane.WARNING_MESSAGE);
        } catch (RuntimeException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private static String formatarCpf(String cpf) {
        if (cpf == null || cpf.length() != 11) {
            return cpf == null ? "" : cpf;
        }
        return cpf.substring(0, 3) + "." + cpf.substring(3, 6) + "." + cpf.substring(6, 9) + "-" + cpf.substring(9);
    }
}
