package br.edu.fmpsc.neovia.view;

import br.edu.fmpsc.neovia.controller.UsuarioController;
import br.edu.fmpsc.neovia.model.Usuario;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class TelaUsuarios extends JFrame {

    private final UsuarioController controller = new UsuarioController();
    private final DefaultTableModel modelo = new DefaultTableModel(new Object[] { "Id", "Nome", "CPF", "E-mail" }, 0) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };
    private final JTable tabela = new JTable(modelo);

    public TelaUsuarios() {
        super("NeoVia - Usuários");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        montarTela();
        carregar();
        setSize(720, 420);
        setMinimumSize(new Dimension(640, 320));
        setLocationRelativeTo(null);
    }

    private void montarTela() {
        JPanel painel = new JPanel(new BorderLayout(0, 12));
        painel.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));

        tabela.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        tabela.setFillsViewportHeight(true);
        painel.add(new JScrollPane(tabela), BorderLayout.CENTER);

        JButton botaoNovo = new JButton("Novo");
        botaoNovo.addActionListener(evento -> novo());
        JButton botaoEditar = new JButton("Editar");
        botaoEditar.addActionListener(evento -> editar());
        JButton botaoExcluir = new JButton("Excluir");
        botaoExcluir.addActionListener(evento -> excluir());
        JButton botaoVoltar = new JButton("Voltar");
        botaoVoltar.addActionListener(evento -> dispose());

        JPanel botoes = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        botoes.add(botaoNovo);
        botoes.add(botaoEditar);
        botoes.add(botaoExcluir);
        botoes.add(botaoVoltar);
        painel.add(botoes, BorderLayout.SOUTH);

        setContentPane(painel);
    }

    private void carregar() {
        try {
            modelo.setRowCount(0);
            for (Usuario usuario : controller.listar()) {
                modelo.addRow(new Object[] {
                        usuario.getId(),
                        usuario.getNome(),
                        formatarCpf(usuario.getCpf()),
                        usuario.getEmail()
                });
            }
        } catch (RuntimeException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void novo() {
        new TelaCadastroUsuario(this, null).setVisible(true);
        carregar();
    }

    private void editar() {
        Long id = idSelecionado();
        if (id == null) {
            return;
        }
        try {
            Usuario usuario = controller.buscarPorId(id);
            if (usuario == null) {
                JOptionPane.showMessageDialog(this, "Usuário não encontrado.", "Atenção", JOptionPane.WARNING_MESSAGE);
                carregar();
                return;
            }
            new TelaCadastroUsuario(this, usuario).setVisible(true);
            carregar();
        } catch (RuntimeException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void excluir() {
        Long id = idSelecionado();
        if (id == null) {
            return;
        }
        int resposta = JOptionPane.showConfirmDialog(
                this,
                "Tem certeza?",
                "Excluir usuário",
                JOptionPane.YES_NO_OPTION);
        if (resposta != JOptionPane.YES_OPTION) {
            return;
        }
        try {
            controller.excluir(id);
            JOptionPane.showMessageDialog(this, "Usuário excluído com sucesso.");
            carregar();
        } catch (RuntimeException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private Long idSelecionado() {
        int linha = tabela.getSelectedRow();
        if (linha < 0) {
            JOptionPane.showMessageDialog(this, "Selecione um usuário.", "Atenção", JOptionPane.WARNING_MESSAGE);
            return null;
        }
        return ((Number) modelo.getValueAt(linha, 0)).longValue();
    }

    private static String formatarCpf(String cpf) {
        if (cpf == null || cpf.length() != 11) {
            return cpf == null ? "" : cpf;
        }
        return cpf.substring(0, 3) + "." + cpf.substring(3, 6) + "." + cpf.substring(6, 9) + "-" + cpf.substring(9);
    }
}
