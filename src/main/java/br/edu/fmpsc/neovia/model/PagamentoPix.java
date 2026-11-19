package br.edu.fmpsc.neovia.model;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.UUID;

public class PagamentoPix extends FormaPagamento {

    private String codigoCopiaECola;

    public PagamentoPix() {
    }

    public String getCodigoCopiaECola() {
        return codigoCopiaECola;
    }

    public void setCodigoCopiaECola(String codigoCopiaECola) {
        this.codigoCopiaECola = codigoCopiaECola;
    }

    public String gerarCodigo(BigDecimal valor) {
        if (valor == null || valor.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Informe um valor maior que zero para o Pix.");
        }
        String valorFormatado = valor.setScale(2, RoundingMode.HALF_UP).toPlainString();
        codigoCopiaECola = "00020126580014BR.GOV.BCB.PIX0136"
                + UUID.randomUUID().toString().replace("-", "")
                + "520400005303986540"
                + valorFormatado.length()
                + valorFormatado
                + "5802BR5925NEOVIA TRANSPORTE LTDA6008PALHOCA62070503***6304ABCD";
        return codigoCopiaECola;
    }

    @Override
    public boolean pagar(BigDecimal valor) {
        gerarCodigo(valor);
        return true;
    }

    @Override
    public String getTipo() {
        return "PIX";
    }
}
