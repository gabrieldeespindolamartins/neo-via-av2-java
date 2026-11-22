package br.edu.fmpsc.neovia.view;

import br.edu.fmpsc.neovia.controller.CartaoController;
import br.edu.fmpsc.neovia.controller.RecargaController;
import br.edu.fmpsc.neovia.model.CartaoCredito;
import br.edu.fmpsc.neovia.model.CartaoTransporte;
import br.edu.fmpsc.neovia.model.FormaPagamento;
import br.edu.fmpsc.neovia.model.PagamentoPix;
import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.math.BigDecimal;

public class TelaRecarga extends JFrame {
    private static final String PIX = "Pix";
    private static final String CREDITO = "Cartão de crédito";

    private final CartaoController cartaoController = new CartaoController();
    private final RecargaController recargaController = new RecargaController();

    private final JComboBox<CartaoTransporte> cmbCartao = new JComboBox<>();
    private final JTextField txtValor = new JTextField(10);
    private final JComboBox<String> cmbForma = new JComboBox<>(new String[]{PIX, CREDITO});
    private final JLabel lblSaldo = new JLabel(" ");

    // Dados do cartão de crédito (só aparecem quando a forma é crédito)
    private final JTextField txtNumCredito = new JTextField(16);
    private final JTextField txtTitular = new JTextField(16);
    private final JTextField txtValidade = new JTextField(5);
    private final JTextField txtCvv = new JTextField(4);
    private final JComboBox<String> cmbBandeira = new JComboBox<>(new String[]{"Visa", "Mastercard", "Elo"});
    private final JPanel painelCredito = new JPanel(new GridLayout(0, 2, 6, 6));

    public TelaRecarga() {
        setTitle("Recarga de cartão");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        // mostra "apelido (número)" em vez do texto padrão do objeto
        cmbCartao.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value,
                    int index, boolean selected, boolean focus) {
                super.getListCellRendererComponent(list, value, index, selected, focus);
                if (value instanceof CartaoTransporte c) {
                    setText(c.getApelido() + " (" + c.getNumero() + ")");
                }
                return this;
            }
        });
        cmbCartao.addActionListener(e -> atualizarSaldo());

        JPanel campos = new JPanel(new GridLayout(0, 2, 6, 6));
        campos.setBorder(BorderFactory.createEmptyBorder(12, 12, 6, 12));
        campos.add(new JLabel("Cartão de transporte:")); campos.add(cmbCartao);
        campos.add(new JLabel("Saldo atual:"));          campos.add(lblSaldo);
        campos.add(new JLabel("Valor (R$):"));           campos.add(txtValor);
        campos.add(new JLabel("Forma de pagamento:"));   campos.add(cmbForma);

        painelCredito.setBorder(BorderFactory.createTitledBorder("Dados do cartão de crédito"));
        painelCredito.add(new JLabel("Número:"));   painelCredito.add(txtNumCredito);
        painelCredito.add(new JLabel("Titular:"));  painelCredito.add(txtTitular);
        painelCredito.add(new JLabel("Bandeira:")); painelCredito.add(cmbBandeira);
        painelCredito.add(new JLabel("Validade (MM/AAAA):")); painelCredito.add(txtValidade);
        painelCredito.add(new JLabel("CVV:"));      painelCredito.add(txtCvv);
        painelCredito.setVisible(false);

        cmbForma.addActionListener(e -> {
            painelCredito.setVisible(CREDITO.equals(cmbForma.getSelectedItem()));
            pack();
        });

        JButton btnConfirmar = new JButton("Confirmar");
        JButton btnVoltar = new JButton("Voltar");
        btnConfirmar.addActionListener(e -> confirmar());
        btnVoltar.addActionListener(e -> dispose());
        JPanel rodape = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        rodape.add(btnConfirmar);
        rodape.add(btnVoltar);

        add(campos, BorderLayout.NORTH);
        add(painelCredito, BorderLayout.CENTER);
        add(rodape, BorderLayout.SOUTH);

        carregarCartoes();
        pack();
        setLocationRelativeTo(null);
    }

    private void carregarCartoes() {
        cmbCartao.removeAllItems();
        try {
            for (CartaoTransporte c : cartaoController.listar()) {
                cmbCartao.addItem(c);
            }
        } catch (IllegalStateException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(),
                    "Erro", JOptionPane.ERROR_MESSAGE);
        }
        atualizarSaldo();
    }

    private void atualizarSaldo() {
        CartaoTransporte c = (CartaoTransporte) cmbCartao.getSelectedItem();
        lblSaldo.setText(c == null ? "Nenhum cartão cadastrado" : moeda(c.consultarSaldo()));
    }

    // A tela escolhe a subclasse. O controller só enxerga FormaPagamento (polimorfismo).
    private FormaPagamento criarForma() {
        if (PIX.equals(cmbForma.getSelectedItem())) {
            return new PagamentoPix();
        }

        String numero = txtNumCredito.getText().trim();
        String titular = txtTitular.getText().trim();
        String validade = txtValidade.getText().trim();
        String cvv = txtCvv.getText().trim();
        if (numero.isEmpty() || titular.isEmpty() || validade.isEmpty() || cvv.isEmpty()) {
            throw new IllegalArgumentException("Preencha os dados do cartão de crédito.");
        }

        // Construtor do CartaoCredito: (titular, bandeira, número, validade).
        // O CVV só é conferido aqui na tela: não é guardado (só os 4 últimos dígitos vão ao banco).
        String bandeira = (String) cmbBandeira.getSelectedItem();
        return new CartaoCredito(titular, bandeira, numero, validade);
    }

    private void confirmar() {
        CartaoTransporte cartao = (CartaoTransporte) cmbCartao.getSelectedItem();
        try {
            recargaController.confirmar(cartao, txtValor.getText(), criarForma());
            JOptionPane.showMessageDialog(this,
                    "Recarga feita! Novo saldo: " + moeda(cartao.consultarSaldo()),
                    "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            txtValor.setText("");
            txtCvv.setText("");
            atualizarSaldo();
        } catch (IllegalArgumentException ex) {
            // validação: sem cartão, valor vazio ou inválido, dados do crédito, pagamento recusado
            JOptionPane.showMessageDialog(this, ex.getMessage(),
                    "Aviso", JOptionPane.WARNING_MESSAGE);
        } catch (IllegalStateException ex) {
            // erro do banco: mostra a mensagem e o programa continua
            JOptionPane.showMessageDialog(this, ex.getMessage(),
                    "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private String moeda(BigDecimal valor) {
        return String.format("R$ %.2f", valor);
    }
}