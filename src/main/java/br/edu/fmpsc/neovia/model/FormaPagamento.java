package br.edu.fmpsc.neovia.model;

import java.math.BigDecimal;

/**
 * Forma de pagamento de uma recarga (classe ABSTRATA).
 *
 * Três ideias de POO aparecem aqui, em linguagem simples:
 *
 * 1) HERANÇA: esta classe herda de EntidadeBase (ganha o id).
 *    PagamentoPix e CartaoCredito vão herdar desta classe (extends FormaPagamento).
 *
 * 2) CLASSE ABSTRATA: não existe "pagamento genérico" na vida real, só Pix, cartão...
 *    Por isso não dá para fazer new FormaPagamento(). Ela só define o que toda
 *    forma de pagamento PRECISA saber fazer (os métodos abstratos abaixo),
 *    e cada subclasse é obrigada a escrever o seu jeito.
 *
 * 3) POLIMORFISMO: a variável é do tipo da classe-mãe, mas o objeto é de uma filha.
 *    Na hora de chamar pagar(), o Java executa o código da filha certa:
 *
 *        FormaPagamento forma = new PagamentoPix();   // ou new CartaoCredito()
 *        forma.pagar(valor);   // roda o pagar() do Pix; com cartão rodaria o do cartão
 *
 *    A Recarga não precisa saber qual é a forma: ela só chama forma.pagar(valor).
 *
 * Quem cria as subclasses: Gustavo (fase 2). Não altere esta classe sem falar com o Gabriel.
 */
public abstract class FormaPagamento extends EntidadeBase {

    /**
     * Realiza o pagamento do valor. Cada subclasse paga do seu jeito (sobrescreve este método).
     *
     * @return true se o pagamento foi aprovado, false se foi recusado
     */
    public abstract boolean pagar(BigDecimal valor);

    /**
     * Nome do tipo, como será gravado no banco: "PIX" ou "CARTAO_CREDITO".
     * O DAO usa este texto para saber qual subclasse criar ao ler do banco.
     */
    public abstract String getTipo();
}
