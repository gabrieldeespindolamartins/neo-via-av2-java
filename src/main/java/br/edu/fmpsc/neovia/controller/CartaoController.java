package br.edu.fmpsc.neovia.controller;

import br.edu.fmpsc.neovia.model.CartaoTransporte;
import br.edu.fmpsc.neovia.model.TipoCartao;
import br.edu.fmpsc.neovia.model.Usuario;
import br.edu.fmpsc.neovia.model.dao.CartaoDao;
import br.edu.fmpsc.neovia.util.SessaoUsuario;
import java.sql.SQLException;
import java.util.List;

public class CartaoController {
    private static final int ERRO_DUPLICADO_MYSQL = 1062;

    private final CartaoDao dao = new CartaoDao();

    // IllegalArgumentException = erro de validação ("Dados errados", "Cartão já existe")
    // IllegalStateException    = erro do banco (vem do DAO)
    public void cadastrar(String numero, String apelido, String cidade,
                          String operadora, TipoCartao tipo) {
        numero = numero == null ? "" : numero.trim();
        cidade = cidade == null ? "" : cidade.trim();
        operadora = operadora == null ? "" : operadora.trim();

        if (!numero.matches("\\d{1,30}") || cidade.isEmpty() || operadora.isEmpty() || tipo == null) {
            throw new IllegalArgumentException("Dados errados");
        }

        if (apelido == null || apelido.isBlank()) {
            apelido = "Cartão " + numero;
        }

        // AJUSTAR o nome do método se for outro
        Usuario dono = SessaoUsuario.getUsuarioLogado();

        try {
            dao.salvar(new CartaoTransporte(numero, apelido.trim(), cidade, operadora, tipo, dono));
        } catch (IllegalStateException e) {
            // RN04: a chave UNIQUE (usuario_id, numero) do banco barra o repetido
            if (e.getCause() instanceof SQLException sql
                    && sql.getErrorCode() == ERRO_DUPLICADO_MYSQL) {
                throw new IllegalArgumentException("Cartão já existe");
            }
            throw e;
        }
    }

    public List<CartaoTransporte> listar() {
        return dao.listarTodos();
    }
}