# 🌐 BFF Agendador de Tarefas (Backend For Frontend)

Uma API RESTful desenvolvida com **Java 17**, **Spring Boot 3.2.6** e **Spring Cloud OpenFeign**, atuando como a camada central de **BFF (Backend For Frontend)** e orquestradora do ecossistema de microsserviços. 

O BFF unifica e simplifica os pontos de acesso para aplicações cliente (Front-end Web e Mobile), consumindo e coordenando de forma transparente os microsserviços de **[Usuários](https://github.com/mauricioRodriguesDev/usuario)**, **[Agendador de Tarefas](https://github.com/mauricioRodriguesDev/agendador-tarefas)** e **[Notificação](https://github.com/mauricioRodriguesDev/notificacao)**. Além disso, executa rotinas agendadas automáticas (**Cron Jobs**) para monitorar tarefas pendentes e disparar alertas por e-mail.

---

## 🚀 Tecnologias Utilizadas

| Categoria | Tecnologia |
| :--- | :--- |
| **Linguagem** | Java 17 |
| **Framework** | Spring Boot 3.2.6 |
| **Comunicação Inter-Serviços** | Spring Cloud OpenFeign 2023.0.2 + Apache HttpClient 5 (`feign-hc5 13.3`) |
| **Agendamento de Tarefas** | Spring Task Scheduling (`@Scheduled` com expressões Cron) |
| **Documentação da API** | Springdoc OpenAPI 2.5.0 (Swagger UI v3) |
| **Segurança & Schemas** | OpenAPI Security Scheme (Bearer JWT) |
| **Produtividade** | Lombok |
| **Build Tool** | Apache Maven (com Maven Wrapper `mvnw`) |
| **Containerização** | Docker & Docker Compose |
| **CI/CD** | GitHub Actions (Workflow de build com Maven) |

---

## 📌 Funcionalidades

- **Gateway / Ponto Único de Entrada:** Interface unificada para o frontend, ocultando a complexidade e topologia da rede de microsserviços internos.
- **Orquestração de Usuários:**
  - Cadastro de novos usuários e autenticação (Login com geração de token JWT).
  - Consulta de perfil e exclusão de contas.
  - Atualização cadastral de dados, endereços e telefones.
  - Consulta automática de endereços integrando com a API pública do **ViaCEP**.
- **Orquestração de Tarefas:**
  - Criação de novas tarefas vinculadas ao usuário autenticado.
  - Listagem de todas as tarefas pertencentes ao usuário logado.
  - Consulta de tarefas agendadas por intervalo de datas/período.
  - Atualização completa de tarefas existentes.
  - Alteração pontual de status (`PATCH`) entre `PENDENTE`, `NOTIFICADO` e `CANCELADO`.
  - Exclusão de tarefas por ID.
- **Rotina Automatizada de Notificações (Cron Job):**
  - Monitoramento contínuo em segundo plano executado periodicamente (`CronService`).
  - Autenticação automática com credenciais de serviço para obter token de autorização.
  - Varredura de tarefas programadas para a próxima hora.
  - Disparo de e-mails formatados via microsserviço de Notificação.
  - Atualização automática do status da tarefa para `NOTIFICADO`.
- **Tratamento Centralizado de Erros:** `FeignError` (decodificador de respostas HTTP) e `GlobalExeceptionHandler` com `@ControllerAdvice`.
- **Habilitação de CORS:** Configuração flexível para requisições cross-origin vindas de SPAs (Angular, React, Vue).

---

## 🏛️ Arquitetura do Ecossistema

```text
                                  ┌─────────────────────────────┐
                                  │      Cliente Frontend       │
                                  │   (Web / Mobile / Swagger)  │
                                  └──────────────┬──────────────┘
                                                 │ HTTP / REST
                                                 ▼
                                  ┌─────────────────────────────┐
                                  │    BFF-Agendador-Tarefas    │
                                  │        (porta 8083)         │
                                  │                             │
                                  │    • Roteamento / Gateway   │
                                  │    • CronService (Scheduler)│
                                  └──────┬──────────┬───────────┘
                                         │          │
                 ┌───────────────────────┘          └───────────────────────┐
                 │ Feign Client                             Feign Client    │
                 ▼                                                          ▼
   ┌───────────────────────────┐                              ┌───────────────────────────┐
   │        Usuário API        │                              │     Agendador-Tarefas     │
   │       (porta 8080)        │                              │       (porta 8081)        │
   │                           │                              │                           │
   │  • Auth JWT / BCrypt      │                              │  • Gestão de Tarefas      │
   │  • Endereços e Telefones  │                              │  • Filtro por Período     │
   │  • Integração ViaCEP      │                              │  • Status de Notificação  │
   └─────────────┬─────────────┘                              └─────────────┬─────────────┘
                 │ JPA                                                      │ Spring Data
                 ▼                                                          ▼
        ┌─────────────────┐                                        ┌─────────────────┐
        │   PostgreSQL    │                                        │     MongoDB     │
        │  (porta 5432)   │                                        │  (porta 27017)  │
        └─────────────────┘                                        └─────────────────┘
                                         │
                                         │ CronService (disparo periódico)
                                         ▼
                          ┌─────────────────────────────┐
                          │       Notificação API       │
                          │        (porta 8082)         │
                          │                             │
                          │   • Thymeleaf Template      │
                          │   • JavaMailSender (SMTP)   │
                          └──────────────┬──────────────┘
                                         │
                                         ▼
                                  ┌──────────────┐
                                  │  📧 E-mail   │
                                  │  do Usuário  │
                                  └──────────────┘
```

### Mapeamento dos Microsserviços

| Microsserviço | Porta | Banco / Serviço | Repositório | Papel no Ecossistema |
| :--- | :---: | :--- | :--- | :--- |
| **BFF Agendador Tarefas** | `8083` | — | *(Este projeto)* | Ponto único de entrada, orquestrador e cron job |
| **Usuário API** | `8080` | PostgreSQL | [usuario](https://github.com/mauricioRodriguesDev/usuario) | Autenticação, dados do usuário, endereços e telefones |
| **Agendador de Tarefas** | `8081` | MongoDB | [agendador-tarefas](https://github.com/mauricioRodriguesDev/agendador-tarefas) | Armazenamento e manipulação de tarefas |
| **Notificação** | `8082` | SMTP Gmail | [notificacao](https://github.com/mauricioRodriguesDev/notificacao) | Renderização e envio de e-mails |

---

## 📁 Estrutura de Camadas

```text
com.javanauta.bffagendadortarefas
├── business/                                # Camada de serviços e regras de negócio
│   ├── CronService.java                     # Tarefa agendada para busca e notificação de eventos
│   ├── EmailService.java                    # Serviço de integração com o serviço de Notificação
│   ├── TarefasService.java                  # Orquestração das operações de tarefas
│   ├── UsuarioService.java                  # Orquestração das operações de usuários
│   ├── dto/
│   │   ├── in/                              # DTOs de entrada (Request)
│   │   │   ├── EnderecoDTORequest.java
│   │   │   ├── LoginDTORequest.java
│   │   │   ├── TarefasDTORequest.java
│   │   │   ├── TelefoneDTORequest.java
│   │   │   └── UsuarioDTORequest.java
│   │   └── out/                             # DTOs de saída (Response)
│   │       ├── EnderecoDTOResponse.java
│   │       ├── TarefasDTOResponse.java
│   │       ├── TelefoneDTOResponse.java
│   │       ├── UsuarioDTOResponse.java
│   │       └── ViaCepDTOResponse.java
│   └── enums/
│       └── StatusNotificacaoEnum.java       # PENDENTE, NOTIFICADO, CANCELADO
├── controller/                              # Camada REST Controller
│   ├── TarefasController.java               # Endpoints de tarefas
│   ├── UsuarioController.java               # Endpoints de usuários
│   └── docs/                                # Interfaces com anotações OpenAPI (Swagger)
│       ├── ITarefasApi.java
│       └── IUsuarioApi.java
└── infrastructure/                          # Infraestrutura, integrações e configs
    ├── client/                              # Feign Clients para chamadas HTTP
    │   ├── EmailClient.java                 # Comunicação com serviço de Notificação
    │   ├── TarefasClient.java               # Comunicação com serviço de Tarefas
    │   ├── UsuarioClient.java               # Comunicação com serviço de Usuários
    │   └── config/
    │       ├── FeignConfig.java             # Registro de beans do Feign
    │       └── FeignError.java              # Decodificador de erros HTTP do Feign
    ├── configs/
    │   └── CorsConfig.java                  # Configuração de CORS para navegadores
    ├── exceptions/                          # Exceções de negócio e Handler global
    │   ├── BusinessException.java
    │   ├── ConflictException.java
    │   ├── IllegalArgumentException.java
    │   ├── ResourceNotFoundException.java
    │   ├── UnauthorizedException.java
    │   └── GlobalExeceptionHandler.java     # @ControllerAdvice unificado
    └── security/
        └── SecurityConfig.java              # Configuração do esquema Bearer JWT no Swagger
```

---

## 📋 Pré-requisitos

Antes de iniciar o BFF localmente, garanta que você possui:

- [JDK 17+](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html)
- [Apache Maven](https://maven.apache.org/) (ou utilize o `./mvnw` incluso)
- [Git](https://git-scm.com/)
- [Docker e Docker Compose](https://www.docker.com/) *(opcional, caso execute todo o ecossistema em containers)*
- **Microsserviços dependentes rodando:**
  - **Usuário API** na porta `8080`
  - **Agendador de Tarefas** na porta `8081`
  - **Notificação API** na porta `8082`

---

## ⚙️ Configurações e Variáveis de Ambiente

O arquivo de configuração principal está localizado em `src/main/resources/application.properties`:

```properties
spring.application.name=bff-agendador-tarefas
server.port=8083

# URLs dos Microsserviços downstream (Feign Clients)
usuario.url=${USUARIO_URL:localhost:8080/usuario}
agendador-tarefas.url=${AGENDADOR_TAREFAS_URL:localhost:8081/tarefas}
notificacao.url=${NOTIFICACAO_URL:localhost:8082/email}

# Expressão Cron para o agendador de notificações (padrão: a cada 5 minutos)
cron.horario=${CRON_HORARIO:0 0/5 * * * ?}

# Credenciais de serviço para autenticação automática no CronService
usuario.email=${USUARIO_EMAIL:admin@admin.com}
usuario.senha=${USUARIO_SENHA:1234}
```

### Variáveis de Ambiente Recomendadas

- **Linux / macOS:**
  ```bash
  export USUARIO_URL=localhost:8080/usuario
  export AGENDADOR_TAREFAS_URL=localhost:8081/tarefas
  export NOTIFICACAO_URL=localhost:8082/email
  export USUARIO_EMAIL=admin@admin.com
  export USUARIO_SENHA=1234
  ```

- **Windows (PowerShell):**
  ```powershell
  $env:USUARIO_URL="localhost:8080/usuario"
  $env:AGENDADOR_TAREFAS_URL="localhost:8081/tarefas"
  $env:NOTIFICACAO_URL="localhost:8082/email"
  $env:USUARIO_EMAIL="admin@admin.com"
  $env:USUARIO_SENHA="1234"
  ```

---

## 🛠️ Como Executar

### Opção 1: Execução Local com Maven

1. **Clone o repositório:**
   ```bash
   git clone https://github.com/mauricioRodriguesDev/bff-agendador-tarefas.git
   cd bff-agendador-tarefas
   ```

2. **Inicie a aplicação:**
   - **Linux / macOS:**
     ```bash
     ./mvnw clean spring-boot:run
     ```
   - **Windows:**
     ```powershell
     .\mvnw.cmd clean spring-boot:run
     ```

O BFF estará acessível em: **`http://localhost:8083`**

---

### Opção 2: Execução com Docker Compose (Ecossistema Completo)

O projeto inclui um `docker-compose.yml` capaz de subir todos os microsserviços e bancos de dados simultaneamente:

```bash
docker-compose up --build
```

Serviços iniciados pelo compose:
* `postgres` (porta `5433:5432`)
* `mongo` (porta `27017:27017`)
* `usuario` (porta `8080`)
* `agendador-tarefas` (porta `8081`)
* `notificacao` (porta `8082`)
* `bff-agendador-tarefas` (porta `8083`)

---

## 📖 Documentação da API (Swagger UI)

Com a aplicação em execução, acesse a documentação interativa:

👉 **`http://localhost:8083/swagger-ui/index.html`**

### Como se autenticar no Swagger:
1. Execute a rota `POST /usuario/login` com suas credenciais.
2. Copie o token retornado (ele já começa com `Bearer eyJ...`).
3. Ao executar endpoints protegidos de tarefas ou perfil, informe o token copiado no campo de parâmetro **`Authorization`**.

---

## 🛣️ Endpoints Disponíveis no BFF

### 👤 Módulo de Usuários (`/usuario`)

| Método | Endpoint | Parâmetros / Headers | Descrição |
| :--- | :--- | :--- | :--- |
| `POST` | `/usuario` | Body JSON | Cadastra um novo usuário no sistema |
| `POST` | `/usuario/login` | Body JSON | Autentica usuário e retorna Token JWT |
| `GET` | `/usuario` | `?email={email}` + Header `Authorization` | Busca dados do usuário por e-mail |
| `PUT` | `/usuario` | Body JSON + Header `Authorization` | Atualiza dados cadastrais do usuário |
| `DELETE` | `/usuario/{email}` | Path variable `{email}` + Header `Authorization` | Deleta um usuário do sistema |
| `POST` | `/usuario/endereco` | Body JSON + Header `Authorization` | Cadastra um novo endereço para o usuário |
| `PUT` | `/usuario/endereco` | `?id={id}` + Body JSON + Header `Authorization` | Atualiza um endereço existente |
| `POST` | `/usuario/telefone` | Body JSON + Header `Authorization` | Cadastra um novo telefone para o usuário |
| `PUT` | `/usuario/telefone` | `?id={id}` + Body JSON + Header `Authorization` | Atualiza um telefone existente |
| `GET` | `/usuario/endereco/{cep}` | Path variable `{cep}` | Consulta dados de endereço via **ViaCEP** |

---

### 📅 Módulo de Tarefas (`/tarefas`)

| Método | Endpoint | Parâmetros / Headers | Descrição |
| :--- | :--- | :--- | :--- |
| `POST` | `/tarefas` | Body JSON + Header `Authorization` | Cria e agenda uma nova tarefa |
| `GET` | `/tarefas` | Header `Authorization` | Lista todas as tarefas do usuário autenticado |
| `GET` | `/tarefas/eventos` | `?dataInicial=...&dataFinal=...` + Header `Authorization` | Busca tarefas agendadas em um período |
| `PUT` | `/tarefas` | `?id={id}` + Body JSON + Header `Authorization` | Atualiza completamente uma tarefa |
| `PATCH` | `/tarefas` | `?status={status}&id={id}` + Header `Authorization` | Altera status (`PENDENTE`, `NOTIFICADO`, `CANCELADO`) |
| `DELETE` | `/tarefas` | `?id={id}` + Header `Authorization` | Deleta uma tarefa pelo seu ID |

#### Exemplo de Payload — Criar Tarefa (`POST /tarefas`)

```json
{
  "nomeTarefa": "Reunião de Alinhamento",
  "descricao": "Alinhamento semanal de entregas do projeto",
  "dataEvento": "10-11-2026 10:55:00"
}
```

---

## ⏰ Rotina Agendada (CronService)

O BFF possui uma classe gerenciada pelo Spring (`CronService`) anotada com `@Scheduled(cron = "${cron.horario}")` que executa a cada 5 minutos:

1. **Autenticação:** Realiza login automático no microsserviço de usuários utilizando as credenciais administrativas configuradas (`usuario.email` e `usuario.senha`).
2. **Busca por Tarefas:** Faz uma requisição ao serviço de tarefas buscando tarefas com status `PENDENTE` agendadas entre **+1 hora** e **+1 hora e 5 minutos** a partir do horário atual.
3. **Disparo de E-mails:** Para cada tarefa encontrada, envia uma requisição `POST /email` para o microsserviço de Notificação, disparando o e-mail estilizado ao usuário responsável.
4. **Atualização de Status:** Executa um `PATCH /tarefas` alterando o status da tarefa para `NOTIFICADO`, evitando reenvios duplicados.

---

## 🧪 Testes

Para executar os testes unitários e de integração:

```bash
# Linux / macOS
./mvnw test

# Windows
.\mvnw.cmd test
```

---

## 🔗 Projetos Relacionados

| Microsserviço | Descrição | Repositório |
| :--- | :--- | :--- |
| **BFF Agendador Tarefas** | Orquestrador, Gateway e Scheduler *(este projeto)* | [mauricioRodriguesDev/bff-agendador-tarefas](https://github.com/mauricioRodriguesDev/bff-agendador-tarefas) |
| **Usuário API** | Gerenciamento de usuários e autenticação JWT | [mauricioRodriguesDev/usuario](https://github.com/mauricioRodriguesDev/usuario) |
| **Agendador de Tarefas** | Agendamento e persistência de tarefas (MongoDB) | [mauricioRodriguesDev/agendador-tarefas](https://github.com/mauricioRodriguesDev/agendador-tarefas) |
| **Notificação API** | Envio de e-mails com Thymeleaf e SMTP Gmail | [mauricioRodriguesDev/notificacao](https://github.com/mauricioRodriguesDev/notificacao) |

---

## 📄 Licença

Este projeto é de uso livre para fins de estudo e desenvolvimento.

---

## 👨‍💻 Autor

Desenvolvido por [mauricioRodriguesDev](https://github.com/mauricioRodriguesDev).
