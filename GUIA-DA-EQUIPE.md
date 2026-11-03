# Guia da equipe – NeoVia (Av2 de Java)

Leia este guia antes de começar. Qualquer dúvida, fale com o Gabriel. Não existe pergunta boba. 🙂

---

## 1. Quem faz o quê

| Fase | Período | Gustavo | Isabela |
|---|---|---|---|
| **Fase 1** | até **12/11** | **Rotas** (`TelaRotas`) | **Cadastro do cartão** (`TelaCartoes`) |
| **Fase 2** | **13 a 19/11** (só se a fase 1 estiver pronta) | **Cadastro/Login** (`TelaLogin`) | **Recarga** (`TelaRecarga`) |

- **Gabriel**: gestor. Criou a base, revisa o código de vocês e junta tudo na `main`.
- Entrega: **22/11**. Apresentação: **23/11**.

---

## 2. Quais classes cada um cria (fase 1)

As classes vêm do nosso **diagrama de classes**. Consultem o diagrama para os atributos de cada uma.
Toda classe de entidade **herda de `EntidadeBase`** (`extends EntidadeBase`), então **não declarem `id`**: ele já vem de lá.

### Gustavo – Rotas

| Pacote | Classe | Observação |
|---|---|---|
| `model` | `Local` | origem e destino da rota |
| `model` | `Rota` | origem, destino e trechos |
| `model` | `Trecho` | parte da rota feita em um ônibus |
| `model` | `LinhaOnibus` | linha usada em cada trecho |
| `model` | `Viagem` | para **salvar a rota confirmada** |
| `controller` | `RotaController` | ponte entre a tela e o DAO |
| `view` | `TelaRotas` | substitui a tela provisória |
| `dao` | `RotaDaoBanco` (e outros que precisar) | **só depois da aula de 09/11** |

> ⚠️ No diagrama, `Viagem` se liga ao `CartaoTransporte`, que é da Isabela. Para vocês não dependerem um do outro na fase 1, **a `Viagem` ainda não guarda o cartão**. Esse vínculo será feito depois que as duas partes estiverem na `main`.

### Isabela – Cartão de transporte

| Pacote | Classe | Observação |
|---|---|---|
| `model` | `CartaoTransporte` | ex.: número, apelido, cidade, operadora, tipo (ver diagrama) |
| `model` | `TipoCartao` | **enum** (lista fixa de tipos). Enum **não** herda de `EntidadeBase` |
| `controller` | `CartaoController` | ponte entre a tela e o DAO |
| `view` | `TelaCartoes` | substitui a tela provisória |
| `dao` | `CartaoDaoBanco` | **só depois da aula de 09/11** |

### Dicas para os dois

**Saber quem é o usuário** (para ligar a rota ou o cartão a ele):
```java
Usuario usuario = SessaoUsuario.getUsuarioLogado();   // na fase 1 é o "Usuário de Teste", id 1
```

**Testar sem banco**: usem o `DaoMemoria`. Deixem o DAO como `static` no controller, para os dados não sumirem quando a tela for fechada e aberta de novo:
```java
public class CartaoController {
    // static = um único DAO para o programa todo.
    // Depois do banco pronto, só esta linha muda: new CartaoDaoBanco()
    private static final Dao<CartaoTransporte> dao = new DaoMemoria<>();

    public void salvar(CartaoTransporte cartao) { dao.salvar(cartao); }
    public List<CartaoTransporte> listar() { return dao.listarTodos(); }
}
```

**Vai usar o editor visual do NetBeans?**
1. Apague o arquivo da tela provisória (ex.: `TelaRotas.java`).
2. Botão direito no pacote `view` > **New > JFrame Form** > nome **exatamente igual** (`TelaRotas`).
3. **Importante:** no painel *Properties* do JFrame, mude **defaultCloseOperation** para **DISPOSE**. O padrão do NetBeans é *EXIT_ON_CLOSE*, que fecha o programa inteiro quando você fecha a sua tela.

---

## 3. Passo a passo do Git (PowerShell)

### Primeira vez (só uma vez por computador)
```powershell
# Seu nome e e-mail aparecem em cada commit (use o e-mail da sua conta do GitHub)
git config --global user.name "Seu Nome"
git config --global user.email "seu-email@exemplo.com"
```

### 1) Clonar (baixar) o projeto
```powershell
cd $HOME\Documents          # ou outra pasta onde você guarda projetos
git clone https://github.com/gabrieldeespindolamartins/neo-via-av2-java.git
cd neo-via-av2-java
```

### 2) Criar a SUA branch
Branch = uma "cópia paralela" do projeto, só sua. O que você faz nela não afeta a `main` nem o colega.
```powershell
git checkout -b tela-rotas      # Gustavo
git checkout -b tela-cartao     # Isabela
```
Confira em qual branch você está com `git branch` (a sua aparece com `*`).

### 3) Trabalhar e fazer commits
Commit = um "ponto salvo" do seu trabalho, com uma mensagem dizendo o que mudou.
```powershell
git status                                   # mostra o que você alterou
git add .                                    # prepara todas as alterações
git commit -m "Cria classe Rota com origem e destino"
```

### 4) Enviar para o GitHub
```powershell
git push -u origin tela-rotas    # na PRIMEIRA vez (troque pelo nome da sua branch)
git push                         # nas próximas vezes
```

### 5) Pedir a revisão
No GitHub, abra um **Pull Request** da sua branch para a `main`. O Gabriel revisa e junta.

### Na fase 2
Atualize a `main` e crie uma branch nova a partir dela:
```powershell
git checkout main
git pull
git checkout -b tela-login      # Gustavo (sugestão de nome)
git checkout -b tela-recarga    # Isabela (sugestão de nome)
```

---

## 4. Regras da equipe

1. **Cada um mexe só nos próprios arquivos.** Precisa de algo no arquivo do colega? Fale com ele e com o Gabriel.
2. **Não edite sem falar com o Gabriel:** `TelaPrincipal`, `EntidadeBase`, `Dao`, `DaoMemoria`, `ConexaoBanco`, `SessaoUsuario`, `Usuario` e o `pom.xml`.
3. **Commits pequenos e frequentes**, com mensagens claras. Bom: `"Adiciona botão Excluir na TelaCartoes"`. Ruim: `"ajustes"`.
4. **Faça push pelo menos ao fim de cada dia de trabalho.** Se o computador der problema, o trabalho está salvo no GitHub.
5. **Nunca trabalhe direto na `main`.** Antes de começar, confira com `git branch`.
6. **Não adicione bibliotecas** ao projeto sem combinar com o Gabriel.
7. **Travou por mais de 30 minutos?** Peça ajuda. Isso economiza tempo de todo mundo.

---

## 5. Checklist de cada tela (antes de pedir revisão)

- [ ] **Cadastrar**: dá para criar um registro novo.
- [ ] **Ver**: os registros aparecem numa lista ou tabela (`JTable` ou `JList`).
- [ ] **Editar**: dá para alterar um registro já salvo.
- [ ] **Excluir**: dá para apagar um registro (de preferência pedindo confirmação).
- [ ] **Validação**: campos obrigatórios vazios mostram mensagem de erro (`JOptionPane`), sem travar o programa.
- [ ] **Dados salvos no banco** (depois de 09/11; antes disso, no `DaoMemoria`).
- [ ] **Separação MVC/DAO**:
  - `model`: classes com atributos, getters e setters, herdando de `EntidadeBase`;
  - `view`: a tela não tem SQL nem regra de negócio, só chama o controller;
  - `controller`: valida e chama o DAO;
  - `dao`: o único lugar que acessa os dados.
- [ ] Fechar a sua tela **não fecha** o programa inteiro (DISPOSE).
- [ ] O projeto compila e roda sem erros (**F6**).
- [ ] Tudo commitado e enviado (`git status` sem pendências, `git push` feito).
