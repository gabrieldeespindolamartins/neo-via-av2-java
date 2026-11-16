package br.edu.fmpsc.neovia.view;

import br.edu.fmpsc.neovia.controller.CartaoController;
import br.edu.fmpsc.neovia.model.TipoCartao;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import java.awt.GridLayout;

public class FormCartao extends JDialog {
    private final JTextField txtNumero = new JTextField(20);
    private final JTextField txtApelido = new JTextField(20);
    private final JTextField txtCidade = new JTextField(20);
    private final JTextField txtOperadora = new JTextField(20);
    private final JComboBox<TipoCartao> cmbTipo = new JComboBox<>(TipoCartao.values());

    public FormCartao(JFrame pai, CartaoController controller) {
        super(pai, "Dados do passe", true);
        setLayout(new GridLayout(0, 2, 6, 6));

        add(new JLabel("Número:"));    add(txtNumero);
        add(new JLabel("Apelido:"));   add(txtApelido);
        add(new JLabel("Cidade:"));    add(txtCidade);
        add(new JLabel("Operadora:")); add(txtOperadora);
        add(new JLabel("Tipo:"));      add(cmbTipo);

        JButton btnSalvar = new JButton("Salvar");
        JButton btnCancelar = new JButton("Cancelar");
        add(btnSalvar);
        add(btnCancelar);

        btnSalvar.addActionListener(e -> {
            try {
                controller.cadastrar(txtNumero.getText(), txtApelido.getText(),
                        txtCidade.getText(), txtOperadora.getText(),
                        (TipoCartao) cmbTipo.getSelectedItem());
                JOptionPane.showMessageDialog(this, "Cartão cadastrado com sucesso!",
                        "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                dispose();
            } catch (IllegalArgumentException ex) {
                // "Dados errados" ou "Cartão já existe"
                JOptionPane.showMessageDialog(this, ex.getMessage(),
                        "Aviso", JOptionPane.WARNING_MESSAGE);
            } catch (IllegalStateException ex) {
                // erro do banco: mostra a mensagem e o programa continua
                JOptionPane.showMessageDialog(this, ex.getMessage(),
                        "Erro", JOptionPane.ERROR_MESSAGE);
            }
        });
        btnCancelar.addActionListener(e -> dispose());

        pack();
        setLocationRelativeTo(pai);
    }
}