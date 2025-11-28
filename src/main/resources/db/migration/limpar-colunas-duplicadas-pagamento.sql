-- Script para remover colunas duplicadas da tabela PAGAMENTO
-- Execute este script no H2 Console após fazer backup dos dados

-- Remover colunas duplicadas:
-- - MOMENTO_CRIACAO (duplicado com CREATED_AT)
-- - MOMENTO_ENVIO (duplicado com DATA_ENVIO)
-- - STATUS_PAGAMENTO (duplicado com STATUS)

ALTER TABLE PAGAMENTO DROP COLUMN IF EXISTS MOMENTO_CRIACAO;
ALTER TABLE PAGAMENTO DROP COLUMN IF EXISTS MOMENTO_ENVIO;
ALTER TABLE PAGAMENTO DROP COLUMN IF EXISTS STATUS_PAGAMENTO;

