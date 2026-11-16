package br.edu.fmpsc.neovia.model;

import java.math.BigDecimal;

public class CartaoTransporte extends EntidadeBase {
    private String numero;
    private String apelido;
    private String cidade;
    private String operadora;
    private TipoCartao tipo;
    private BigDecimal saldo; // private: só muda por creditar() e debitar()
    private Usuario usuario;

    // Cartão novo: sem id (ainda não foi salvo) e o saldo começa em zero
    public CartaoTransporte(String numero, String apelido, String cidade,
                            String operadora, TipoCartao tipo, Usuario usuario) {
        this(null, numero, apelido, cidade, operadora, tipo, BigDecimal.ZERO, usuario);
    }

    // Cartão que já existe no banco (usado pelo DAO)
    public CartaoTransporte(Long id, String numero, String apelido, String cidade,
                            String operadora, TipoCartao tipo, BigDecimal saldo, Usuario usuario) {
        setId(id);
        this.numero = numero;
        this.apelido = apelido;
        this.cidade = cidade;
        this.operadora = operadora;
        this.tipo = tipo;
        this.saldo = saldo;
        this.usuario = usuario;
    }

    public BigDecimal consultarSaldo() {
        return saldo;
    }

    public void creditar(BigDecimal valor) {
        if (valor == null || valor.signum() <= 0) {
            throw new IllegalArgumentException("O valor do crédito deve ser maior que zero.");
        }
        saldo = saldo.add(valor);
    }

    public void debitar(BigDecimal valor) {
        if (valor == null || valor.signum() <= 0) {
            throw new IllegalArgumentException("O valor do débito deve ser maior que zero.");
        }
        if (saldo.compareTo(valor) < 0) {
            throw new IllegalArgumentException("Saldo insuficiente.");
        }
        saldo = saldo.subtract(valor);
    }

    // Getters e setters (de propósito, NÃO existe setSaldo)
    public String getNumero() { return numero; }
    public void setNumero(String numero) { this.numero = numero; }
    public String getApelido() { return apelido; }
    public void setApelido(String apelido) { this.apelido = apelido; }
    public String getCidade() { return cidade; }
    public void setCidade(String cidade) { this.cidade = cidade; }
    public String getOperadora() { return operadora; }
    public void setOperadora(String operadora) { this.operadora = operadora; }
    public TipoCartao getTipo() { return tipo; }
    public void setTipo(TipoCartao tipo) { this.tipo = tipo; }
    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }
}