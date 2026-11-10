package br.edu.fmpsc.neovia.model;

/**
 * Classe-mãe de TODAS as entidades do sistema (Usuario, CartaoTransporte, Recarga...).
 *
 * Por que ela existe?
 * - Toda entidade salva no banco precisa de um identificador (id).
 * - Em vez de repetir o atributo id em cada classe, ele fica aqui uma vez só,
 *   e as outras classes herdam com "extends EntidadeBase".
 *
 * Por que é abstrata?
 * - Não faz sentido existir uma "EntidadeBase" sozinha no sistema.
 *   Ela só serve de base para outras classes, por isso não pode ser instanciada
 *   (não dá para fazer new EntidadeBase()).
 *
 * Bônus: o Dao genérico (Dao<T extends EntidadeBase>) só aceita classes que
 * herdam daqui. Assim ele tem certeza de que toda entidade tem getId() e setId().
 */
public abstract class EntidadeBase {

    // Long (com L maiúsculo) aceita null: id null = objeto ainda não foi salvo.
    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
