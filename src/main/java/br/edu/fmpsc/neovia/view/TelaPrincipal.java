package br.edu.fmpsc.neovia.view;

import br.edu.fmpsc.neovia.util.SessaoUsuario;
import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

/**
 * Tela principal (menu) do NeoVia.
 *
 * Escrita à mão, sem o editor visual do NetBeans (sem arquivo .form),
 * para que ninguém precise editá-la de novo: ela só abre as telas de cada módulo.
 *
 * NÃO EDITE ESTA CLASSE sem falar com o Gabriel.
 * Cada colega trabalha só na própria tela (TelaRotas, TelaCartoes, TelaLogin, TelaRecarga).
 */
public class TelaPrincipal extends JFrame {

    public TelaPrincipal() {
        super("NeoVia - Transporte Público");
        // Fechar a tela principal encerra o programa.
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        montarTela();
        pack();
        setLocationRelativeTo(null); // centraliza na tela do computador
    }

    private void montarTela() {
        // BorderLayout divide a janela em regiões: NORTH (topo), CENTER (meio), SOUTH (rodapé).
        JPanel painel = new JPanel(new BorderLayout(0, 16));
        painel.setBorder(BorderFactory.createEmptyBorder(24, 32, 24, 32));

        JLabel titulo = new JLabel("NeoVia", SwingConstants.CENTER);
        titulo.setFont(titulo.getFont().deriveFont(Font.BOLD, 28f));
        painel.add(titulo, BorderLayout.NORTH);

        // GridLayout(linhas, colunas, espaço horizontal, espaço vertical): botões do mesmo tamanho.
        JPanel botoes = new JPanel(new GridLayout(4, 1, 0, 10));
        botoes.add(criarBotao("Rotas", () -> new TelaRotas().setVisible(true)));
        botoes.add(criarBotao("Meus cartões", () -> new TelaCartoes().setVisible(true)));
        botoes.add(criarBotao("Recarga", () -> new TelaRecarga().setVisible(true)));
        botoes.add(criarBotao("Entrar / Cadastrar", () -> new TelaLogin().setVisible(true)));
        painel.add(botoes, BorderLayout.CENTER);

        // Rodapé mostra quem está logado (na fase 1, o Usuário de Teste).
        JLabel rodape = new JLabel("Usuário: " + SessaoUsuario.getUsuarioLogado().getNome(),
                SwingConstants.CENTER);
        painel.add(rodape, BorderLayout.SOUTH);

        setContentPane(painel);
    }

    /**
     * Cria um botão que executa uma ação ao ser clicado.
     * Runnable = "um bloco de código para executar depois" (aqui, abrir uma tela).
     */
    private JButton criarBotao(String texto, Runnable acao) {
        JButton botao = new JButton(texto);
        botao.setFont(botao.getFont().deriveFont(16f));
        botao.addActionListener(evento -> acao.run());
        return botao;
    }
}
