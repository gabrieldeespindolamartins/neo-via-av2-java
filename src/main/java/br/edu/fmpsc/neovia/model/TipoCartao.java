package br.edu.fmpsc.neovia.model;

public enum TipoCartao {
    ESTUDANTE("Estudante"),
    VALE_TRANSPORTE("Vale-transporte"),
    COMUM("Comum");

    private final String descricao;

    TipoCartao(String descricao) {
        this.descricao = descricao;
    }

    @Override
    public String toString() {
        return descricao;
    }
}