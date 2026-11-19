package br.edu.fmpsc.neovia.model;

import java.math.BigDecimal;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class CartaoCredito extends FormaPagamento {

    private static final DateTimeFormatter FORMATO_VALIDADE = DateTimeFormatter.ofPattern("MM/yyyy");

    private String nomeTitular;
    private String bandeira;
    private String ultimos4Digitos;
    private String validade;

    public CartaoCredito() {
    }

    public CartaoCredito(String nomeTitular, String bandeira, String numeroCartaoOuUltimos4, String validade) {
        this.nomeTitular = nomeTitular;
        this.bandeira = bandeira;
        setUltimos4Digitos(numeroCartaoOuUltimos4);
        this.validade = validade;
    }

    public String getNomeTitular() {
        return nomeTitular;
    }

    public void setNomeTitular(String nomeTitular) {
        this.nomeTitular = nomeTitular;
    }

    public String getBandeira() {
        return bandeira;
    }

    public void setBandeira(String bandeira) {
        this.bandeira = bandeira;
    }

    public String getUltimos4Digitos() {
        return ultimos4Digitos;
    }

    public void setUltimos4Digitos(String numeroCartaoOuUltimos4) {
        this.ultimos4Digitos = extrairUltimos4(numeroCartaoOuUltimos4);
    }

    public String getValidade() {
        return validade;
    }

    public void setValidade(String validade) {
        this.validade = validade;
    }

    @Override
    public boolean pagar(BigDecimal valor) {
        validarDados();
        if (valor == null || valor.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Informe um valor maior que zero para o cartão de crédito.");
        }
        return true;
    }

    @Override
    public String getTipo() {
        return "CARTAO_CREDITO";
    }

    public void validarDados() {
        if (nomeTitular == null || nomeTitular.isBlank()) {
            throw new IllegalArgumentException("Informe o nome do titular.");
        }
        if (bandeira == null || bandeira.isBlank()) {
            throw new IllegalArgumentException("Informe a bandeira do cartão.");
        }
        if (ultimos4Digitos == null || !ultimos4Digitos.matches("\\d{4}")) {
            throw new IllegalArgumentException("Informe os 4 últimos dígitos do cartão.");
        }
        if (validade == null || validade.isBlank()) {
            throw new IllegalArgumentException("Informe a validade do cartão (MM/AAAA).");
        }
        try {
            YearMonth validadeCartao = YearMonth.parse(validade.trim(), FORMATO_VALIDADE);
            if (validadeCartao.isBefore(YearMonth.now())) {
                throw new IllegalArgumentException("O cartão está vencido.");
            }
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("A validade deve estar no formato MM/AAAA.");
        }
    }

    private static String extrairUltimos4(String numero) {
        if (numero == null || numero.isBlank()) {
            return null;
        }
        String digitos = numero.replaceAll("\\D", "");
        if (digitos.length() < 4) {
            return digitos;
        }
        return digitos.substring(digitos.length() - 4);
    }
}
