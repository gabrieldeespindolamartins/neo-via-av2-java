-- =====================================================================
-- NeoVia - estrutura do banco de dados (MySQL)
--
-- Como executar (MySQL Workbench): File > Open SQL Script > este arquivo
-- e clique no raio (Execute). Depois execute o dados.sql.
--
-- Por enquanto só existe a tabela usuario.
-- As demais tabelas (rota, trecho, cartao_transporte, recarga...) virão
-- do DER (Diagrama Entidade-Relacionamento) que a equipe está fazendo.
-- =====================================================================

-- utf8mb4 = aceita qualquer caractere (acentos, emojis...)
CREATE DATABASE IF NOT EXISTS neovia
    DEFAULT CHARACTER SET utf8mb4
    DEFAULT COLLATE utf8mb4_unicode_ci;

USE neovia;

-- Tabela do passageiro (classe Usuario, pacote Acesso e Conta)
CREATE TABLE IF NOT EXISTS usuario (
    id          BIGINT       NOT NULL AUTO_INCREMENT,  -- Long id da EntidadeBase
    nome        VARCHAR(100) NOT NULL,
    cpf         CHAR(11)     NOT NULL,                 -- só números, sem pontos e traço
    email       VARCHAR(150) NOT NULL,
    senha_hash  VARCHAR(255) NOT NULL,                 -- nunca a senha em texto puro (RNF12)
    PRIMARY KEY (id),
    UNIQUE KEY uk_usuario_cpf (cpf),
    UNIQUE KEY uk_usuario_email (email)
);
