-- =====================================================================
-- NeoVia - estrutura do banco de dados (MySQL 8)
--
-- Baseado no DER do Pedro (revisado em 10/11), só com as 4 tabelas que
-- o professor exige na Av2: usuario, cartao_transporte, forma_pagamento
-- e recarga. O DER completo (rotas, viagens, extrato...) fica no
-- documento do PI.
--
-- Como executar (MySQL Workbench): File > Open SQL Script > este arquivo
-- e clique no raio (Execute). Depois execute o dados.sql.
--
-- ATENÇÃO: este script APAGA e recria as tabelas. Use só no banco de
-- desenvolvimento do seu computador.
-- =====================================================================

-- utf8mb4 = aceita qualquer caractere (acentos, emojis...)
CREATE DATABASE IF NOT EXISTS neovia
    DEFAULT CHARACTER SET utf8mb4
    DEFAULT COLLATE utf8mb4_unicode_ci;

USE neovia;

-- Apaga na ordem inversa das ligações (quem depende sai primeiro).
DROP TABLE IF EXISTS recarga;
DROP TABLE IF EXISTS forma_pagamento;
DROP TABLE IF EXISTS cartao_transporte;
DROP TABLE IF EXISTS usuario;

-- ---------------------------------------------------------------------
-- usuario: o passageiro (classe Usuario). CRUD completo do Gustavo.
-- ---------------------------------------------------------------------
CREATE TABLE usuario (
    id            BIGINT       NOT NULL AUTO_INCREMENT,  -- Long id da EntidadeBase
    nome          VARCHAR(100) NOT NULL,
    cpf           CHAR(11)     NOT NULL,                 -- só números, sem pontos e traço
    email         VARCHAR(150) NOT NULL,
    senha_hash    VARCHAR(255) NOT NULL,                 -- SHA-256 em hexadecimal, nunca a senha pura (RNF12)
    foto          VARCHAR(255) NULL,                     -- opcional (caminho da imagem)
    data_cadastro DATE         NOT NULL DEFAULT (CURRENT_DATE),
    PRIMARY KEY (id),
    UNIQUE KEY uk_usuario_cpf (cpf),
    UNIQUE KEY uk_usuario_email (email)                  -- o login procura pelo e-mail
);

-- ---------------------------------------------------------------------
-- cartao_transporte: cartão físico do ônibus (classe CartaoTransporte).
-- Isabela, fase 1.
-- ---------------------------------------------------------------------
CREATE TABLE cartao_transporte (
    id          BIGINT        NOT NULL AUTO_INCREMENT,
    usuario_id  BIGINT        NOT NULL,                  -- dono do cartão
    numero      VARCHAR(30)   NOT NULL,
    apelido     VARCHAR(50)   NULL,
    cidade      VARCHAR(80)   NOT NULL,
    operadora   VARCHAR(80)   NOT NULL,
    tipo        VARCHAR(20)   NOT NULL,                  -- enum TipoCartao
    saldo       DECIMAL(10,2) NOT NULL DEFAULT 0.00,     -- só muda pela recarga
    PRIMARY KEY (id),
    -- RN04: o mesmo número não repete para o mesmo usuário ("Cartão já existe")
    UNIQUE KEY uk_cartao_usuario_numero (usuario_id, numero),
    CONSTRAINT ck_cartao_tipo  CHECK (tipo IN ('ESTUDANTE', 'VALE_TRANSPORTE', 'COMUM')),
    CONSTRAINT ck_cartao_saldo CHECK (saldo >= 0),
    -- Se o usuário for excluído, os cartões dele saem junto (CASCADE).
    CONSTRAINT fk_cartao_usuario FOREIGN KEY (usuario_id)
        REFERENCES usuario (id) ON DELETE CASCADE
);

-- ---------------------------------------------------------------------
-- forma_pagamento: Pix e cartão de crédito numa tabela só.
-- A coluna tipo diz qual subclasse é (FormaPagamento.getTipo()).
-- As colunas que não se aplicam ficam NULL. Gustavo, fase 2.
-- ---------------------------------------------------------------------
CREATE TABLE forma_pagamento (
    id                  BIGINT       NOT NULL AUTO_INCREMENT,
    tipo                VARCHAR(20)  NOT NULL,           -- 'PIX' ou 'CARTAO_CREDITO'
    usuario_id          BIGINT       NULL,               -- dono do cartão de crédito (Pix fica NULL)
    codigo_copia_e_cola VARCHAR(255) NULL,               -- só Pix
    nome_titular        VARCHAR(100) NULL,               -- só cartão de crédito
    bandeira            VARCHAR(30)  NULL,               -- só cartão de crédito
    ultimos_4_digitos   CHAR(4)      NULL,               -- só os 4 últimos números (RNF14)
    validade            VARCHAR(7)   NULL,               -- formato MM/AAAA
    PRIMARY KEY (id),
    CONSTRAINT ck_forma_tipo CHECK (tipo IN ('PIX', 'CARTAO_CREDITO')),
    CONSTRAINT fk_forma_usuario FOREIGN KEY (usuario_id)
        REFERENCES usuario (id) ON DELETE SET NULL
);

-- ---------------------------------------------------------------------
-- recarga: crédito colocado num cartão (classe Recarga). Isabela, fase 2.
-- ---------------------------------------------------------------------
CREATE TABLE recarga (
    id                   BIGINT        NOT NULL AUTO_INCREMENT,
    cartao_transporte_id BIGINT        NOT NULL,
    forma_pagamento_id   BIGINT        NOT NULL,
    valor                DECIMAL(10,2) NOT NULL,
    data_hora            DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP,
    status               VARCHAR(20)   NOT NULL DEFAULT 'PENDENTE',  -- enum StatusRecarga
    PRIMARY KEY (id),
    CONSTRAINT ck_recarga_valor  CHECK (valor > 0),
    CONSTRAINT ck_recarga_status CHECK (status IN ('PENDENTE', 'CONFIRMADA', 'CANCELADA')),
    -- Se o cartão for excluído, as recargas dele saem junto.
    CONSTRAINT fk_recarga_cartao FOREIGN KEY (cartao_transporte_id)
        REFERENCES cartao_transporte (id) ON DELETE CASCADE,
    CONSTRAINT fk_recarga_forma FOREIGN KEY (forma_pagamento_id)
        REFERENCES forma_pagamento (id)
);
