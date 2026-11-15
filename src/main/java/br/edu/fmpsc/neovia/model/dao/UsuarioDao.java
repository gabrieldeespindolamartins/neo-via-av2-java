package br.edu.fmpsc.neovia.model.dao;

import br.edu.fmpsc.neovia.model.Usuario;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class UsuarioDao implements Dao<Usuario> {

    @Override
    public void salvar(Usuario entidade) {
        inserir(entidade);
    }

    public void inserir(Usuario entidade) {
        String sql = "INSERT INTO usuario (nome, cpf, email, senha_hash) VALUES (?, ?, ?, ?)";
        try (Connection conexao = ConexaoBanco.obterConexao()) {
            garantirUnico(conexao, entidade);
            try (PreparedStatement comando = conexao.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                preencherDados(comando, entidade);
                comando.executeUpdate();
                try (ResultSet chaves = comando.getGeneratedKeys()) {
                    if (chaves.next()) {
                        entidade.setId(chaves.getLong(1));
                    }
                }
            }
        } catch (SQLException e) {
            throw traduzirErro(e);
        }
    }

    @Override
    public void atualizar(Usuario entidade) {
        String sql = "UPDATE usuario SET nome = ?, cpf = ?, email = ?, senha_hash = ? WHERE id = ?";
        try (Connection conexao = ConexaoBanco.obterConexao()) {
            garantirUnico(conexao, entidade);
            try (PreparedStatement comando = conexao.prepareStatement(sql)) {
                preencherDados(comando, entidade);
                comando.setLong(5, entidade.getId());
                if (comando.executeUpdate() == 0) {
                    throw new IllegalArgumentException("Usuário não encontrado.");
                }
            }
        } catch (SQLException e) {
            throw traduzirErro(e);
        }
    }

    @Override
    public void excluir(Long id) {
        String sql = "DELETE FROM usuario WHERE id = ?";
        try (Connection conexao = ConexaoBanco.obterConexao();
                PreparedStatement comando = conexao.prepareStatement(sql)) {
            comando.setLong(1, id);
            comando.executeUpdate();
        } catch (SQLException e) {
            throw traduzirErro(e);
        }
    }

    @Override
    public Usuario buscarPorId(Long id) {
        String sql = "SELECT id, nome, cpf, email, senha_hash FROM usuario WHERE id = ?";
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
    public List<Usuario> listarTodos() {
        String sql = "SELECT id, nome, cpf, email, senha_hash FROM usuario ORDER BY nome";
        List<Usuario> usuarios = new ArrayList<>();
        try (Connection conexao = ConexaoBanco.obterConexao();
                PreparedStatement comando = conexao.prepareStatement(sql);
                ResultSet resultado = comando.executeQuery()) {
            while (resultado.next()) {
                usuarios.add(mapear(resultado));
            }
            return usuarios;
        } catch (SQLException e) {
            throw traduzirErro(e);
        }
    }

    public Usuario buscarPorEmail(String email) {
        String sql = "SELECT id, nome, cpf, email, senha_hash FROM usuario WHERE email = ?";
        try (Connection conexao = ConexaoBanco.obterConexao();
                PreparedStatement comando = conexao.prepareStatement(sql)) {
            comando.setString(1, email);
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

    private void preencherDados(PreparedStatement comando, Usuario entidade) throws SQLException {
        comando.setString(1, entidade.getNome());
        comando.setString(2, entidade.getCpf());
        comando.setString(3, entidade.getEmail());
        comando.setString(4, entidade.getSenhaHash());
    }

    private void garantirUnico(Connection conexao, Usuario entidade) throws SQLException {
        if (existeOutro(conexao, "cpf", entidade.getCpf(), entidade.getId())) {
            throw new IllegalArgumentException("Já existe um usuário com este CPF.");
        }
        if (existeOutro(conexao, "email", entidade.getEmail(), entidade.getId())) {
            throw new IllegalArgumentException("Já existe um usuário com este e-mail.");
        }
    }

    private boolean existeOutro(Connection conexao, String coluna, String valor, Long idIgnorado) throws SQLException {
        String sql = "SELECT id FROM usuario WHERE " + coluna + " = ?";
        try (PreparedStatement comando = conexao.prepareStatement(sql)) {
            comando.setString(1, valor);
            try (ResultSet resultado = comando.executeQuery()) {
                while (resultado.next()) {
                    long idEncontrado = resultado.getLong("id");
                    if (idIgnorado == null || idEncontrado != idIgnorado) {
                        return true;
                    }
                }
                return false;
            }
        }
    }

    private Usuario mapear(ResultSet resultado) throws SQLException {
        Usuario usuario = new Usuario();
        usuario.setId(resultado.getLong("id"));
        usuario.setNome(resultado.getString("nome"));
        usuario.setCpf(resultado.getString("cpf"));
        usuario.setEmail(resultado.getString("email"));
        usuario.setSenhaHash(resultado.getString("senha_hash"));
        return usuario;
    }

    private RuntimeException traduzirErro(SQLException e) {
        if (e.getErrorCode() == 1062) {
            String detalhe = e.getMessage() == null ? "" : e.getMessage().toLowerCase();
            if (detalhe.contains("cpf")) {
                return new IllegalArgumentException("Já existe um usuário com este CPF.");
            }
            if (detalhe.contains("email")) {
                return new IllegalArgumentException("Já existe um usuário com este e-mail.");
            }
            return new IllegalArgumentException("CPF ou e-mail já cadastrado.");
        }
        return new RuntimeException("Erro ao acessar o banco de dados: " + e.getMessage(), e);
    }
}
