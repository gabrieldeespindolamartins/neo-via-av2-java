package br.edu.fmpsc.neovia.model.dao;

import br.edu.fmpsc.neovia.model.CartaoCredito;
import br.edu.fmpsc.neovia.model.FormaPagamento;
import br.edu.fmpsc.neovia.model.PagamentoPix;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

public class FormaPagamentoDao implements Dao<FormaPagamento> {

    @Override
    public void salvar(FormaPagamento entidade) {
        String sql = "INSERT INTO forma_pagamento "
                + "(tipo, usuario_id, codigo_copia_e_cola, nome_titular, bandeira, ultimos_4_digitos, validade) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conexao = ConexaoBanco.obterConexao();
                PreparedStatement comando = conexao.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            preencherDados(comando, entidade);
            comando.executeUpdate();
            try (ResultSet chaves = comando.getGeneratedKeys()) {
                if (chaves.next()) {
                    entidade.setId(chaves.getLong(1));
                }
            }
        } catch (SQLException e) {
            throw traduzirErro(e);
        }
    }

    @Override
    public void atualizar(FormaPagamento entidade) {
        String sql = "UPDATE forma_pagamento SET tipo = ?, usuario_id = ?, codigo_copia_e_cola = ?, "
                + "nome_titular = ?, bandeira = ?, ultimos_4_digitos = ?, validade = ? WHERE id = ?";
        try (Connection conexao = ConexaoBanco.obterConexao();
                PreparedStatement comando = conexao.prepareStatement(sql)) {
            preencherDados(comando, entidade);
            comando.setLong(8, entidade.getId());
            if (comando.executeUpdate() == 0) {
                throw new IllegalArgumentException("Forma de pagamento não encontrada.");
            }
        } catch (SQLException e) {
            throw traduzirErro(e);
        }
    }

    @Override
    public void excluir(Long id) {
        String sql = "DELETE FROM forma_pagamento WHERE id = ?";
        try (Connection conexao = ConexaoBanco.obterConexao();
                PreparedStatement comando = conexao.prepareStatement(sql)) {
            comando.setLong(1, id);
            comando.executeUpdate();
        } catch (SQLException e) {
            throw traduzirErro(e);
        }
    }

    @Override
    public FormaPagamento buscarPorId(Long id) {
        String sql = "SELECT id, tipo, usuario_id, codigo_copia_e_cola, nome_titular, bandeira, "
                + "ultimos_4_digitos, validade FROM forma_pagamento WHERE id = ?";
        try (Connection conexao = ConexaoBanco.obterConexao();
                PreparedStatement comando = conexao.prepareStatement(sql)) {
            comando.setLong(1, id);
            try (ResultSet resultado = comando.executeQuery()) {
                if (resultado.next()) {
                    return mapear(resultado);
                }
                return null;
            }
        } catch (SQLException e) {
            throw traduzirErro(e);
        }
    }

    @Override
    public List<FormaPagamento> listarTodos() {
        String sql = "SELECT id, tipo, usuario_id, codigo_copia_e_cola, nome_titular, bandeira, "
                + "ultimos_4_digitos, validade FROM forma_pagamento ORDER BY id";
        List<FormaPagamento> formas = new ArrayList<>();
        try (Connection conexao = ConexaoBanco.obterConexao();
                PreparedStatement comando = conexao.prepareStatement(sql);
                ResultSet resultado = comando.executeQuery()) {
            while (resultado.next()) {
                formas.add(mapear(resultado));
            }
            return formas;
        } catch (SQLException e) {
            throw traduzirErro(e);
        }
    }

    private void preencherDados(PreparedStatement comando, FormaPagamento entidade) throws SQLException {
        comando.setString(1, entidade.getTipo());
        comando.setNull(2, Types.BIGINT);
        comando.setNull(3, Types.VARCHAR);
        comando.setNull(4, Types.VARCHAR);
        comando.setNull(5, Types.VARCHAR);
        comando.setNull(6, Types.CHAR);
        comando.setNull(7, Types.VARCHAR);

        if (entidade instanceof PagamentoPix pix) {
            if (pix.getCodigoCopiaECola() != null) {
                comando.setString(3, pix.getCodigoCopiaECola());
            }
        } else if (entidade instanceof CartaoCredito cartao) {
            if (cartao.getNomeTitular() != null) {
                comando.setString(4, cartao.getNomeTitular());
            }
            if (cartao.getBandeira() != null) {
                comando.setString(5, cartao.getBandeira());
            }
            if (cartao.getUltimos4Digitos() != null) {
                comando.setString(6, cartao.getUltimos4Digitos());
            }
            if (cartao.getValidade() != null) {
                comando.setString(7, cartao.getValidade());
            }
        } else {
            throw new IllegalArgumentException("Tipo de forma de pagamento não suportado: " + entidade.getClass().getSimpleName());
        }
    }

    private FormaPagamento mapear(ResultSet resultado) throws SQLException {
        String tipo = resultado.getString("tipo");
        FormaPagamento forma;
        if ("PIX".equals(tipo)) {
            PagamentoPix pix = new PagamentoPix();
            pix.setCodigoCopiaECola(resultado.getString("codigo_copia_e_cola"));
            forma = pix;
        } else if ("CARTAO_CREDITO".equals(tipo)) {
            CartaoCredito cartao = new CartaoCredito();
            cartao.setNomeTitular(resultado.getString("nome_titular"));
            cartao.setBandeira(resultado.getString("bandeira"));
            cartao.setUltimos4Digitos(resultado.getString("ultimos_4_digitos"));
            cartao.setValidade(resultado.getString("validade"));
            forma = cartao;
        } else {
            throw new IllegalArgumentException("Tipo de forma de pagamento desconhecido no banco: " + tipo);
        }
        forma.setId(resultado.getLong("id"));
        return forma;
    }

    private RuntimeException traduzirErro(SQLException e) {
        return new RuntimeException("Erro ao acessar o banco de dados: " + e.getMessage(), e);
    }
}
