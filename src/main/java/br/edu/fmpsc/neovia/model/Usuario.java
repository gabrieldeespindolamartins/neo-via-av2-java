package br.edu.fmpsc.neovia.model;

/**
 * Passageiro que usa o NeoVia.
 * Segue o diagrama de classes (pacote Acesso e Conta).
 *
 * Herda o id de EntidadeBase, por isso não declara o id aqui.
 */
public class Usuario extends EntidadeBase {

    private String nome;
    private String cpf;
    private String email;

    // Guardamos o "hash" da senha, nunca a senha em texto puro (RNF12).
    // Hash = a senha embaralhada de um jeito que não dá para desfazer.
    // Quem implementa o cálculo do hash é a tela de Login/Cadastro (fase 2).
    private String senhaHash;

    /** Construtor vazio: útil para telas e para o DAO montarem o objeto aos poucos. */
    public Usuario() {
    }

    /** Construtor completo (sem id: o id é definido ao salvar). */
    public Usuario(String nome, String cpf, String email, String senhaHash) {
        this.nome = nome;
        this.cpf = cpf;
        this.email = email;
        this.senhaHash = senhaHash;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenhaHash() {
        return senhaHash;
    }

    public void setSenhaHash(String senhaHash) {
        this.senhaHash = senhaHash;
    }

    /** Texto mostrado quando o objeto aparece numa lista ou num System.out.println. */
    @Override
    public String toString() {
        return nome + " (id " + getId() + ")";
    }
}
