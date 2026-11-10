package br.edu.fmpsc.neovia.model.dao;

import br.edu.fmpsc.neovia.model.EntidadeBase;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO provisório que guarda os dados numa lista em memória (ArrayList).
 *
 * Para que serve?
 * - Permite testar as telas ANTES de o banco de dados estar pronto.
 * - Os dados somem quando o programa fecha. Isso é esperado: é só para teste.
 *
 * Como usar (exemplo com a classe CartaoTransporte):
 *     Dao<CartaoTransporte> dao = new DaoMemoria<>();
 *     dao.salvar(novoCartao);
 *
 * Depois, quando o banco estiver pronto, basta trocar UMA linha:
 *     Dao<CartaoTransporte> dao = new CartaoDao();   // nova classe que implementa Dao<CartaoTransporte>
 * O resto do código continua igual. Isso é POLIMORFISMO: a variável é do tipo
 * da interface (Dao), e o objeto real pode ser qualquer classe que a implemente.
 *
 * Por ser genérica (<T>), esta MESMA classe serve para Usuario,
 * CartaoTransporte, Recarga... Ninguém precisa reescrevê-la.
 */
public class DaoMemoria<T extends EntidadeBase> implements Dao<T> {

    // Onde os objetos ficam guardados enquanto o programa está aberto.
    private final List<T> dados = new ArrayList<>();

    // Simula o AUTO_INCREMENT do banco: cada objeto salvo recebe o próximo número.
    private long proximoId = 1;

    @Override
    public void salvar(T entidade) {
        entidade.setId(proximoId);
        proximoId++;
        dados.add(entidade);
    }

    @Override
    public void atualizar(T entidade) {
        // Procura a posição do objeto com o mesmo id e troca pelo novo.
        for (int i = 0; i < dados.size(); i++) {
            if (dados.get(i).getId().equals(entidade.getId())) {
                dados.set(i, entidade);
                return;
            }
        }
        throw new IllegalArgumentException("Não existe registro com id " + entidade.getId());
    }

    @Override
    public void excluir(Long id) {
        // removeIf apaga todos os itens que satisfazem a condição (aqui: mesmo id).
        dados.removeIf(item -> item.getId().equals(id));
    }

    @Override
    public T buscarPorId(Long id) {
        for (T item : dados) {
            if (item.getId().equals(id)) {
                return item;
            }
        }
        return null; // não encontrou
    }

    @Override
    public List<T> listarTodos() {
        // Devolve uma CÓPIA da lista, para ninguém alterar os dados "por fora" do DAO.
        return new ArrayList<>(dados);
    }
}
