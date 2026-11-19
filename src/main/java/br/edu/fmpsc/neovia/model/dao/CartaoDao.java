package br.edu.fmpsc.neovia.model.dao;

import br.edu.fmpsc.neovia.model.CartaoTransporte;
import br.edu.fmpsc.neovia.model.TipoCartao;
import br.edu.fmpsc.neovia.model.Usuario;
import br.edu.fmpsc.neovia.util.SessaoUsuario;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class CartaoDao {

    private Connection abrir() throws SQLException {
        return ConexaoBanco.obterConexao();
    }

    private Usuario donoLogado() {
        return SessaoUsuario.getUsuarioLogado();
    }

    private IllegalStateException erro(SQLException e) {
        return new IllegalStateException("Erro no banco de dados: " + e.getMessage(), e);
    }

    public void salvar(CartaoTransporte c) {
        String sql = "INSERT INTO cartao_transporte "
                + "(numero, apelido, cidade, operadora, tipo, saldo, usuario_id) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection con = abrir();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, c.getNumero());
            ps.setString(2, c.getApelido());
            ps.setString(3, c.getCidade());
            ps.setString(4, c.getOperadora());
            ps.setString(5, c.getTipo().name());
            ps.setBigDecimal(6, c.consultarSaldo());
            ps.setLong(7, c.getUsuario().getId());
            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    c.setId(rs.getLong(1));
                }
            }
        } catch (SQLException e) {
            throw erro(e);
        }
    }

    // Opcional no AV2: atualiza os dados do cartão (o saldo NÃO entra aqui)
    public void atualizar(CartaoTransporte c) {
        String sql = "UPDATE cartao_transporte "
                + "SET apelido = ?, cidade = ?, operadora = ?, tipo = ? "
                + "WHERE id = ? AND usuario_id = ?";

        try (Connection con = abrir();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, c.getApelido());
            ps.setString(2, c.getCidade());
            ps.setString(3, c.getOperadora());
            ps.setString(4, c.getTipo().name());
            ps.setLong(5, c.getId());
            ps.setLong(6, c.getUsuario().getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw erro(e);
        }
    }

    // Opcional no AV2
    public void excluir(Long id) {
        String sql = "DELETE FROM cartao_transporte WHERE id = ? AND usuario_id = ?";

        try (Connection con = abrir();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setLong(1, id);
            ps.setLong(2, donoLogado().getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw erro(e);
        }
    }

    // Busca um cartão do usuário logado
    public CartaoTransporte buscarPorId(Long id) {
        return buscarPorId(id, donoLogado());
    }

    public CartaoTransporte buscarPorId(Long id, Usuario dono) {
        String sql = "SELECT * FROM cartao_transporte WHERE id = ? AND usuario_id = ?";

        try (Connection con = abrir();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setLong(1, id);
            ps.setLong(2, dono.getId());
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? montar(rs, dono) : null;
            }
        } catch (SQLException e) {
            throw erro(e);
        }
    }

    // Lista os cartões do usuário logado
    public List<CartaoTransporte> listarTodos() {
        return listarPorUsuario(donoLogado());
    }

    public List<CartaoTransporte> listarPorUsuario(Usuario dono) {
        String sql = "SELECT * FROM cartao_transporte WHERE usuario_id = ? ORDER BY apelido";
        List<CartaoTransporte> lista = new ArrayList<>();

        try (Connection con = abrir();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setLong(1, dono.getId());
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(montar(rs, dono));
                }
            }
        } catch (SQLException e) {
            throw erro(e);
        }
        return lista;
    }

    // Grava o saldo que o próprio cartão informa (a Recarga vai usar)
    public void atualizarSaldo(CartaoTransporte c) {
        String sql = "UPDATE cartao_transporte SET saldo = ? WHERE id = ?";

        try (Connection con = abrir();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setBigDecimal(1, c.consultarSaldo());
            ps.setLong(2, c.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw erro(e);
        }
    }

    private CartaoTransporte montar(ResultSet rs, Usuario dono) throws SQLException {
        return new CartaoTransporte(
                rs.getLong("id"),
                rs.getString("numero"),
                rs.getString("apelido"),
                rs.getString("cidade"),
                rs.getString("operadora"),
                TipoCartao.valueOf(rs.getString("tipo")),
                rs.getBigDecimal("saldo"),
                dono);
    }
}