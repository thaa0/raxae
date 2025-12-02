# Documentação da API - Raxae

## Base URL
```
/raxae/api
```

## Autenticação
A maioria dos endpoints requer autenticação via JWT. O token deve ser enviado no header:
```
Authorization: Bearer {token}
```

O token é obtido através do endpoint de login (`POST /raxae/api/v1/auth/login`).

---

## 1. Autenticação e Usuário

### 1.1. Cadastrar Novo Usuário
- **Método:** `POST`
- **URL:** `/raxae/api/v1/auth/cadastro`
- **Autenticação:** Não requerida
- **Body:**
```json
{
  "nomeCompleto": "string",
  "whatsapp": "string",
  "email": "string",
  "senha": "string"
}
```
- **Status Code:** `201 CREATED`
- **Response:** Sem corpo (void)

---

### 1.2. Login
- **Método:** `POST`
- **URL:** `/raxae/api/v1/auth/login`
- **Autenticação:** Não requerida
- **Body:**
```json
{
  "email": "string",
  "senha": "string"
}
```
- **Status Code:** `200 OK`
- **Response:**
```json
{
  "tipo": "Bearer",
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "usuarioId": "550e8400-e29b-41d4-a716-446655440000"
}
```

---

### 1.3. Obter Informações do Usuário
- **Método:** `GET`
- **URL:** `/raxae/api/v1/auth/info`
- **Autenticação:** Requerida
- **Query Parameters:**
  - `mes` (opcional): String no formato `YYYY-MM` (ex: `2024-01`)
- **Status Code:** `200 OK`
- **Response:**
```json
{
  "numeroDeGrupo": 3,
  "economiaTotal": 1500.50,
  "totalPagoNoMes": 850.25
}
```

---

## 2. Grupos

### 2.1. Criar Grupo
- **Método:** `POST`
- **URL:** `/raxae/api/v1/grupo`
- **Autenticação:** Requerida
- **Body:**
```json
{
  "nomeGrupo": "string",
  "descricao": "string",
  "icone": "string"
}
```
- **Status Code:** `201 CREATED`
- **Response:**
```json
{
  "id": "550e8400-e29b-41d4-a716-446655440000",
  "nomeGrupo": "string",
  "descricao": "string",
  "icone": "string",
  "adminId": "550e8400-e29b-41d4-a716-446655440000",
  "dataCriacao": "2024-01-15T10:30:00",
  "membros": [
    {
      "idMembro": "550e8400-e29b-41d4-a716-446655440000",
      "idUsuario": "550e8400-e29b-41d4-a716-446655440000",
      "nomeUsuario": "string",
      "status": "ATIVO"
    }
  ]
}
```

---

### 2.2. Deletar Grupo
- **Método:** `DELETE`
- **URL:** `/raxae/api/v1/grupo/{idDoGrupo}`
- **Autenticação:** Requerida
- **Path Parameters:**
  - `idDoGrupo`: UUID
- **Status Code:** `204 NO CONTENT`
- **Response:** Sem corpo (void)

---

### 2.3. Obter Grupo por ID
- **Método:** `GET`
- **URL:** `/raxae/api/v1/grupo/{idDoGrupo}`
- **Autenticação:** Requerida
- **Path Parameters:**
  - `idDoGrupo`: UUID
- **Status Code:** `200 OK`
- **Response:** `GrupoResponse` (mesma estrutura do endpoint de criar grupo)

---

### 2.4. Editar Grupo
- **Método:** `PATCH`
- **URL:** `/raxae/api/v1/grupo/{idDoGrupo}`
- **Autenticação:** Requerida
- **Path Parameters:**
  - `idDoGrupo`: UUID
- **Body:** `GrupoEditaRequest` (mesma estrutura de `GrupoNovoRequest`)
- **Status Code:** `200 OK`
- **Response:** `GrupoResponse` (mesma estrutura do endpoint de criar grupo)

---

### 2.5. Remover Membro do Grupo
- **Método:** `PATCH`
- **URL:** `/raxae/api/v1/grupo/{idDoGrupo}/membro/{idDoMembro}/remover`
- **Autenticação:** Requerida
- **Path Parameters:**
  - `idDoGrupo`: UUID
  - `idDoMembro`: UUID
- **Status Code:** `204 NO CONTENT`
- **Response:** Sem corpo (void)

---

### 2.6. Gerar Convite para Grupo
- **Método:** `POST`
- **URL:** `/raxae/api/v1/grupo/{idDoGrupo}/convite`
- **Autenticação:** Requerida
- **Path Parameters:**
  - `idDoGrupo`: UUID
- **Status Code:** `200 OK`
- **Response:** String (código do convite)

---

### 2.7. Entrar em Grupo (via Convite)
- **Método:** `GET`
- **URL:** `/raxae/api/v1/grupo/{idDoGrupo}/join`
- **Autenticação:** Requerida
- **Path Parameters:**
  - `idDoGrupo`: UUID
- **Status Code:** `200 OK`
- **Response:** Sem corpo (void)

---

### 2.8. Listar Grupos do Usuário
- **Método:** `GET`
- **URL:** `/raxae/api/v1/grupo/meus-grupos`
- **Autenticação:** Requerida
- **Status Code:** `200 OK`
- **Response:** `List<GrupoResponse>`

---

### 2.9. Listar Membros do Grupo
- **Método:** `GET`
- **URL:** `/raxae/api/v1/grupo/{idDoGrupo}/membros`
- **Autenticação:** Requerida
- **Path Parameters:**
  - `idDoGrupo`: UUID
- **Status Code:** `200 OK`
- **Response:**
```json
[
  {
    "nomeCompleto": "string",
    "status": "ATIVO"
  }
]
```

---

### 2.10. Buscar Histórico de Membro
- **Método:** `GET`
- **URL:** `/raxae/api/v1/grupo/{idDoGrupo}/historico/{idDoMembro}`
- **Autenticação:** Requerida
- **Path Parameters:**
  - `idDoGrupo`: UUID
  - `idDoMembro`: UUID
- **Status Code:** `200 OK`
- **Response:**
```json
{
  "grupoId": "550e8400-e29b-41d4-a716-446655440000",
  "membroId": "550e8400-e29b-41d4-a716-446655440000",
  "totalDespesasRealizadas": 1500.50,
  "totalCobrancasRecebidas": 1200.00,
  "saldo": -300.50,
  "historico": [
    {
      "id": "550e8400-e29b-41d4-a716-446655440000",
      "tipo": "string",
      "descricao": "string",
      "valor": 150.50,
      "data": "2024-01-15T10:30:00",
      "status": "string"
    }
  ]
}
```
- **Observação:** Requer que o usuário seja admin do grupo

---

## 3. Despesas

### 3.1. Registrar Despesa
- **Método:** `POST`
- **URL:** `/raxae/api/v1/grupos/{grupoId}/despesas`
- **Autenticação:** Requerida
- **Path Parameters:**
  - `grupoId`: UUID
- **Body:**
```json
{
  "nome": "string",
  "valor": 150.50,
  "tipoRecorrencia": "UNICA | MENSAL",
  "tipoDivisao": "IGUALITARIA | POR_VALOR",
  "diaVencimento": 15,
  "divisoesEspecificas": {
    "550e8400-e29b-41d4-a716-446655440000": 50.00
  },
  "pixBeneficiado": "string (opcional)",
  "dataVencimentoAvulsa": "2024-01-15 (opcional, formato: YYYY-MM-DD)"
}
```
- **Valores dos Enums:**
  - `tipoRecorrencia`: `UNICA` ou `MENSAL`
  - `tipoDivisao`: `IGUALITARIA` ou `POR_VALOR`
- **Status Code:** `201 CREATED`
- **Response:**
```json
{
  "id": "550e8400-e29b-41d4-a716-446655440000",
  "nome": "string",
  "valor": 150.50,
  "tipoRecorrencia": "MENSAL",
  "tipoDivisao": "IGUAL",
  "momentoCriacao": "2024-01-15T10:30:00",
  "grupoId": "550e8400-e29b-41d4-a716-446655440000",
  "adminId": "550e8400-e29b-41d4-a716-446655440000"
}
```

---

### 3.2. Excluir Despesa
- **Método:** `DELETE`
- **URL:** `/raxae/api/v1/grupos/{grupoId}/despesas/{despesaId}`
- **Autenticação:** Requerida
- **Path Parameters:**
  - `grupoId`: UUID
  - `despesaId`: UUID
- **Status Code:** `204 NO CONTENT`
- **Response:** Sem corpo (void)

---

## 4. Pagamentos

### 4.1. Registrar Pagamento com Comprovante
- **Método:** `POST`
- **URL:** `/raxae/api/v1/expenses/{expenseId}/pay`
- **Autenticação:** Requerida
- **Content-Type:** `multipart/form-data`
- **Path Parameters:**
  - `expenseId`: UUID
- **Form Data:**
  - `comprovante`: File (multipart/form-data, máximo 10MB)
- **Status Code:** `201 CREATED`
- **Response:**
```json
{
  "pagamentoId": "550e8400-e29b-41d4-a716-446655440000",
  "cobrancaId": "550e8400-e29b-41d4-a716-446655440000",
  "status": "ENVIADO | CONFIRMADO"
}
```

---

### 4.2. Buscar Comprovante de Pagamento
- **Método:** `GET`
- **URL:** `/raxae/api/v1/expenses/{cobrancaId}/comprovante`
- **Autenticação:** Requerida
- **Path Parameters:**
  - `cobrancaId`: UUID
- **Status Code:** `200 OK`
- **Response:** Arquivo de imagem (JPEG)
- **Content-Type:** `image/jpeg`
- **Headers de Resposta:**
  - `Content-Disposition: attachment; filename="comprovante.jpg"`

---

## 5. Planos

### 5.1. Listar Planos Disponíveis
- **Método:** `GET`
- **URL:** `/raxae/api/v1/planos`
- **Autenticação:** Não requerida (verificar se realmente não requer)
- **Status Code:** `200 OK`
- **Response:**
```json
[
  {
    "id": "550e8400-e29b-41d4-a716-446655440000",
    "tipo": "BASICO",
    "descricao": "string",
    "precoMensal": 29.90,
    "limiteGrupos": 3,
    "limiteMembrosPorGrupo": 10,
    "limiteDespesaPorGrupo": 50
  }
]
```

---

### 5.2. Atualizar Plano do Usuário
- **Método:** `PATCH`
- **URL:** `/raxae/api/v1/planos/atualizar`
- **Autenticação:** Requerida
- **Body:**
```json
{
  "planoId": "550e8400-e29b-41d4-a716-446655440000"
}
```
- **Status Code:** `200 OK`
- **Response:**
```json
{
  "id": "550e8400-e29b-41d4-a716-446655440000",
  "statusAdesao": "ATIVO",
  "momentoAdesao": "2024-01-15T10:30:00",
  "diaExpiracao": 15,
  "usuarioId": "550e8400-e29b-41d4-a716-446655440000",
  "planoId": "550e8400-e29b-41d4-a716-446655440000"
}
```

---

## 6. Cobranças (Endpoints Administrativos)

### 6.1. Gerar Cobranças Automáticas (Manual)
- **Método:** `POST`
- **URL:** `/raxae/api/cobrancas/gerar-automaticas`
- **Autenticação:** Verificar se requerida
- **Status Code:** `200 OK`
- **Response:**
```json
{
  "message": "Processo de geração automática de cobranças executado com sucesso. Verifique os logs para detalhes."
}
```
- **Observação:** Endpoint para execução manual do processo que normalmente roda automaticamente às 05:00 via CRON Job

---

## 7. Lembretes (Endpoints Administrativos)

### 7.1. Enviar Lembretes Automáticos (Manual)
- **Método:** `POST`
- **URL:** `/raxae/api/lembretes/enviar-automaticos`
- **Autenticação:** Verificar se requerida
- **Status Code:** `200 OK`
- **Response:**
```json
{
  "message": "Processo de envio de lembretes executado com sucesso. Verifique os logs para detalhes."
}
```
- **Observação:** Endpoint para execução manual do processo que normalmente roda automaticamente às 09:00 via CRON Job

---

## Códigos de Status HTTP

- `200 OK`: Requisição bem-sucedida
- `201 CREATED`: Recurso criado com sucesso
- `204 NO CONTENT`: Requisição bem-sucedida sem conteúdo de resposta
- `400 BAD REQUEST`: Requisição inválida
- `401 UNAUTHORIZED`: Não autenticado
- `403 FORBIDDEN`: Não autorizado
- `404 NOT FOUND`: Recurso não encontrado
- `500 INTERNAL SERVER ERROR`: Erro interno do servidor

---

## Enums e Valores Possíveis

### TipoRecorrencia
- `UNICA`: Despesa única (não recorrente)
- `MENSAL`: Despesa mensal (recorrente)

### TipoDivisao
- `IGUALITARIA`: Divisão igual entre todos os membros
- `POR_VALOR`: Divisão por valores específicos (requer `divisoesEspecificas`)

### TipoPlano
- `GRATUITO`: Plano gratuito
- `PREMIUM`: Plano premium

### StatusParticipacao
- `ATIVO`: Membro ativo no grupo
- `PENDENTE`: Membro com participação pendente
- `REMOVIDO`: Membro removido do grupo

### StatusAdesao
- `ATIVO`: Adesão ativa
- `PENDENTE`: Adesão pendente
- `CANCELADO`: Adesão cancelada
- `EXPIRADO`: Adesão expirada

### StatusPagamento
- `ENVIADO`: Pagamento enviado (aguardando confirmação)
- `CONFIRMADO`: Pagamento confirmado

---

## Observações Importantes

1. **Context Path:** Todos os endpoints têm o prefixo `/raxae/api`
2. **Autenticação:** A maioria dos endpoints requer o header `Authorization: Bearer {token}`
3. **Upload de Arquivos:** O endpoint de pagamento aceita arquivos até 10MB
4. **Formato de Data:** 
   - Para query parameters de mês: `YYYY-MM` (ex: `2024-01`)
   - Para campos de data no body: formato ISO LocalDate
5. **UUIDs:** Todos os IDs de recursos são UUIDs
6. **Validação:** Os endpoints com `@Valid` validam os dados do body antes de processar

---

## Exemplo de Uso com Fetch (JavaScript)

```javascript
// Login
const loginResponse = await fetch('/raxae/api/v1/auth/login', {
  method: 'POST',
  headers: {
    'Content-Type': 'application/json'
  },
  body: JSON.stringify({
    email: 'usuario@example.com',
    senha: 'senha123'
  })
});

const { token } = await loginResponse.json();

// Requisição autenticada
const gruposResponse = await fetch('/raxae/api/v1/grupo/meus-grupos', {
  method: 'GET',
  headers: {
    'Authorization': `Bearer ${token}`
  }
});

const grupos = await gruposResponse.json();

// Upload de comprovante
const formData = new FormData();
formData.append('comprovante', fileInput.files[0]);

const pagamentoResponse = await fetch(`/raxae/api/v1/expenses/${expenseId}/pay`, {
  method: 'POST',
  headers: {
    'Authorization': `Bearer ${token}`
  },
  body: formData
});
```

