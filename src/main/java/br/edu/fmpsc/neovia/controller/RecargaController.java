package br.edu.fmpsc.neovia.controller;

import br.edu.fmpsc.neovia.model.CartaoTransporte;
import br.edu.fmpsc.neovia.model.FormaPagamento;
import br.edu.fmpsc.neovia.model.Recarga;
import br.edu.fmpsc.neovia.model.StatusRecarga;
import br.edu.fmpsc.neovia.model.dao.CartaoDao;
import br.edu.fmpsc.neovia.model.dao.RecargaDao;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public class RecargaController {
    private final CartaoDao cartaoDao = new CartaoDao();
    private final RecargaDao recargaDao = new RecargaDao();

    // IllegalArgumentException = erro de validação; IllegalStateException = erro do banco
    public Recarga confirmar(CartaoTransporte cartao, String valorTexto, FormaPagamento forma) {
        if (cartao == null) {
            throw new IllegalArgumentException("Escolha um cartão.");
        }
        if (forma == null) {
            throw new IllegalArgumentException("Escolha a forma de pagamento.");
        }
        BigDecimal valor = lerValor(valorTexto);

        // CONFERIR: assinatura do pagar() no FormaPagamento (aqui suponho boolean pagar(BigDecimal))
        boolean aprovado = forma.pagar(valor); // polimorfismo: Pix ou crédito, a mesma chamada
        if (!aprovado) {
            throw new IllegalArgumentException("Pagamento não aprovado.");
        }

        cartao.creditar(valor);
        cartaoDao.atualizarSaldo(cartao);

        Recarga recarga = new Recarga(valor, LocalDateTime.now(), StatusRecarga.CONFIRMADA, cartao);
        recargaDao.inserir(recarga);
        return recarga;
    }

    private BigDecimal lerValor(String texto) {
        if (texto == null || texto.isBlank()) {
            throw new IllegalArgumentException("Informe o valor.");
        }
        BigDecimal valor;
        try {
            valor = new BigDecimal(texto.trim().replace(",", "."));
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Valor inválido.");
        }
        if (valor.signum() <= 0) {
            throw new IllegalArgumentException("O valor deve ser maior que zero.");
        }
        return valor;
    }
}