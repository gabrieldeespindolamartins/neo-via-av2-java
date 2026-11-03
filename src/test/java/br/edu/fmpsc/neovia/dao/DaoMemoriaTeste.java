package br.edu.fmpsc.neovia.dao;

import br.edu.fmpsc.neovia.model.Usuario;
import java.util.List;

/**
 * Teste rápido do DaoMemoria (sem bibliotecas de teste, só um main).
 *
 * Como rodar no NetBeans: clique com o botão direito neste arquivo > Run File (Shift+F6).
 * Se tudo estiver certo, a última linha impressa é "TODOS OS TESTES PASSARAM".
 *
 * Também serve de exemplo de polimorfismo para a apresentação:
 * a variável é do tipo Dao (interface), o objeto é um DaoMemoria (implementação).
 */
public class DaoMemoriaTeste {

    public static void main(String[] args) {
        // POLIMORFISMO: tipo da variável = interface; objeto real = DaoMemoria.
        // Quando o banco estiver pronto, só esta linha muda.
        Dao<Usuario> dao = new DaoMemoria<>();

        // CREATE
        Usuario ana = new Usuario("Ana", "11111111111", "ana@email.com", "hash1");
        Usuario bruno = new Usuario("Bruno", "22222222222", "bruno@email.com", "hash2");
        dao.salvar(ana);
        dao.salvar(bruno);
        conferir(ana.getId() == 1L && bruno.getId() == 2L, "salvar gera ids 1 e 2");

        // READ
        List<Usuario> todos = dao.listarTodos();
        conferir(todos.size() == 2, "listarTodos devolve 2 usuários");
        conferir(dao.buscarPorId(2L).getNome().equals("Bruno"), "buscarPorId(2) encontra o Bruno");
        conferir(dao.buscarPorId(99L) == null, "buscarPorId de id inexistente devolve null");

        // UPDATE
        ana.setNome("Ana Souza");
        dao.atualizar(ana);
        conferir(dao.buscarPorId(1L).getNome().equals("Ana Souza"), "atualizar muda o nome");

        // DELETE
        dao.excluir(2L);
        conferir(dao.listarTodos().size() == 1, "excluir remove o Bruno");
        conferir(dao.buscarPorId(2L) == null, "Bruno não é mais encontrado");

        System.out.println("TODOS OS TESTES PASSARAM");
    }

    /** Imprime OK ou interrompe o teste com erro. */
    private static void conferir(boolean condicao, String descricao) {
        if (!condicao) {
            throw new AssertionError("FALHOU: " + descricao);
        }
        System.out.println("OK: " + descricao);
    }
}
