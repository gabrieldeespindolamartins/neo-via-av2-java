# Guia da equipe – NeoVia (Av2 de Java)

Leia este guia antes de começar. Qualquer dúvida, fale com o Gabriel. Não existe pergunta boba. 🙂

---

## 1. Quem faz o quê

| Fase | Período | Gustavo | Isabela |
|---|---|---|---|
| **Fase 1** | **10 a 15/11** | **Usuário (CRUD) e Login** | **Cartão de transporte (cadastro e saldo)** |
| **Fase 2** | **16 a 19/11** | **PagamentoPix e CartaoCredito** | **Recarga** |

Datas importantes:

| Data | O que acontece |
|---|---|
| **20/11** | Revisão e junção de tudo na `main` |
| **22/11** | **Entrega** |
| **23/11** | **Apresentação** |

- **Gabriel**: gestão, base do projeto e revisão do código.
- **Pedro**: DER do banco de dados e slides da apresentação.

O que o professor exige (resumo): pacotes `model`, `view`, `controller` e `model.dao`; CRUD completo de `Usuario`; cadastro de `CartaoTransporte`, consulta de saldo e `Recarga`; classe abstrata `FormaPagamento` com `PagamentoPix` e `CartaoCredito` (método `pagar()` chamado de forma polimórfica); navegação **Login → Menu → Cadastro/Recarga**; **JDBC puro, sem Hibernate**. Rotas **saiu** do escopo.

---

## 2. Quais classes cada um cria

Toda entidade **herda de `EntidadeBase`** (`extends EntidadeBase`), então **não declarem `id`**: ele já vem de lá. Os DAOs com banco ficam no pacote `model.dao` e usam a `ConexaoBanco` (JDBC puro).

### Gustavo – Fase 1 (branch `tela-usuario`)

| Pacote | Classe | Observação |
|---|---|---|
| `model.dao` | `UsuarioDao` | CRUD de usuário (implementa `Dao<Usuario>`) |
| `controller` | `UsuarioController` | ponte entre as telas e o DAO |
| `view` | `TelaUsuarios` | listagem em `JTable`, substitui a provisória |
| `view` | `TelaCadastroUsuario` | formulário para **cadastrar e editar** |
| `view` | `TelaLogin` | login de verdade, substitui a provisória |
| `util` ou `controller` | hash da senha | **SHA-256** (nunca guardar a senha em texto puro) |

### Isabela – Fase 1 (branch `tela-cartao`)

| Pacote | Classe | Observação |
|---|---|---|
| `model` | `TipoCartao` | **enum** (lista fixa de tipos). Enum **não** herda de `EntidadeBase` |
| `model` | `CartaoTransporte` | número, apelido, tipo, saldo, usuário dono |
| `model.dao` | `CartaoDao` | `inserir`, `listarPorUsuario`, `buscarPorId`, `atualizarSaldo` |
| `controller` | `CartaoController` | ponte entre a tela e o DAO |
| `view` | `TelaCartoes` | cadastro, listagem e saldo, substitui a provisória |

### Gustavo – Fase 2 (branch `pagamentos`)

| Pacote | Classe | Observação |
|---|---|---|
| `model` | `PagamentoPix` | `extends FormaPagamento`, sobrescreve `pagar()` e `getTipo()` ("PIX") |
| `model` | `CartaoCredito` | `extends FormaPagamento`, sobrescreve `pagar()` e `getTipo()` ("CARTAO_CREDITO") |
| `model.dao` | `FormaPagamentoDao` | grava e lê as formas de pagamento (usa `getTipo()`) |

### Isabela – Fase 2 (branch `tela-recarga`)

| Pacote | Classe | Observação |
|---|---|---|
| `model` | `StatusRecarga` | **enum** (ex.: PENDENTE, APROVADA, RECUSADA) |
| `model` | `Recarga` | cartão, valor, forma de pagamento, status, data |
| `model.dao` | `RecargaDao` | grava e lista recargas |
| `controller` | `RecargaController` | chama `forma.pagar(valor)` e atualiza o saldo |
| `view` | `TelaRecarga` | substitui a tela provisória |

### Polimorfismo na recarga (o que o professor quer ver)
```java
FormaPagamento forma = new PagamentoPix();   // ou new CartaoCredito()
boolean aprovado = forma.pagar(valor);       // cada classe paga do seu jeito
```
A explicação completa está nos comentários da classe `FormaPagamento`.

### Dicas para todos

**Saber quem é o usuário logado:**
```java
Usuario usuario = SessaoUsuario.getUsuarioLogado();
```
Até 19/11 existe um "Usuário de Teste" (id 1) automático; depois disso o login real é obrigatório.

**Testar sem banco**: use o `DaoMemoria`. Deixe o DAO como `static` no controller, para os dados não sumirem quando a tela for fechada e aberta de novo:
```java
public class CartaoController {
    // static = um único DAO para o programa todo.
    // Com o banco pronto, só esta linha muda: new CartaoDao()
    private static final Dao<CartaoTransporte> dao = new DaoMemoria<>();
}
```

**Vai usar o editor visual do NetBeans?**
1. Apague o arquivo da tela provisória (ex.: `TelaCartoes.java`).
2. Botão direito no pacote `view` > **New > JFrame Form** > nome **exatamente igual** (`TelaCartoes`).
3. **Importante:** no painel *Properties*, mude **defaultCloseOperation** para **DISPOSE**. O padrão do NetBeans é *EXIT_ON_CLOSE*, que fecha o programa inteiro. (Só a `TelaLogin` e a `TelaPrincipal` encerram o programa.)

---

## 3. Passo a passo do Git (PowerShell)

Nomes das branches: **`tela-usuario`** (Gustavo, fase 1), **`tela-cartao`** (Isabela, fase 1), **`pagamentos`** (Gustavo, fase 2) e **`tela-recarga`** (Isabela, fase 2).

### Primeira vez (só uma vez por computador)
```powershell
git config --global user.name "Seu Nome"
git config --global user.email "seu-email@exemplo.com"
git clone https://github.com/gabrieldeespindolamartins/neo-via-av2-java.git
cd neo-via-av2-java
```

### 1) Antes de começar: atualizar a `main`
```powershell
git checkout main
git pull
```

### 2) Criar a SUA branch
Branch = uma "cópia paralela" do projeto, só sua. O que você faz nela não afeta a `main` nem o colega.
```powershell
git checkout -b tela-usuario     # troque pelo nome da sua branch
git branch                       # confira: a branch atual aparece com *
```

### 3) Trabalhar e fazer commits
Commit = um "ponto salvo" do seu trabalho, com uma mensagem dizendo o que mudou.
```powershell
git status                                   # mostra o que você alterou
git add .                                    # prepara todas as alterações
git commit -m "Cria UsuarioDao com inserir e listar"
```

### 4) Enviar para o GitHub (push)
```powershell
git push -u origin tela-usuario   # PRIMEIRA vez (troque pelo nome da sua branch)
git push                          # nas próximas vezes
```

### 5) Pedir a revisão
No GitHub, abra um **Pull Request** da sua branch para a `main`. O Gabriel revisa e junta.

### Na fase 2
Volte ao passo 1 (`git checkout main` + `git pull`) e crie a branch nova: `pagamentos` (Gustavo) ou `tela-recarga` (Isabela).

---

## 4. Regras da equipe

1. **Cada um mexe só nos próprios arquivos.** Precisa de algo no arquivo do colega? Fale com ele e com o Gabriel.
2. **Só altere falando com o Gabriel:** `TelaPrincipal`, `EntidadeBase`, `FormaPagamento`, `Usuario`, `SessaoUsuario`, tudo de `model.dao` que já existe (`Dao`, `DaoMemoria`, `ConexaoBanco`), o `pom.xml` e os arquivos `.sql` da pasta `database`.
3. **Commits pequenos e frequentes**, com mensagens claras. Bom: `"Adiciona botão Excluir na TelaCartoes"`. Ruim: `"ajustes"`.
4. **Faça push pelo menos ao fim de cada dia de trabalho.**
5. **Nunca trabalhe direto na `main`.** Antes de começar, confira com `git branch`.
6. **Não adicione bibliotecas** (sem Hibernate: é JDBC puro).
7. **Travou por mais de 30 minutos?** Peça ajuda.

---

## 5. Checklist de cada tela (antes de pedir revisão)

Segue os critérios do professor:

- [ ] **Formulário de cadastro**: dá para criar um registro novo.
- [ ] **Listagem em `JTable`**: os registros aparecem numa tabela.
- [ ] **Editar**: dá para alterar um registro já salvo.
- [ ] **Excluir com confirmação**: pergunta "Tem certeza?" (`JOptionPane.showConfirmDialog`) antes de apagar.
- [ ] **Validação de campos obrigatórios**: campo vazio mostra mensagem, sem travar.
- [ ] **Validação de tipos**: número, CPF, e-mail e valores em formato correto (ex.: `new BigDecimal(texto)` dentro de `try/catch`).
- [ ] **Tratamento de erros do banco**: `try/catch` de `SQLException` com mensagem para o usuário (nada de erro só no console).
- [ ] **Mensagens de sucesso e erro** com `JOptionPane`.
- [ ] **Botão Voltar** que fecha a tela e volta ao menu.
- [ ] **Fechar a tela não fecha o programa** (`DISPOSE_ON_CLOSE`).
- [ ] **Separação MVC/DAO**:
  - `model`: atributos, getters e setters, herdando de `EntidadeBase`;
  - `view`: a tela não tem SQL nem regra de negócio, só chama o controller;
  - `controller`: valida e chama o DAO;
  - `model.dao`: o único lugar que acessa o banco (JDBC com `ConexaoBanco`).
- [ ] O projeto compila e roda sem erros (**F6**).
- [ ] Tudo commitado e enviado (`git status` sem pendências, `git push` feito).
