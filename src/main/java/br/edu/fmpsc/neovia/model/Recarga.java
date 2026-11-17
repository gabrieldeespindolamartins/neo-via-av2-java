package br.edu.fmpsc.neovia.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Recarga extends EntidadeBase {
    private BigDecimal valor;
    private LocalDateTime dataHora;
    private StatusRecarga status;
    private CartaoTransporte cartao;

    public Recarga(BigDecimal valor, LocalDateTime dataHora,
                   StatusRecarga status, CartaoTransporte cartao) {
        this(null, valor, dataHora, status, cartao);
    }

    public Recarga(Long id, BigDecimal valor, LocalDateTime dataHora,
                   StatusRecarga status, CartaoTransporte cartao) {
        setId(id);
        this.valor = valor;
        this.dataHora = dataHora;
        this.status = status;
        this.cartao = cartao;
    }

    public BigDecimal getValor() { return valor; }
    public void setValor(BigDecimal valor) { this.valor = valor; }
    public LocalDateTime getDataHora() { return dataHora; }
    public void setDataHora(LocalDateTime dataHora) { this.dataHora = dataHora; }
    public StatusRecarga getStatus() { return status; }
    public void setStatus(StatusRecarga status) { this.status = status; }
    public CartaoTransporte getCartao() { return cartao; }
    public void setCartao(CartaoTransporte cartao) { this.cartao = cartao; }
}