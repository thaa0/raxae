package com.divertech.raxae.usuario.application.service;

import com.divertech.raxae.auth.config.service.AuthService;
import com.divertech.raxae.cobranca.domain.Cobranca;
import com.divertech.raxae.cobranca.repository.CobrancaRepository;
import com.divertech.raxae.plano.application.repository.AdesaoRepository; 
import com.divertech.raxae.plano.application.repository.PlanoRepository; 
import com.divertech.raxae.plano.domain.Adesao; 
import com.divertech.raxae.plano.domain.Plano; 
import com.divertech.raxae.plano.domain.TipoPlano; 
import com.divertech.raxae.usuario.application.controller.InfoUsuarioResponse;
import com.divertech.raxae.usuario.application.repository.UsuarioRepository;
import com.divertech.raxae.usuario.domain.Usuario;
import com.divertech.raxae.usuario.application.controller.UsuarioRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.YearMonth;

@Service
@RequiredArgsConstructor
@Log4j2
public class UsuarioApplicationService implements UsuarioService {
    private final UsuarioRepository usuarioRepository;
    private final BCryptPasswordEncoder encriptador;
    private final AuthService authService;

    private final PlanoRepository planoRepository;
    private final AdesaoRepository adesaoRepository;
    private final CobrancaRepository cobrancaRepository;

    @Override
    @Transactional 
    public void cadastrarUsuario(UsuarioRequest request) {
        log.info("[start] UsuarioApplicationService - cadastrarUsuario");
        Usuario novoUsuario = new Usuario(request, encriptador);
        usuarioRepository.salva(novoUsuario);
        try {
//            Plano planoGratuito = planoRepository.buscaPorTipo(TipoPlano.GRATUITO);
//            Adesao novaAdesao = Adesao.criarAdesaoInicial(novoUsuario, planoGratuito);
//            adesaoRepository.salvar(novaAdesao);
        } catch (Exception e) {
            log.error("[fail] Falha ao criar adesão padrão para o usuário {}", request.getEmail(), e);
            throw new RuntimeException("Falha ao finalizar cadastro. Tente novamente.", e);
        }
        

        log.debug("[finish] UsuarioApplicationService - cadastrarUsuario");
    }

    @Override
    public InfoUsuarioResponse getInfoUsuario(Usuario usuarioLogado, YearMonth mes) {
        log.info("[start] UsuarioApplicationService - getInfoUsuario");
        
        // Se não foi fornecido um mês, usa o mês atual
        YearMonth mesConsulta = mes != null ? mes : YearMonth.now();

        var cobrancasPagasNoMes = cobrancaRepository.buscarCobrancasPagasPorUsuarioEMes(
                usuarioLogado.getId(), mesConsulta);

        double totalPagoNoMes = cobrancasPagasNoMes.stream()
                .map(Cobranca::getValor)
                .map(BigDecimal::doubleValue)
                .reduce(0.0, Double::sum);

        var todasCobrancasPagas = cobrancaRepository.buscarTodasCobrancasPagasPorUsuario(
                usuarioLogado.getId());

        double economiaTotal = todasCobrancasPagas.stream()
                .mapToDouble(cobranca -> {
                    BigDecimal valorTotalDespesa = cobranca.getDespesa().getValor();
                    BigDecimal valorPago = cobranca.getValor();
                    return valorTotalDespesa.subtract(valorPago).doubleValue();
                })
                .sum();
        
        int numeroDeGrupos = usuarioRepository.contaGruposDoUsuario(usuarioLogado.getId());
        log.debug("DEBUG: Número de grupos do usuário: {}", numeroDeGrupos);
        
        InfoUsuarioResponse infoUsuarioResponse = new InfoUsuarioResponse(
                numeroDeGrupos,
                economiaTotal,
                totalPagoNoMes,
                usuarioLogado.getNomeCompleto(),
                usuarioLogado.getEmail(),
                usuarioLogado.getWhatsapp()
        );
        log.debug("DEBUG: Economia total: {}, Total pago no mês: {}", economiaTotal, totalPagoNoMes);
        log.debug("[finish] UsuarioApplicationService - getInfoUsuario");
        return infoUsuarioResponse;
    }
}