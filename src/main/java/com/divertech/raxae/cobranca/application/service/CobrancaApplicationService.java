package com.divertech.raxae.cobranca.application.service;

import com.divertech.raxae.cobranca.application.controller.CobrancaResponse;
import com.divertech.raxae.cobranca.domain.Cobranca;
import com.divertech.raxae.cobranca.repository.CobrancaRepository;
import com.divertech.raxae.grupo.application.service.GrupoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Log4j2
public class CobrancaApplicationService implements CobrancaService {

    private final CobrancaRepository cobrancaRepository;
    private final GrupoService grupoService;

    @Override
    public List<CobrancaResponse> listarCobrancasPorUsuario(UUID usuarioId) {
        log.info("[start] CobrancaApplicationService - listarCobrancasPorUsuario");
        List<Cobranca> cobrancas = cobrancaRepository.buscarCobrancasPorUsuario(usuarioId);
        log.info("[finish] CobrancaApplicationService - listarCobrancasPorUsuario - {} cobranças encontradas", cobrancas.size());
        return cobrancas.stream()
                .map(CobrancaResponse::from)
                .collect(Collectors.toList());
    }

    @Override
    public List<CobrancaResponse> listarCobrancasPorGrupo(UUID grupoId, UUID usuarioAdminId) {
        log.info("[start] CobrancaApplicationService - listarCobrancasPorGrupo - grupoId: {}, usuarioAdminId: {}", grupoId, usuarioAdminId);
        grupoService.validaUsuarioAdmin(grupoId, usuarioAdminId);
        List<Cobranca> cobrancas = cobrancaRepository.buscarCobrancasPorGrupo(grupoId);
        log.info("[finish] CobrancaApplicationService - listarCobrancasPorGrupo - {} cobranças encontradas", cobrancas.size());
        return cobrancas.stream()
                .map(CobrancaResponse::from)
                .collect(Collectors.toList());
    }

//    private List<Cobranca> calcularDivisao(Despesa despesa, List<Usuario> usuarios, DespesaRequest request) {
//        List<Cobranca> cobrancas = new ArrayList<>();
//        Map<UUID, Usuario> mapaMembros = usuarios.stream()
//                .collect(Collectors.toMap(Usuario::getId, usuario -> usuario));
//        LocalDate dataVencimento = calcularDataVencimento(despesa);
//
//        switch (despesa.getTipoDivisao()) {
//            case IGUALITARIA:
//                BigDecimal valorIndividual = despesa.getValor()
//                        .divide(new BigDecimal(usuarios.size()), 2, RoundingMode.HALF_UP);
//                for (Usuario usuario : usuarios) {
//                    cobrancas.add(criarCobranca(despesa, usuario, valorIndividual, dataVencimento));
//                }
//                break;
//            case POR_VALOR:
//                validarDivisaoExata(despesa.getValor(), request.getDivisoesEspecificas());
//                for (Map.Entry<UUID, BigDecimal> entry : request.getDivisoesEspecificas().entrySet()) {
//                    Usuario devedor = mapaMembros.get(entry.getKey());
//                    if (devedor == null) {
//                        throw APIException.build(HttpStatus.BAD_REQUEST, "Usuário com ID " + entry.getKey() + " não pertence a este grupo.");
//                    }
//                    cobrancas.add(criarCobranca(despesa, devedor, entry.getValue(), dataVencimento));
//                }
//                break;
//          }
//          return cobrancas;
//      }
//
//
//
//    private Cobranca criarCobranca(Despesa despesa, Usuario devedor, BigDecimal valor, LocalDate dataVencimento) {
//        return new Cobranca(despesa, devedor, valor, StatusCobranca.PENDENTE, dataVencimento);
//    }
//
//    private void validarDivisaoExata(BigDecimal valorTotal, Map<UUID, BigDecimal> divisoes) {
//        if (divisoes == null || divisoes.isEmpty()) {
//            throw APIException.build(HttpStatus.BAD_REQUEST, "Para divisão por VALOR_EXATO, o mapa 'divisoesEspecificas' é obrigatório.");
//        }
//        BigDecimal soma = divisoes.values().stream().reduce(BigDecimal.ZERO, BigDecimal::add);
//        if (soma.compareTo(valorTotal) != 0) {
//            throw APIException.build(HttpStatus.BAD_REQUEST,
//                    String.format("A soma dos valores (R$%.2f) não bate com o valor total da despesa (R$%.2f).", soma, valorTotal)
//            );
//        }
//    }
//
//    private void validarDivisaoPorcentagem(Map<UUID, BigDecimal> divisoes) {
//        if (divisoes == null || divisoes.isEmpty()) {
//            throw APIException.build(HttpStatus.BAD_REQUEST, "Para divisão por PORCENTAGEM, o mapa 'divisoesEspecificas' é obrigatório.");
//        }
//        BigDecimal somaPercentual = divisoes.values().stream().reduce(BigDecimal.ZERO, BigDecimal::add);
//        if (somaPercentual.compareTo(new BigDecimal(100)) != 0) {
//            throw APIException.build(HttpStatus.BAD_REQUEST,
//                    String.format("A soma das porcentagens (%.2f%%) não totaliza 100%%.", somaPercentual)
//            );
//        }
//    }
}
