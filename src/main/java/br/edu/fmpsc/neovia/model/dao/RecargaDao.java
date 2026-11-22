package br.edu.fmpsc.neovia.model.dao;

import br.edu.fmpsc.neovia.model.CartaoTransporte;
import br.edu.fmpsc.neovia.model.FormaPagamento;
import br.edu.fmpsc.neovia.model.Recarga;
import br.edu.fmpsc.neovia.model.StatusRecarga;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class RecargaDao {

    private Connection abrir() throws SQLException {
        return ConexaoBanco.obterConexao();
    }

    private IllegalStateException erro(SQLException e) {
        return new IllegalStateException("Erro no banco de dados: " + e.getMessage(), e);
    }

    public void inserir(Recarga r, FormaPagamento forma) {
        String sql = "INSERT INTO recarga (valor, data_hora, status, cartao_transporte_id, forma_pagamento_id) "
                + "VALUES (?, ?, ?, ?, ?)";

        try (Connection con = abrir();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setBigDecimal(1, r.getValor());
            ps.setTimestamp(2, Timestamp.valueOf(r.getDataHora()));
            ps.setString(3, r.getStatus().name());
            ps.setLong(4, r.getCartao().getId());
            ps.setLong(5, forma.getId()); // a forma precisa estar salva antes (tem id)
            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    r.setId(rs.getLong(1));
                }
            }
        } catch (SQLException e) {
            throw erro(e);
        }
    }

    public List<Recarga> listarPorCartao(CartaoTransporte cartao) {
        String sql = "SELECT * FROM recarga WHERE cartao_transporte_id = ? ORDER BY data_hora DESC";
        List<Recarga> lista = new ArrayList<>();

        try (Connection con = abrir();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setLong(1, cartao.getId());
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(new Recarga(
                            rs.getLong("id"),
                            rs.getBigDecimal("valor"),
                            rs.getTimestamp("data_hora").toLocalDateTime(),
                            StatusRecarga.valueOf(rs.getString("status")),
                            cartao));
                }
            }
        } catch (SQLException e) {
            throw erro(e);
        }
        return lista;
    }
}
