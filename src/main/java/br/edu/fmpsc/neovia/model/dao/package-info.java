/**
 * DAO: acesso aos dados (onde e como os objetos são guardados).
 *
 * - Dao: interface com as operações do CRUD.
 * - DaoMemoria: implementação provisória em memória, para testar as telas.
 * - ConexaoBanco: dados de conexão com o MySQL.
 * - Cada módulo cria aqui o seu DAO com banco (ex.: CartaoDao implements Dao<CartaoTransporte>).
 */
package br.edu.fmpsc.neovia.model.dao;
