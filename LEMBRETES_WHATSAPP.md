# Sistema de Lembretes Automáticos via WhatsApp

## Visão Geral
Este documento descreve a implementação completa do sistema de lembretes automáticos de cobranças via WhatsApp usando a API Maytapi e Feign Client.

## Arquitetura da Solução

### 1. Estrutura de Pacotes

```
notificacao/
├── application/
│   ├── controller/
│   │   └── LembreteController.java
│   └── service/
│       ├── GeracaoLembreteService.java
│       ├── LembreteCobrancaScheduledJob.java
│       ├── MensagemLembreteBuilder.java
│       └── WhatsAppService.java
├── client/
│   ├── dto/
│   │   ├── WhatsAppMessageRequest.java
│   │   └── WhatsAppMessageResponse.java
│   ├── MaytapiClientConfig.java
│   └── MaytapiWhatsAppClient.java
└── domain/
    └── TipoLembrete.java
```

### 2. Configuração do CRON Job ✅

**Classe**: `LembreteCobrancaScheduledJob.java`
- **Horário**: 09:00 da manhã (todos os dias)
- **Expressão CRON**: `"0 0 9 * * ?"`
- **Descrição**: Executa automaticamente todos os dias às 09:00

### 3. Integração com API WhatsApp (Maytapi)

#### Feign Client
**Interface**: `MaytapiWhatsAppClient.java`
- Integração declarativa com API Maytapi
- Configuração automática de headers e parâmetros
- Tratamento de erros customizado

#### Endpoint da API
```
POST https://api.maytapi.com/api/{productId}/{phoneId}/sendMessage
Headers:
  - Content-Type: application/json
  - x-maytapi-key: {apiKey}
```

#### Configuração (application.yml)
```yaml
maytapi:
  base-url: https://api.maytapi.com
  product-id: dc01968f-####-####-####-7cfcf51aa423  # Substitua pelo seu ID
  phone-id: 12                                       # Substitua pelo seu phone ID
  api-key: b267697c-####-####-####-2435e812efc1    # Substitua pela sua chave
```

**⚠️ IMPORTANTE**: Substitua os valores `####` pelas suas credenciais reais da Maytapi.

## Fluxo de Processamento

### 1. Consulta ao Banco de Dados

**Query Implementada**:
```java
SELECT c FROM Cobranca c 
WHERE c.status = 'PENDENTE' 
  AND c.mesReferencia = :mesAtual 
  AND c.dataVencimento = :dataAlvo
```

**Dados Retornados**:
- ID da cobrança
- Usuário (com número WhatsApp)
- Despesa (nome)
- Data de vencimento
- Status

### 2. Janelas de Lembrete

#### Janela 1: Vence Hoje
- **Condição**: `dataVencimento == hoje`
- **Mensagem**: 
```
🔔 Ei {nome}, olha a treta chegando! A conta de {despesa} vence HOJE. 
Bora quitar antes que vire novela... 💸
```

#### Janela 2: Atrasado 1º Aviso (1 dia de atraso)
- **Condição**: `dataVencimento == hoje - 1 dia`
- **Mensagem**:
```
⚠️ Rapaz {nome}... a conta de {despesa} venceu ontem e já tá pedindo atenção. 
Não deixa o grupo virar tribunal, paga logo 😅.
```

#### Janela 3: Atrasado Crítico (3 dias de atraso)
- **Condição**: `dataVencimento == hoje - 3 dias`
- **Mensagem**:
```
🚨 Alerta Raxae 🚨

Ei {nome}, a conta de {despesa} tá atrasada há 3 dias! 
Já já o povo começa a te marcar no grupo... 
melhor resolver antes da zoeira virar cobrança pesada 😂.
```

### 3. Envio via WhatsApp

**Serviço**: `WhatsAppService.java`

**Método**: `enviarMensagem(String numeroWhatsApp, String mensagem)`

**Payload Enviado**:
```json
{
  "to_number": "+5511999999999",
  "type": "text",
  "message": "Mensagem formatada aqui...",
  "typing": "typing",
  "duration": 3
}
```

**Formato do Número**:
- Deve iniciar com código do país
- Sem caracteres especiais (+, -, espaços)
- Exemplo: `5511999999999` para Brasil (São Paulo)

### 4. Tratamento de Falhas

✅ **Implementado**:
- Falhas individuais não interrompem o processo completo
- Cada cobrança é processada em um try-catch separado
- Logs detalhados de cada falha
- Contadores de sucesso/falha

## Componentes Principais

### GeracaoLembreteService

Métodos principais:
- `executarGeracaoLembretes()`: Orquestra todo o processo
- `processarLembretesVenceHoje()`: Processa janela 1
- `processarLembretesAtrasadoPrimeiroAviso()`: Processa janela 2
- `processarLembretesAtrasadoCritico()`: Processa janela 3

### WhatsAppService

Métodos:
- `enviarMensagem(String numero, String mensagem)`: Envia texto simples
- `enviarMensagemComMidia(String numero, String urlMidia, String texto)`: Envia com mídia

### MensagemLembreteBuilder

Responsável por construir mensagens personalizadas baseadas no tipo de lembrete.

## Logs e Monitoramento

### Logs de Execução

```
========================================
CRON Job iniciado: Envio de Lembretes via WhatsApp
========================================
=== Iniciando Geração de Lembretes Automáticos ===
Processando lembretes: Vence Hoje (2025-11-18)
Encontradas 5 cobranças que vencem hoje
Enviando mensagem WhatsApp para: 5511999999999
Mensagem enviada com sucesso para: 5511999999999
Lembretes 'Vence Hoje' enviados: 5/5
Processando lembretes: Atrasado 1º Aviso (vencimento: 2025-11-17)
Encontradas 2 cobranças atrasadas há 1 dia
Lembretes 'Atrasado 1º Aviso' enviados: 2/2
Processando lembretes: Atrasado Crítico (vencimento: 2025-11-15)
Encontradas 1 cobranças atrasadas há 3 dias
Lembretes 'Atrasado Crítico' enviados: 1/1
========================================
=== Resumo da Execução ===
Lembretes 'Vence Hoje': 5
Lembretes 'Atrasado 1º Aviso': 2
Lembretes 'Atrasado Crítico': 1
Total de lembretes enviados: 8
========================================
CRON Job de lembretes finalizado com sucesso
========================================
```

### Métricas Importantes

- Número de lembretes enviados por janela
- Taxa de sucesso/falha no envio
- Tempo de execução total
- Quantidade de cobranças processadas

## Teste Manual

### Via Endpoint REST

**URL**: `POST /raxae/api/lembretes/enviar-automaticos`

**Swagger**: http://localhost:8080/raxae/api/swagger

**Exemplo cURL**:
```bash
curl -X POST http://localhost:8080/raxae/api/lembretes/enviar-automaticos
```

### Teste de Integração com Maytapi

1. Configure suas credenciais reais no `application.yml`
2. Certifique-se que sua instância WhatsApp está conectada na Maytapi
3. Crie uma cobrança pendente com vencimento = hoje
4. Execute o endpoint manual
5. Verifique se a mensagem chegou no WhatsApp

## Configuração e Personalização

### Alterar Horário do CRON

Editar `LembreteCobrancaScheduledJob.java`:
```java
@Scheduled(cron = "0 0 9 * * ?")  // 09:00
// Para 10:00: @Scheduled(cron = "0 0 10 * * ?")
// Para 14:30: @Scheduled(cron = "0 30 14 * * ?")
```

### Personalizar Mensagens

Editar `MensagemLembreteBuilder.java` - método `construirMensagem()`

### Adicionar Novas Janelas de Lembrete

1. Adicionar novo valor em `TipoLembrete.java`
2. Criar novo método em `GeracaoLembreteService.java`
3. Adicionar mensagem em `MensagemLembreteBuilder.java`
4. Chamar no método `executarGeracaoLembretes()`

## Checklist de Implementação

### Configuração ✅
- [x] Dependência Feign Client no pom.xml
- [x] @EnableFeignClients na classe principal
- [x] @EnableScheduling na classe principal
- [x] Configurações Maytapi no application.yml

### Cliente WhatsApp ✅
- [x] Interface Feign Client criada
- [x] DTOs de request/response
- [x] Configuração customizada do Feign
- [x] Tratamento de erros

### Serviços ✅
- [x] WhatsAppService para envio de mensagens
- [x] MensagemLembreteBuilder para construir mensagens
- [x] GeracaoLembreteService com 3 janelas de lembrete
- [x] LembreteCobrancaScheduledJob (CRON)

### Repositório ✅
- [x] Query para buscar cobranças por status, mês e data
- [x] Implementação na camada de infraestrutura

### Endpoints ✅
- [x] Controller para execução manual
- [x] Documentação Swagger

### Logs e Monitoramento ✅
- [x] Logs detalhados em cada etapa
- [x] Contadores de sucesso/falha
- [x] Tratamento de exceções

## Requisitos de Produção

### Credenciais Maytapi

1. Criar conta em https://maytapi.com
2. Obter Product ID
3. Registrar telefone e obter Phone ID
4. Gerar API Key
5. Atualizar `application.yml` com valores reais

### Números WhatsApp

- Devem estar salvos no formato internacional
- Exemplo correto: `5511999999999` (Brasil)
- Sem +, -, parênteses ou espaços

### Limitações da API

- Verificar limite de mensagens do seu plano Maytapi
- Implementar retry em caso de rate limiting (futuro)
- Validar conexão da instância WhatsApp antes de enviar

## Troubleshooting

### Mensagens não são enviadas

1. Verificar se credenciais Maytapi estão corretas
2. Confirmar que instância WhatsApp está conectada
3. Verificar formato do número de telefone
4. Checar logs do Feign Client (nível DEBUG)
5. Validar status da API: https://status.maytapi.com

### CRON não executa

1. Verificar se `@EnableScheduling` está presente
2. Checar logs no horário agendado
3. Validar expressão CRON

### Números no formato errado

- Criar método de normalização de números
- Validar no cadastro do usuário

## Próximas Melhorias

- [ ] Retry automático em caso de falha
- [ ] Fila de mensagens (Redis/RabbitMQ)
- [ ] Dashboard de monitoramento
- [ ] Templates de mensagens configuráveis
- [ ] Suporte a múltiplos idiomas
- [ ] Envio de comprovante de pagamento via WhatsApp
- [ ] Notificações para administradores do grupo

