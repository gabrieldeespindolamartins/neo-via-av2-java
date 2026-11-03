package br.edu.fmpsc.neovia.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Único lugar do sistema com os dados de conexão com o banco MySQL.
 *
 * Por que isolar aqui?
 * - Se o endereço, o usuário ou a senha do banco mudarem, muda-se só este arquivo.
 * - Os DAOs com banco (ex.: RotaDaoBanco) pedem a conexão para esta classe,
 *   em vez de cada um ter os seus próprios dados de conexão.
 *
 * =====================================================================
 *  CONFIGURAR APÓS A AULA DE 09/11
 *  O professor vai mostrar como quer a conexão (possivelmente com Hibernate).
 *  Até lá, NÃO configure Hibernate. Use o DaoMemoria para testar as telas.
 *  Não altere este arquivo sem falar com o Gabriel.
 * =====================================================================
 */
public final class ConexaoBanco {

    // Dados de conexão (ajustar conforme o MySQL de cada computador).
    // Formato da URL: jdbc:mysql://servidor:porta/nome_do_banco
    private static final String URL = "jdbc:mysql://localhost:3306/neovia";
    private static final String USUARIO = "root";
    private static final String SENHA = "";

    // Construtor privado: ninguém precisa criar objetos desta classe,
    // basta chamar ConexaoBanco.obterConexao().
    private ConexaoBanco() {
    }

    /**
     * Abre e devolve uma conexão com o banco (JDBC puro, sem Hibernate).
     * Quem usar deve fechar a conexão depois, de preferência com try-with-resources:
     *
     *     try (Connection conexao = ConexaoBanco.obterConexao()) {
     *         // usar a conexão aqui
     *     }
     *
     * Pode mudar depois da aula de 09/11.
     */
    public static Connection obterConexao() throws SQLException {
        return DriverManager.getConnection(URL, USUARIO, SENHA);
    }
}
