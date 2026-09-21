# PsiUnisantos - API Backend (clinica-api)

Este documento explica, passo a passo, como este projeto Spring Boot foi criado e como
rodá-lo localmente. Ele foi escrito pensando em quem nunca desenvolveu uma API em Java
com Spring Boot antes.

## 1. Como o projeto foi criado

O projeto **não foi escrito do zero**. Ele foi gerado usando o **Spring Initializr**
(https://start.spring.io), uma ferramenta oficial do Spring que já monta toda a estrutura
inicial de pastas, arquivos de configuração de build e as dependências escolhidas,
evitando que o time precise configurar tudo manualmente.

### 1.1. Configurações selecionadas no Spring Initializr

| Campo | Valor escolhido |
|---|---|
| Project | Gradle - Groovy |
| Language | Java |
| Spring Boot | 4.1.1 |
| Group | com.unisantos |
| Artifact | clinica-api |
| Package name | com.unisantos.clinica-api |
| Packaging | Jar |
| Configuration | Properties |
| Java | 25 |

### 1.2. Dependências selecionadas e o que cada uma faz

| Dependência | Para que serve |
|---|---|
| **Spring Web** | Permite criar endpoints REST (controllers) e usa o Tomcat embutido como servidor HTTP. É a base de qualquer API. |
| **Spring Data JPA** | Facilita salvar e consultar dados no banco relacional usando Java, sem escrever SQL manualmente na maior parte dos casos. Usa o Hibernate por baixo dos panos. |
| **MySQL Driver** | O "conector" que permite ao Java conversar com um banco de dados MySQL especificamente. |
| **Spring Security** | Framework de autenticação e controle de acesso. Protege os endpoints da API por padrão — é por causa dele que a API pede usuário e senha (mais detalhes na seção 4). |
| **Validation** | Permite validar campos de entrada automaticamente (ex: "este campo é obrigatório", "este e-mail precisa ser válido") usando anotações simples no código. |
| **Flyway Migration** | Controla e versiona as alterações no schema do banco de dados (criação de tabelas, colunas, etc.), como um "Git para o banco de dados". |
| **SpringDoc OpenAPI** | Gera automaticamente uma documentação interativa da API (Swagger UI), listando todos os endpoints disponíveis sem precisarmos escrever a documentação manualmente. |
| **Lombok** | Reduz código repetitivo em Java (getters, setters, construtores) usando anotações, deixando as classes mais enxutas. |
| **Spring Boot Actuator** | Expõe endpoints prontos para monitorar a saúde e as métricas da aplicação (ex: se ela está no ar, uso de memória, etc.). |
| **Prometheus** | Formata as métricas do Actuator no padrão que a ferramenta Prometheus entende, para monitoramento futuro. |
| **Testcontainers** | Permite rodar testes de integração usando um banco de dados real dentro de um container Docker temporário, em vez de simular o banco. |
| **Spring Boot DevTools** | Ferramenta de produtividade: reinicia a aplicação automaticamente sempre que um arquivo do código é alterado e salvo, sem precisar parar e rodar tudo de novo manualmente. |


### 1.3. Download e envio para o repositório

1. No site do Spring Initializr, com as configurações da seção 1.1 e as dependências da
   seção 1.2 selecionadas, clicou-se em **GENERATE**, o que baixou um arquivo `.zip`.
2. O conteúdo do `.zip` foi extraído dentro da pasta `server/` deste repositório
   (não em uma subpasta nova — os arquivos como `build.gradle`, `src/`, `gradlew` ficam
   direto dentro de `server/`).

## 2. Pré-requisitos para rodar o projeto

- Java 25 instalado (ou usar o ambiente já configurado no Codespaces do repositório)
- Docker instalado e rodando (necessário para o banco de dados MySQL local)

Não é necessário instalar o Gradle manualmente — o projeto usa o **Gradle Wrapper**
(`gradlew`), que baixa e usa a versão correta do Gradle sozinho.

## 3. Como rodar a aplicação localmente

1. Entre na pasta do backend:
   ```bash
   cd server
   ```
2. Dê permissão de execução ao wrapper do Gradle (só precisa fazer isso uma vez):
   ```bash
   chmod +x gradlew
   ```
3. Rode a aplicação:
   ```bash
   ./gradlew bootRun
   ```

Ao rodar esse comando, o Spring Boot detecta automaticamente o arquivo `compose.yaml`
presente na pasta `server/` (graças à dependência de suporte a Docker Compose incluída
no projeto) e sobe um container de banco de dados MySQL sozinho, sem você precisar fazer
nada manualmente. Aguarde até aparecer no terminal uma mensagem parecida com:

```
Started ClinicaApiApplication in X seconds
```

Isso confirma que a aplicação subiu com sucesso e está rodando na porta **8080**.

Para parar a aplicação (e o container do MySQL, que é derrubado junto), use `Ctrl+C`
no terminal onde o comando está rodando.

## 4. Autenticação: JWT, não mais usuário/senha do Spring

> **Nota histórica:** quando este projeto foi gerado, o Spring Security ainda
> não tinha configuração própria — então o Spring Boot aplicava o fallback de
> **HTTP Basic** com um usuário `user` e uma senha UUID gerada a cada boot
> (aquele popup do navegador + `Using generated security password` no log).
> Isso **não existe mais**: agora há um `SecurityConfig` real e a API é
> stateless via JWT — sem popup, sem sessão, sem senha gerada.

Hoje a autenticação funciona assim:

1. `POST /api/v1/auth/login` com email/senha de uma conta (o coordenador é
   seedado via `COORDINATOR_EMAIL` / `COORDINATOR_PASSWORD` do `.env`) —
   retorna um **access token** JWT (24h) e grava um **refresh token** em
   cookie httpOnly (7 dias).
2. Toda rota protegida exige `Authorization: Bearer <accessToken>` — sem ele
   a resposta é `403` (no navegador: página em branco, sem popup).
3. `POST /api/v1/auth/refresh` troca o cookie por um novo access token;
   `GET /api/v1/auth/me` devolve o perfil do usuário autenticado.

Endpoints públicos (sem token): `/auth/login`, `/auth/refresh`,
`/auth/logout`, Swagger UI, `/v3/api-docs` e `/actuator/health`.
`/auth/forgot-password` e `/auth/reset-password` **exigem token** — só o
dono da conta pode rotacionar a própria senha.

## 5. URLs disponíveis para teste

Com a aplicação rodando (`./gradlew bootRun` — lê o `.env` da raiz
automaticamente), os seguintes endereços ficam disponíveis:

| URL | O que mostra |
|---|---|
| http://localhost:8080/swagger-ui/index.html | **Swagger UI** — interface visual listando todos os endpoints da API |
| http://localhost:8080/v3/api-docs | Especificação OpenAPI em JSON (usada pelo Swagger UI) |
| http://localhost:8080/actuator/health | Saúde da aplicação (`{"status":"UP"}`) |

A API é REST pura — `http://localhost:8080/` sozinho retorna 403 (não há
página inicial; toda rota é protegida por JWT).

### Testando endpoints pelo Swagger UI

Rotas marcadas com cadeado exigem um **Bearer access token**:

1. Execute `POST /api/v1/auth/login` com o coordenador do `.env`
   (`COORDINATOR_EMAIL` / `COORDINATOR_PASSWORD`) e copie o `accessToken`
   da resposta.
2. Clique em **Authorize** (canto superior direito), cole o token e confirme.
3. Todas as chamadas seguintes já saem com `Authorization: Bearer <token>`
   — o cadeado indica exatamente quais rotas precisam disso.

Os endpoints públicos (`/auth/login`, `/auth/refresh`, `/auth/logout`) não
mostram o cadeado. `/auth/forgot-password` e `/auth/reset-password` exigem
token — só o dono da conta pode gerar/usar um reset token.

> Se estiver rodando dentro do GitHub Codespaces, substitua `http://localhost:8080`
> pela URL pública gerada automaticamente na aba **PORTS** do VS Code para a porta 8080.

## 6. Próximos passos

Este README cobre apenas a criação do projeto base e como executá-lo. À medida que os
módulos de Agenda e Sala Virtual forem implementados, novos endpoints e regras de
autenticação (RBAC) serão adicionados, substituindo esse usuário/senha temporário por
um mecanismo de login real.
