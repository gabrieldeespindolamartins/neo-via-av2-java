-- =====================================================================
-- NeoVia - dados iniciais para teste
-- Executar DEPOIS do schema.sql.
-- =====================================================================

USE neovia;

-- "Usuário de Teste": o mesmo que a classe util.SessaoUsuario devolve
-- enquanto a tela de login não existe (fase 1). Precisa ter id = 1.
-- A senha_hash é um texto provisório (não é um hash real); será trocada
-- quando o cadastro/login da fase 2 definir como o hash é gerado.
INSERT INTO usuario (id, nome, cpf, email, senha_hash)
VALUES (1, 'Usuário de Teste', '00000000000', 'teste@neovia.com', 'hash-provisorio');
