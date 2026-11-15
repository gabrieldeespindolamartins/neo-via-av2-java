-- =====================================================================
-- NeoVia - dados iniciais para teste
-- Executar DEPOIS do schema.sql.
-- =====================================================================

USE neovia;

-- "Usuário de Teste": o mesmo que a util.SessaoUsuario usa (id = 1).
-- Login para testar: e-mail teste@neovia.com / senha 123456
-- A senha_hash é o SHA-256 de "123456", em hexadecimal minúsculo.
-- O login do Gustavo precisa gerar o hash do mesmo jeito para a senha bater.
INSERT INTO usuario (id, nome, cpf, email, senha_hash)
VALUES (1, 'Usuário de Teste', '00000000000', 'teste@neovia.com',
        '8d969eef6ecad3c29a3a629280e686cf0c3f5d5a86aff3ca12020c923adc6c92');

-- Um cartão do Usuário de Teste, com saldo zero, para testar a Recarga.
INSERT INTO cartao_transporte (usuario_id, numero, apelido, cidade, operadora, tipo, saldo)
VALUES (1, '1234567890', 'Cartão principal', 'Palhoça', 'Operadora Teste', 'COMUM', 0.00);
