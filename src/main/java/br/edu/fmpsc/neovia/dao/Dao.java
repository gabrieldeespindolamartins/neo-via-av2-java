package br.edu.fmpsc.neovia.dao;

import br.edu.fmpsc.neovia.model.EntidadeBase;
import java.util.List;

/**
 * Contrato (interface) que TODO DAO do sistema deve cumprir.
 *
 * DAO = Data Access Object = "objeto que acessa os dados".
 * Ele é o único lugar do sistema que sabe ONDE os dados ficam guardados
 * (numa lista em memória, num banco MySQL...). O resto do sistema só chama
 * salvar(), listarTodos() etc., sem saber como isso é feito por dentro.
 *
 * O <T extends EntidadeBase> é um "tipo genérico":
 * - T é um espaço reservado para a classe que o DAO vai guardar (Usuario, Rota...).
 * - "extends EntidadeBase" garante que T sempre tem id.
 * Exemplo: Dao<Usuario> é um DAO que só aceita e devolve objetos Usuario.
 *
 * As 5 operações abaixo formam o CRUD:
 * Create (salvar), Read (buscarPorId, listarTodos), Update (atualizar), Delete (excluir).
 */
public interface Dao<T extends EntidadeBase> {

    /** Grava uma entidade nova. Ao terminar, a entidade passa a ter um id. */
    void salvar(T entidade);

    /** Grava as alterações de uma entidade que já existe (que já tem id). */
    void atualizar(T entidade);

    /** Remove a entidade com este id. */
    void excluir(Long id);

    /** Devolve a entidade com este id, ou null se não existir. */
    T buscarPorId(Long id);

    /** Devolve todas as entidades guardadas (lista vazia se não houver nenhuma). */
    List<T> listarTodos();
}
