# NeoVia 🚌

Sistema desktop em Java (Swing) para o **passageiro de transporte público**: gerenciar o **usuário** (CRUD e login), cadastrar o **cartão de transporte**, consultar o saldo e fazer **recargas** (pagando com Pix ou cartão de crédito).
É a Av2 da disciplina de Programação I (POO) e é baseado no nosso Projeto Integrador. Usa o padrão **MVC** com **DAO** e CRUD com banco de dados MySQL.

---

## 1. O que você precisa ter instalado

| Programa | Para quê |
|---|---|
| **JDK 17 ou mais novo** | Compilar e rodar o Java. Confira no PowerShell: `java -version` |
| **Apache NetBeans** | Abrir, editar e rodar o projeto |
| **Git** | Baixar o projeto e enviar as suas alterações. Confira: `git --version` |
| **MySQL** | Banco de dados. Enquanto o DAO com banco não existe, as telas usam dados em memória |

---

## 2. Como abrir no NetBeans e rodar

1. Baixe o projeto (o passo a passo do Git está no `GUIA-DA-EQUIPE.md`).
2. No NetBeans: **File > Open Project** e selecione a pasta `neo-via-av2-java` (a que tem o arquivo `pom.xml`).
3. Na primeira vez, o NetBeans baixa o driver do MySQL pela internet. Espere a barra de progresso no canto inferior terminar.
4. Clique em **Run Project** (botão ▶ verde ou **F6**). A **tela de login** abre primeiro. Clique em *Entrar como Usuário de Teste* para ir ao menu, que tem 4 botões (Usuários, Meus cartões, Recarga e Sair).

> **Deu erro de "Java version"?** Clique com o botão direito no projeto > **Properties > Build > Compile** e confira se o *Java Platform* é o JDK 17 ou mais novo.

---

## 3. Estrutura de pastas

```
neo-via-av2-java/
├── pom.xml                      ← "receita" do projeto (versão do Java, bibliotecas, classe principal)
├── README.md                    ← este arquivo
├── GUIA-DA-EQUIPE.md            ← quem faz o quê, passo a passo do Git e regras
├── database/
│   ├── schema.sql               ← cria o banco e as tabelas
│   └── dados.sql                ← dados iniciais (Usuário de Teste)
└── src/
    ├── main/java/br/edu/fmpsc/neovia/
    │   ├── Main.java            ← ponto de partida: abre a tela de login
    │   ├── model/               ← as "coisas" do sistema (Usuario, CartaoTransporte, Recarga, FormaPagamento...)
    │   │   └── dao/             ← quem salva e busca os dados (memória ou banco). Fica DENTRO de model
    │   ├── view/                ← as telas (o que o usuário vê e clica)
    │   ├── controller/          ← a ponte entre a tela e os dados
    │   └── util/                ← ajudantes usados por todos (ex.: quem está logado)
    └── test/java/br/edu/fmpsc/neovia/
        └── model/dao/DaoMemoriaTeste.java  ← teste rápido do DAO em memória
```

### O que já existe em cada pasta

| Arquivo | O que é |
|---|---|
| `model/EntidadeBase` | Classe-mãe de todas as entidades. Dá o `id` para todo mundo (herança). |
| `model/Usuario` | O passageiro: nome, CPF, e-mail e senha (guardada como hash). |
| `model/FormaPagamento` | Classe **abstrata** das formas de pagamento (`pagar()` e `getTipo()`). `PagamentoPix` e `CartaoCredito` herdam dela (fase 2). |
| `model/dao/Dao` | Interface com as 5 operações do CRUD: `salvar`, `atualizar`, `excluir`, `buscarPorId`, `listarTodos`. |
| `model/dao/DaoMemoria` | Implementação provisória do `Dao`, que guarda tudo numa lista. Serve para testar as telas antes do banco. |
| `model/dao/ConexaoBanco` | Dados de conexão com o MySQL, num lugar só. **JDBC puro, sem Hibernate.** |
| `util/SessaoUsuario` | Guarda quem está logado. Se ninguém fez login, devolve o "Usuário de Teste" (id 1) até 19/11. |
| `view/TelaLogin` | **Primeira tela do sistema.** Provisória, com o botão "Entrar como Usuário de Teste". Fechar ela encerra o programa. |
| `view/TelaPrincipal` | Menu: Usuários, Meus cartões, Recarga e Sair. Feita à mão (sem `.form`). **Não editar.** |
| `view/TelaUsuarios`, `TelaCartoes`, `TelaRecarga` | Telas provisórias. Cada uma será substituída pelo seu responsável. |

---

## 4. O padrão MVC, explicado de forma simples

Pense num **restaurante**:

| No restaurante | No sistema | Pasta |
|---|---|---|
| O **salão e o cardápio**: onde o cliente faz o pedido | **View** (tela) | `view/` |
| O **garçom**: confere o pedido e leva para a cozinha | **Controller** | `controller/` |
| A **cozinha e a despensa**: onde a comida é guardada e preparada | **DAO** (+ banco de dados) | `model/dao/` |
| O **prato** que vai e volta | **Model** (o objeto com os dados) | `model/` |

O cliente nunca entra na cozinha. Do mesmo jeito, **a tela nunca fala direto com o banco**: ela sempre passa pelo controller.

### Exemplo: o caminho de um clique

O usuário preenche os dados de um cartão e clica em **Salvar**:

```
[1] TelaCartoes (view)         → clique no botão "Salvar"
        │  chama controller.salvar(numero, apelido, ...)
        ▼
[2] CartaoController           → confere se os campos estão preenchidos
        │  cria o objeto: new CartaoTransporte(...)   ← model
        │  chama dao.salvar(cartao)
        ▼
[3] Dao<CartaoTransporte>      → hoje: DaoMemoria (guarda na lista)
        │                        depois: CartaoDao (faz o INSERT)
        ▼
[4] Banco MySQL                → tabela cartao_transporte
```

Em código, a ideia é esta (simplificada):

```java
// Na tela (view): só pega o que foi digitado e chama o controller
botaoSalvar.addActionListener(e -> controller.salvar(campoNumero.getText(), campoApelido.getText()));

// No controller: valida, monta o objeto e manda o DAO salvar
public void salvar(String numero, String apelido) {
    if (numero.isBlank()) {
        throw new IllegalArgumentException("Informe o número do cartão.");
    }
    CartaoTransporte cartao = new CartaoTransporte(numero, apelido);
    dao.salvar(cartao);   // o controller não sabe se é memória ou banco
}
```

### Por que separar assim?
- **Cada parte tem uma única responsabilidade**: fica mais fácil achar e corrigir erros.
- **Dá para trocar uma parte sem mexer nas outras**: trocar a lista em memória pelo MySQL não muda nenhuma tela.
- **A equipe trabalha em paralelo** sem pisar no código do colega.

---

## 5. Teste rápido do DAO

No NetBeans, abra `src/test/java/.../model/dao/DaoMemoriaTeste.java`, clique com o botão direito > **Run File** (Shift+F6).
A última linha deve ser `TODOS OS TESTES PASSARAM`.

---

## 6. Banco de dados

1. Abra o MySQL Workbench.
2. Execute `database/schema.sql` (cria o banco `neovia` e a tabela `usuario`).
3. Execute `database/dados.sql` (cria o Usuário de Teste, id 1).
4. Ajuste usuário e senha do MySQL em `model/dao/ConexaoBanco.java` (só o Gabriel altera esse arquivo).

As tabelas `cartao_transporte`, `recarga` e `forma_pagamento` entram após a revisão do DER (11/11).
