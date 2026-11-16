package br.edu.fmpsc.neovia.view;

import br.edu.fmpsc.neovia.controller.CartaoController;
import br.edu.fmpsc.neovia.model.CartaoTransporte;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import java.awt.BorderLayout;
import java.awt.FlowLayout;

public class TelaCartoes extends JFrame {
    private final CartaoController controller = new CartaoController();
    private final DefaultTableModel modelo = new DefaultTableModel(
            new String[]{"Apelido", "Número", "Tipo", "Saldo"}, 0) {
        @Override
        public boolean isCellEditable(int linha, int coluna) {
            return false;
        }
    };

    public TelaCartoes() {
        setTitle("Meus cartões");
        setSize(600, 350);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        JTable tabela = new JTable(modelo);
        JButton btnAdicionar = new JButton("Adicionar");
        JButton btnVoltar = new JButton("Voltar");

        btnAdicionar.addActionListener(e -> {
            new FormCartao(this, controller).setVisible(true); // modal
            carregar();
        });
        btnVoltar.addActionListener(e -> dispose());

        JPanel rodape = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        rodape.add(btnAdicionar);
        rodape.add(btnVoltar);

        add(new JScrollPane(tabela), BorderLayout.CENTER);
        add(rodape, BorderLayout.SOUTH);
        carregar();
    }

    private void carregar() {
        modelo.setRowCount(0);
        try {
            for (CartaoTransporte c : controller.listar()) {
                modelo.addRow(new Object[]{
                    c.getApelido(),
                    c.getNumero(),
                    c.getTipo(),
                    String.format("R$ %.2f", c.consultarSaldo())
                });
            }
        } catch (IllegalStateException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(),
                    "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
}