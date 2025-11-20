# 🚀 RAXAE - Plataforma de Gestão de Despesas Compartilhadas

## 📋 Descrição do Projeto

**RAXAE** é uma plataforma que **automatiza e notifica a gestão de despesas compartilhadas**. O sistema permite criar grupos, registrar despesas recorrentes, gerar cobranças automáticas e enviar lembretes via WhatsApp para os membros do grupo.

### Principais Funcionalidades

- ✅ **Gerenciamento de Usuários** - Cadastro, login e autenticação JWT
- 👥 **Grupos** - Criação de grupos para compartilhar despesas
- 💰 **Despesas Recorrentes** - Registro de despesas que se repetem mensalmente
- 📊 **Cobranças Automáticas** - Geração automática de cobranças baseadas nas despesas
- 📱 **Notificações WhatsApp** - Envio de lembretes automáticos de vencimento
- 🔔 **Sistema de Lembretes** - Notificações 2 dias antes, 1 dia antes e no dia do vencimento

---

## 🛠️ Tecnologias Utilizadas

- **Java 17**
- **Spring Boot 3.4.0**
- **Spring Security** (Autenticação JWT)
- **Spring Data JPA**
- **H2 Database** (Banco de dados em arquivo)
- **Lombok**
- **SpringDoc OpenAPI** (Swagger)
- **WebFlux** (Cliente HTTP reativo)
- **Maven**

---

## 📦 Pré-requisitos

Antes de começar, certifique-se de ter instalado:

- **Java 17** ou superior ([Download JDK](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html))
- **Maven 3.6+** (ou use o Maven Wrapper incluído no projeto)
- **Git** (para clonar o repositório)

### Verificar instalação:

```cmd
java -version
mvn -version
```

---

## 🚀 Como Executar o Projeto

### 1️⃣ Clone o Repositório

```cmd
git clone <url-do-repositorio>
cd raxae
```

### 2️⃣ Build do Projeto

#### Usando Maven Wrapper (Recomendado - não precisa ter Maven instalado):

```cmd
mvnw.cmd clean install
```

#### Ou usando Maven instalado:

```cmd
mvn clean install
```

### 3️⃣ Executar a Aplicação

#### Opção 1: Via Maven

```cmd
mvnw.cmd spring-boot:run
```

#### Opção 2: Via JAR gerado

```cmd
java -jar target/raxae-0.0.1-SNAPSHOT.jar
```

### 4️⃣ Verificar se está rodando

A aplicação estará disponível em:
- **URL Base**: `http://localhost:8080/raxae/api`
- **Swagger UI**: `http://localhost:8080/raxae/api/swagger`
- **Console H2**: `http://localhost:8080/raxae/api/h2-console`

---

## 📚 Documentação da API (Swagger)

### Acessar o Swagger

Após iniciar a aplicação, acesse:

```
http://localhost:8080/raxae/api/swagger
```

O Swagger fornece uma interface interativa para testar todos os endpoints da API.

### 🔐 Autenticação no Swagger

A maioria dos endpoints requer autenticação JWT. Siga os passos:

1. **Criar um usuário** via endpoint `/v1/auth/cadastro`
2. **Fazer login** via endpoint `/v1/auth/login` (receberá um token)
3. **Clicar no botão "Authorize"** no topo da página do Swagger
4. **Inserir o token** no formato: `Bearer <seu-token-aqui>`
5. **Clicar em "Authorize"** e depois "Close"

Agora você pode testar os endpoints protegidos!

---

## 🔑 Endpoints Principais

### Autenticação

| Método | Endpoint | Descrição | Auth |
|--------|----------|-----------|------|
| POST | `/v1/auth/cadastro` | Cadastrar novo usuário | ❌ |
| POST | `/v1/auth/login` | Fazer login (retorna JWT) | ❌ |
| GET | `/v1/auth/info` | Obter informações do usuário logado | ✅ |

### Grupos

| Método | Endpoint | Descrição | Auth |
|--------|----------|-----------|------|
| POST | `/v1/grupo` | Criar novo grupo | ✅ |
| GET | `/v1/grupo/{id}` | Buscar grupo por ID | ✅ |
| PATCH | `/v1/grupo/{id}` | Editar grupo | ✅ |
| DELETE | `/v1/grupo/{id}` | Deletar grupo | ✅ |
| POST | `/v1/grupo/{id}/convite` | Gerar convite para o grupo | ✅ |
| PATCH | `/v1/grupo/{id}/membro/{idMembro}/remover` | Remover membro | ✅ |

### Despesas

| Método | Endpoint | Descrição | Auth |
|--------|----------|-----------|------|
| POST | `/v1/grupos/{grupoId}/despesas` | Registrar nova despesa | ✅ |
| DELETE | `/v1/grupos/{grupoId}/despesas/{despesaId}` | Excluir despesa | ✅ |

### Cobranças

| Método | Endpoint | Descrição | Auth |
|--------|----------|-----------|------|
| POST | `/cobrancas/gerar-automaticas` | Gerar cobranças automáticas manualmente | ✅ |

---

## 🗄️ Banco de Dados H2

### Acessar Console H2

```
URL: http://localhost:8080/raxae/api/h2-console
```

**Configurações de conexão:**
- **JDBC URL**: `jdbc:h2:file:./data/raxadb`
- **User Name**: `raxae`
- **Password**: `raxae123`

O banco de dados é persistido no arquivo `./data/raxadb.mv.db`

### 🎲 Dados de Teste Pré-Cadastrados

O projeto já possui dados de teste cadastrados no banco de dados (`/data`), então **você não precisa passar por todo o fluxo de cadastro** se quiser apenas testar a aplicação rapidamente!

#### 👤 Usuários Disponíveis

**Usuário 1 - Thálita Souza**
- **Email**: `thalita@gmail.com`
- **Senha**: `1234`
- **WhatsApp**: `73988805160`

**Usuário 2 - Rick Lee**
- **Email**: `ricklee@gmail.com`
- **Senha**: `1234`
- **WhatsApp**: `73988805180`

#### 👥 Grupos Existentes

**Grupo 1: "ADS"** (Admin: Rick Lee)
- **Descrição**: Cursos compartilhados para hora complementar
- **ID**: `ba595d4b-794b-40fc-87b7-2d612ab59031`
- **Despesas cadastradas**:
  - Rocktseat: R$ 245,00 (mensal)
  - DevNinja: R$ 50,00 (mensal)

**Grupo 2: "Streammings"** (Admin: Rick Lee)
- **Descrição**: Streammings compartilhados
- **ID**: `e5ae6511-8a99-4fb0-a323-2cff24b68c78`
- **Membros**: Rick Lee e Thálita Souza
- **Despesas cadastradas**:
  - Prime: R$ 230,00 (mensal)

#### 🚀 Como usar os dados de teste

1. **Faça login** com um dos usuários acima:
   ```bash
   POST http://localhost:8080/raxae/api/v1/auth/login
   ```
   ```json
   {
     "email": "ricklee@gmail.com",
     "senha": "1234"
   }
   ```

2. **Use o token retornado** para acessar os endpoints protegidos

3. **Acesse os grupos** já cadastrados usando os IDs acima

> 💡 **Dica**: Todos os dados de teste completos estão disponíveis em `src/main/resources/massas-para-teste.txt`

---

## ⚙️ Configurações

As configurações da aplicação estão no arquivo `src/main/resources/application.yml`:

```yaml
server:
  servlet:
    context-path: /raxae/api

spring:
  datasource:
    url: jdbc:h2:file:./data/raxadb
    username: raxae
    password: raxae123
```

### Variáveis de Ambiente

Para integração com WhatsApp (Maytapi), configure as seguintes variáveis:

- `BASE_URL_MAYTAPI` - URL base da API Maytapi
- `API_KEY_MAYTAPI` - Sua chave de API
- `PHONE_ID_MAYTAPI` - ID do telefone
- `PRODUCT_ID_MAYTAPI` - ID do produto

---

## 📅 Processos Automatizados

### Geração Automática de Cobranças
- **Horário**: 05:00 (diariamente)
- **Descrição**: Gera cobranças para despesas recorrentes no primeiro dia de cada mês

### Envio de Lembretes WhatsApp
- **Vence daqui 2 dias**: 09:00
- **Vence amanhã**: 09:00
- **Vence hoje**: 09:00

---

## 📖 Estrutura do Projeto

```
src/main/java/com/divertech/raxae/
├── auth/              # Autenticação e segurança JWT
├── cobranca/          # Gestão de cobranças e despesas
├── config/            # Configurações (Swagger, etc)
├── grupo/             # Gestão de grupos
├── handler/           # Tratamento de exceções
├── notificacao/       # Sistema de notificações WhatsApp
├── plano/             # Gestão de planos
└── usuario/           # Gestão de usuários
```

---

## 🐛 Troubleshooting

### Erro: "Port 8080 already in use"
Verifique se outra aplicação está usando a porta 8080 ou altere a porta no `application.yml`:
```yaml
server:
  port: 8081
```

### Erro ao conectar no banco H2
Certifique-se de que o diretório `./data` existe e tem permissões de escrita.

### Token JWT expirado
Os tokens expiram após 12 horas (43200000 ms). Faça login novamente para obter um novo token.

---

## 👨‍💻 Desenvolvimento

### Compilar sem executar testes

```cmd
mvnw.cmd clean install -DskipTests
```

### Executar em modo debug

```cmd
mvnw.cmd spring-boot:run -Dspring-boot.run.jvmArguments="-Xdebug -Xrunjdwp:transport=dt_socket,server=y,suspend=n,address=5005"
```

---

## 📝 Licença

Este projeto foi desenvolvido para fins acadêmicos.

---

## 📞 Suporte

Para dúvidas ou problemas, consulte a documentação do Swagger ou verifique os logs da aplicação.

**Logs importantes:**
- Geração de cobranças: `[GeracaoAutomaticaCobrancaService]`
- Envio de lembretes: `[GeracaoLembreteService]`
- Autenticação: `[AuthService]`

---

## 🎯 Quick Start

```cmd
# 1. Clone o projeto
git clone <url-do-repositorio>
cd raxae

# 2. Build e execute
mvnw.cmd spring-boot:run

# 3. Acesse o Swagger
# http://localhost:8080/raxae/api/swagger
```
