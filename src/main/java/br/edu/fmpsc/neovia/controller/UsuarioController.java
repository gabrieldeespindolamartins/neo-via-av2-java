package br.edu.fmpsc.neovia.controller;

import br.edu.fmpsc.neovia.model.Usuario;
import br.edu.fmpsc.neovia.model.dao.UsuarioDao;
import br.edu.fmpsc.neovia.util.SenhaUtil;
import java.util.List;

public class UsuarioController {

    private static final UsuarioDao dao = new UsuarioDao();

    public void cadastrar(String nome, String cpf, String email, String senha) {
        Usuario usuario = new Usuario(
                validarNome(nome),
                validarCpf(cpf),
                validarEmail(email),
                SenhaUtil.gerarHash(validarSenhaObrigatoria(senha)));
        dao.salvar(usuario);
    }

    public void atualizar(Long id, String nome, String cpf, String email, String senha) {
        Usuario usuario = dao.buscarPorId(id);
        if (usuario == null) {
            throw new IllegalArgumentException("Usuário não encontrado.");
        }
        usuario.setNome(validarNome(nome));
        usuario.setCpf(validarCpf(cpf));
        usuario.setEmail(validarEmail(email));
        if (senha != null && !senha.isBlank()) {
            usuario.setSenhaHash(SenhaUtil.gerarHash(senha));
        }
        dao.atualizar(usuario);
    }

    public void excluir(Long id) {
        dao.excluir(id);
    }

    public Usuario buscarPorId(Long id) {
        return dao.buscarPorId(id);
    }

    public List<Usuario> listar() {
        return dao.listarTodos();
    }

    public Usuario autenticar(String email, String senha) {
        if (email == null || email.isBlank() || senha == null || senha.isBlank()) {
            throw new IllegalArgumentException("Informe o e-mail e a senha.");
        }
        Usuario usuario = dao.buscarPorEmail(email.trim());
        String hashInformado = SenhaUtil.gerarHash(senha);
        if (usuario == null || usuario.getSenhaHash() == null || !usuario.getSenhaHash().equals(hashInformado)) {
            throw new IllegalArgumentException("E-mail ou senha inválidos.");
        }
        return usuario;
    }

    private String validarNome(String nome) {
        if (nome == null || nome.isBlank()) {
            throw new IllegalArgumentException("Informe o nome.");
        }
        String limpo = nome.trim();
        if (limpo.length() > 100) {
            throw new IllegalArgumentException("O nome deve ter no máximo 100 caracteres.");
        }
        return limpo;
    }

    private String validarCpf(String cpf) {
        if (cpf == null || cpf.isBlank()) {
            throw new IllegalArgumentException("Informe o CPF.");
        }
        String numeros = cpf.replaceAll("\\D", "");
        if (!numeros.matches("\\d{11}")) {
            throw new IllegalArgumentException("O CPF deve ter 11 números.");
        }
        return numeros;
    }

    private String validarEmail(String email) {
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("Informe o e-mail.");
        }
        String limpo = email.trim();
        if (!limpo.contains("@") || limpo.startsWith("@") || limpo.endsWith("@") || limpo.contains(" ")) {
            throw new IllegalArgumentException("Informe um e-mail válido (com @).");
        }
        if (limpo.length() > 150) {
            throw new IllegalArgumentException("O e-mail deve ter no máximo 150 caracteres.");
        }
        return limpo;
    }

    private String validarSenhaObrigatoria(String senha) {
        if (senha == null || senha.isBlank()) {
            throw new IllegalArgumentException("Informe a senha.");
        }
        return senha;
    }
}
