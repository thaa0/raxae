# Geração Automática de Cobranças de Despesas Recorrentes

## Visão Geral
Este documento descreve a implementação da funcionalidade de geração automática de cobranças para despesas recorrentes no sistema Raxae.

## Arquitetura da Solução

### 1. Configuração da Tabela Despesa ✅
- **Campo**: `diaVencimento` (Integer)
- **Descrição**: Armazena o dia do mês (1-31) em que a despesa recorrente vence
- **Localização**: `Despesa.java`

### 2. Configuração da Tabela Cobranca ✅
- **Campo Adicionado**: `mesReferencia` (String, formato: YYYY-MM)
- **Propósito**: Garantir idempotência - evita geração duplicada de cobranças
- **Localização**: `Cobranca.java`

### 3. CRON Job Diário ✅
- **Classe**: `GeracaoCobrancaScheduledJob.java`
- **Horário**: 05:00 da manhã (todos os dias)
- **Expressão CRON**: `"0 0 5 * * ?"`
- **Configuração**: `@EnableScheduling` na classe principal `RaxaeApplication.java`

## Fluxo de Processamento

### Passo 1: Calcular Data-Alvo
```java
LocalDate dataAlvo = LocalDate.now().plusDays(3);
int diaAlvo = dataAlvo.getDayOfMonth();
```
- Adiciona 3 dias à data atual
- Extrai apenas o dia do mês resultante

### Passo 2: Buscar Despesas Relevantes
```sql
SELECT * FROM Despesa 
WHERE dia_vencimento = [diaAlvo] 
AND status = 'ATIVA'
```
- Busca todas as despesas ativas que vencem no dia-alvo

### Passo 3: Garantir Idempotência (Crítico!)
```java
boolean jaExiste = cobrancaRepository.existeCobrancaParaDespesaEMes(
    despesaId, 
    mesReferencia // ex: "2025-11"
);
```
- Verifica se cobranças já foram geradas para aquela despesa no mês
- Se já existir, ignora e passa para próxima despesa
- **Previne cobranças duplicadas** mesmo que o job execute múltiplas vezes

### Passo 4: Gerar Cobranças (Bulk Insert)

#### 4.1 Buscar Membros Ativos
```java
Set<Membro> membrosAtivos = despesa.getGrupo().getMembros()
    .stream()
    .filter(m -> m.getStatus() == StatusParticipacao.ATIVO)
    .collect(Collectors.toSet());
```

#### 4.2 Calcular Valores Individuais

**Divisão Igualitária:**
```java
BigDecimal valorPorMembro = despesa.getValor()
    .divide(BigDecimal.valueOf(membrosAtivos.size()), 2, RoundingMode.HALF_UP);
```

**Divisão Personalizada (Por Valor):**
```java
// Utiliza os valores definidos em DespesaDivisaoPersonalizada
Map<Usuario, BigDecimal> divisaoPersonalizada = despesa.getDivisoesPersonalizadas()
    .stream()
    .collect(Collectors.toMap(
        DespesaDivisaoPersonalizada::getUsuario,
        DespesaDivisaoPersonalizada::getValorPersonalizado
    ));
```

#### 4.3 Salvar em Lote (Bulk Insert)
```java
cobrancaRepository.salvarVarias(cobrancas); // saveAll() do JPA
```

## Componentes Implementados

### Entidades Atualizadas
1. **Cobranca.java**
   - ✅ Adicionado campo `mesReferencia`
   - ✅ Campo preenchido automaticamente no construtor

### Repositórios
1. **DespesaRepository.java**
   - ✅ `buscarPorDiaVencimentoEStatus(Integer diaVencimento, StatusDespesa status)`

2. **CobrancaRepository.java**
   - ✅ `existeCobrancaParaDespesaEMes(UUID despesaId, String mesReferencia)`

### Serviços
1. **GeracaoAutomaticaCobrancaService.java**
   - ✅ Lógica completa de geração de cobranças
   - ✅ Tratamento de divisão igualitária e personalizada
   - ✅ Logs detalhados para auditoria
   - ✅ Tratamento de erros por despesa (não para todo o processo)

2. **GeracaoCobrancaScheduledJob.java**
   - ✅ CRON Job configurado
   - ✅ Execução diária às 05:00
   - ✅ Logs de início e fim de execução

## Exemplo de Execução

### Cenário
- **Data Atual**: 10 de Novembro de 2025
- **Data-Alvo**: 13 de Novembro de 2025 (10 + 3 dias)
- **Dia-Alvo**: 13
- **Mês Referência**: "2025-11"

### Processo
1. Job executa às 05:00
2. Calcula dia-alvo: 13
3. Busca despesas com `diaVencimento = 13` e `status = ATIVA`
4. Para cada despesa:
   - Verifica se já existe cobrança para "2025-11"
   - Se não existir, gera cobranças para todos os membros ativos
   - Salva todas as cobranças em uma única operação (bulk insert)

### Logs Esperados
```
========================================
CRON Job iniciado: Geração Automática de Cobranças
========================================
=== Iniciando processo de geração automática de cobranças ===
Dia alvo: 13 - Mês de referência: 2025-11
Buscando despesas com dia de vencimento: 13
Encontradas 5 despesas para processar
Gerando cobranças para despesa: Aluguel - Vencimento: 2025-11-13
Geradas 4 cobranças para a despesa Aluguel
...
=== Processo finalizado ===
Despesas processadas: 5
Despesas ignoradas (já tinham cobranças): 0
=====================================
CRON Job finalizado com sucesso
========================================
```

## Segurança e Confiabilidade

### Idempotência
- ✅ Campo `mesReferencia` garante que cada despesa gere cobranças apenas uma vez por mês
- ✅ Job pode executar múltiplas vezes sem criar duplicatas

### Tratamento de Erros
- ✅ Erros em uma despesa não param o processamento das demais
- ✅ Logs detalhados para debugging
- ✅ Transações por despesa (isolamento de falhas)

### Performance
- ✅ Bulk Insert (saveAll) reduz operações no banco
- ✅ Execução em horário de baixo tráfego (05:00)
- ✅ Queries otimizadas com índices em `diaVencimento`

## Configuração e Manutenção

### Alterar Horário do CRON
Editar `GeracaoCobrancaScheduledJob.java`:
```java
@Scheduled(cron = "0 0 5 * * ?") // 05:00
// Para 03:00: @Scheduled(cron = "0 0 3 * * ?")
// Para 23:00: @Scheduled(cron = "0 0 23 * * ?")
```

### Desabilitar Temporariamente
Comentar `@EnableScheduling` em `RaxaeApplication.java`

### Executar Manualmente
Injetar `GeracaoAutomaticaCobrancaService` em qualquer controller e chamar:
```java
geracaoAutomaticaCobrancaService.executarGeracaoAutomatica();
```

## Requisitos Técnicos

### Dependências
- ✅ Spring Boot Starter (já incluído)
- ✅ Spring Scheduling (habilitado com `@EnableScheduling`)
- ✅ JPA/Hibernate
- ✅ Lombok

### Configurações
- ✅ `@EnableScheduling` na classe principal
- ✅ Banco de dados H2 (configurado em `application.yml`)

## Testes

### Teste Manual
1. Criar uma despesa recorrente com `diaVencimento` = (hoje + 3 dias)
2. Aguardar execução do CRON às 05:00 OU executar manualmente
3. Verificar se cobranças foram criadas na tabela `Cobranca`
4. Executar novamente - cobranças NÃO devem ser duplicadas

### Teste de Idempotência
```sql
-- Verificar cobranças geradas
SELECT * FROM Cobranca 
WHERE despesa_id = '[UUID_DESPESA]' 
AND mes_referencia = '2025-11';

-- Deve retornar sempre o mesmo número de registros, 
-- mesmo após múltiplas execuções do job
```

## Monitoramento

### Logs Importantes
- Número de despesas encontradas
- Número de despesas processadas
- Número de despesas ignoradas (idempotência)
- Erros durante processamento

### Métricas Sugeridas
- Tempo de execução do job
- Número de cobranças geradas por dia
- Taxa de falhas por despesa

