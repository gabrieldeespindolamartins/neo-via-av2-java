package br.edu.fmpsc.neovia.util;

import br.edu.fmpsc.neovia.model.Usuario;

/**
 * Guarda quem está logado no sistema enquanto o programa está aberto.
 *
 * Qualquer tela pode perguntar "quem é o usuário atual?":
 *     Usuario usuario = SessaoUsuario.getUsuarioLogado();
 *
 * FASE 1: a tela de login ainda não existe. Por isso, se ninguém fez login,
 * esta classe devolve um "Usuário de Teste" fixo (id 1), o mesmo que está
 * no arquivo database/dados.sql. Assim as telas de Rotas e Cartões já funcionam.
 *
 * FASE 2: a TelaLogin, depois de conferir e-mail e senha, chama
 *     SessaoUsuario.setUsuarioLogado(usuarioQueFezLogin);
 * e a partir daí as telas passam a receber o usuário real.
 *
 * Os métodos são "static": existe uma única sessão para o programa inteiro,
 * então não é preciso criar objetos desta classe.
 */
public final class SessaoUsuario {

    private static Usuario usuarioLogado;

    private SessaoUsuario() {
    }

    /** Devolve o usuário logado ou, se ninguém fez login ainda, o Usuário de Teste. */
    public static Usuario getUsuarioLogado() {
        if (usuarioLogado == null) {
            return criarUsuarioDeTeste();
        }
        return usuarioLogado;
    }

    /** Chamado pela TelaLogin (fase 2) quando o login dá certo. */
    public static void setUsuarioLogado(Usuario usuario) {
        usuarioLogado = usuario;
    }

    /** Sair da conta (logout). */
    public static void encerrar() {
        usuarioLogado = null;
    }

    /** Mesmo usuário do database/dados.sql. Provisório até a fase 2. */
    private static Usuario criarUsuarioDeTeste() {
        Usuario teste = new Usuario("Usuário de Teste", "00000000000",
                "teste@neovia.com", "hash-provisorio");
        teste.setId(1L);
        return teste;
    }
}
