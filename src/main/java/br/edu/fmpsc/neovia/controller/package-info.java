/**
 * CONTROLLER: a "ponte" entre a tela (view) e os dados (dao).
 *
 * A tela NÃO fala direto com o DAO. Ela chama o controller, que:
 * 1. valida os dados digitados (campos vazios, valores inválidos...);
 * 2. monta o objeto do model (ex.: new Rota(...));
 * 3. pede ao DAO para salvar, listar, atualizar ou excluir;
 * 4. devolve o resultado (ou uma mensagem de erro) para a tela.
 *
 * Cada módulo cria o seu controller aqui. Exemplos:
 * RotaController (Gustavo), CartaoController (Isabela).
 *
 * (Este arquivo package-info.java só documenta o pacote e mantém a pasta
 * no Git enquanto ela não tem outras classes.)
 */
package br.edu.fmpsc.neovia.controller;
