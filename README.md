# PsiUnisantos — Sistema de Gestão da Clínica Escola de Psicologia

Monorepo que unifica o cliente e o servidor, organizado em domínios de negócio para facilitar o trabalho paralelo das squads.

```
├── client/   React 19 + TypeScript + Vite + Material UI (Bun)
├── server/   Spring Boot 4 + Java 25 + MySQL + Flyway + JWT (Gradle)
└── e2e       client/e2e/ — Playwright, estrutura feature-based
```

---

## Pré-requisitos

| Ferramenta                | Versão                    |
| ------------------------- | -------------------------- |
| Docker + Docker Compose   | qualquer recente           |
| Java                      | 25                         |
| Gradle                    | via`./gradlew` (wrapper) |
| Bun (ou Node ≥ 20 + npm) | 1.x                        |

## Configuração de ambiente

```bash
cp .env.example .env   # preencha senhas fortes — nunca as do exemplo
```

Variáveis essenciais (ver `.env.example` para a lista completa):

| Variável                                        | Uso                                                                                                                                                |
| ------------------------------------------------ | -------------------------------------------------------------------------------------------------------------------------------------------------- |
| `MYSQL_*`                                      | credenciais do banco (compose)                                                                                                                     |
| `COORDINATOR_EMAIL` / `COORDINATOR_PASSWORD` | **obrigatórias** — criam a única conta coordenadora no primeiro boot. Não há cadastro público; sem elas nenhuma conta admin é criada. |
| `JWT_SECRET`                                   | assinatura dos tokens — gere com`openssl rand -base64 48`                                                                                       |
| `VITE_API_URL`                                 | URL da API vista pelo client (default`http://localhost:8080`)                                                                                    |

> **Segurança:** nenhuma credencial real é commitada — `application.yml` lê tudo de variáveis de ambiente. O `.env` está no `.gitignore`.

---

## Rodando o projeto

### Opção A — Docker Compose (stack completa)

Sobe MySQL + backend + client em containers:

```bash
docker compose up --build
```

* Client: http://localhost:3000
* API: http://localhost:8080
* Swagger UI: http://localhost:8080/swagger-ui/index.html

### Opção B — Local (dev com hot-reload)

```bash
# 1. Banco de dados
docker compose up -d mysql        # ou: cd server && docker compose up -d

# 2. Backend (porta 8080, devtools com hot-reload)
cd server
COORDINATOR_EMAIL=... COORDINATOR_PASSWORD=... ./gradlew bootRun

# 3. Frontend (porta 5173, Vite HMR)
cd client
bun install
bun run dev
```

* Client: http://localhost:5173
* API docs: http://localhost:8080/swagger-ui/index.html

**Porta ocupada?** O `e2e:stack` (ou um processo antigo) pode estar segurando a porta:

```bash
kill $(lsof -ti:8080)   # libera a API
kill $(lsof -ti:5173)   # libera o Vite
kill $(lsof -ti:3000)   # libera o frontend do compose
bun run e2e:stack:down  # ou pare tudo que o e2e:stack subiu (dentro de client/)
```

---

## Autenticação e sessão

JWT stateless + refresh token rotativo. Não existe cadastro público — a única
conta criada automaticamente é a do coordenador (via `COORDINATOR_*`).

| Credencial                | Onde fica                                                      | Validade                               |
| ------------------------- | -------------------------------------------------------------- | -------------------------------------- |
| Access token (JWT Bearer) | **memória** do React (nunca em localStorage/cookie)     | 24h (`JWT_EXPIRATION_MS`)            |
| Refresh token             | **cookie httpOnly** (`/api/v1/auth`), `SameSite=Lax` | 7 dias (`JWT_REFRESH_EXPIRATION_MS`) |

Como funciona:

1. `POST /auth/login` → retorna o access token no corpo + grava o refresh
   token num cookie httpOnly (o front nunca lê esse cookie — XSS não o alcança).
2. Toda chamada da API leva `Authorization: Bearer <accessToken>`.
3. **Expiração do access token**: o interceptor do axios pega o 401, chama
   `POST /auth/refresh` com o cookie, recebe um novo access token e refaz a
   chamada original — o usuário não percebe.
4. **Rotação**: cada refresh revoga o token anterior e emite um novo
   (`familyId` + `replaced_by_token` na tabela `Refresh_Token`) — um token
   roubado e reusado é detectável.
5. **Reload da página**: o access token em memória se perde, mas o cookie
   sobrevive — o `AuthProvider` troca ele por um token novo e busca
   `GET /auth/me` ao subir. Usuário continua logado; se já autenticado e
   acessar `/login`, é redirecionado para `/` (a página de login é só para
   quem não está autenticado).
6. `POST /auth/logout` revoga o refresh token e limpa o cookie.
7. Conta desativada (`is_active=false`) não autentica — retorna 403
   `Account is not active`.

---

## Testes

### Unitários / integração

```bash
cd server && ./gradlew test      # JUnit + Testcontainers (MySQL real)
cd client && bunx tsc -b         # typecheck
cd client && bun run lint        # eslint + prettier
```

Qualidade de código no server:

```bash
cd server && ./gradlew spotlessCheck checkstyleMain build
```

### E2E — Playwright

A suite fica em `client/e2e/` com estrutura **feature-based** (`specs/<feature>/`, `pages/`, `fixtures/`, `helpers/`) — cada squad adiciona testes na sua feature.

**O stack sobe sozinho**: todo `test:e2e*` roda primeiro `bun run e2e:stack`, que garante MySQL (:3307), API (:8080) e Vite (:5173) — reutilizando o que já estiver no ar (inclusive o stack do docker compose). As credenciais `E2E_ADMIN_EMAIL` / `E2E_ADMIN_PASSWORD` são lidas do `.env` da raiz automaticamente e devem bater com o coordenador seedado.

```bash
cd client
bunx playwright install          # primeira vez (desnecessário se E2E_CHANNEL=chrome)

bun run test:e2e                 # stack + todos os specs (só chromium)
bunx playwright test             # stack manual + TODOS os browsers (lento)
bun run test:e2e:ui              # stack + modo interativo
bun run e2e:stack                # só garante que o stack está no ar
bun run e2e:stack:down           # para o que o e2e:stack subiu (libera :8080)
```

O `e2e:stack` deixa API/Vite rodando em background (dev stack). Se depois você quiser rodar `./gradlew bootRun` manualmente, rode `e2e:stack:down` primeiro — ou `kill $(lsof -ti:8080)` para liberar só a porta.

#### Gerando vídeos para PRs

Vídeos são **só locais** — nada é gravado em CI nem no deploy. Grave localmente e anexe os relevantes ao PR em vez de commitá-los:

```bash
bun run test:e2e:video           # grava vídeo de cada teste (chromium)

# vídeos mais assistíveis: adiciona pausa entre ações
E2E_SLOWMO=600 bun run test:e2e:video
```

Os vídeos saem organizados por feature em `demo-video/<feature>/<teste>.webm` na raiz do projeto (gitignored). Traces e screenshots de falhas ficam em `client/e2e/test-results/`. Mais detalhes: [`client/e2e/README.md`](client/e2e/README.md).

---

## CI/CD (GitHub Actions)

Dois workflows separados em `./.github/workflows/`:

* **`testing.yml`** (PRs e pushes na `main`): `backend` roda `./gradlew build` (Spotless + Checkstyle + JUnit/Testcontainers); `e2e` sobe o `docker-compose` inteiro com **credenciais descartáveis geradas no job** (nenhum secret necessário), espera o `/actuator/health` e roda Playwright headless contra o front do compose (`:3000`), sem vídeos.
* **`deploy.yml`** (push na `main` / manual): SSH na VM Oracle → `git pull` → `docker compose up --build -d`.
