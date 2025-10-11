package com.divertech.raxae.auth.config;

import com.divertech.raxae.usuario.application.repository.UsuarioRepository;
import com.divertech.raxae.usuario.domain.Usuario;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import java.util.List;

@Profile("dev")
@Component
@RequiredArgsConstructor
@Log4j2
public class DataSeeder implements CommandLineRunner {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        if (usuarioRepository.count() == 0) {
            log.info("Banco de dados vazio. Populando com dados iniciais de teste...");
            createUsers();
        } else {
            log.info("Banco de dados já contém dados. O Seeder não será executado.");
        }
    }

    private void createUsers() {
        Usuario admin = Usuario.builder()
                .nomeCompleto("Admin Teste")
                .email("email.do.admin@teste.com")
                .senha(passwordEncoder.encode("senhaDoAdmin"))
                .whatsapp("99999999999")
                .build();

        Usuario naoAdmin = Usuario.builder()
                .nomeCompleto("Usuario Comum")
                .email("email.comum@teste.com")
                .senha(passwordEncoder.encode("senhaComum"))
                .whatsapp("88888888888")
                .build();

        Usuario novoMembro = Usuario.builder()
                .nomeCompleto("Novo Membro")
                .email("email.novo.membro@teste.com")
                .senha(passwordEncoder.encode("senhaNovoMembro"))
                .whatsapp("77777777777")
                .build();
        
        usuarioRepository.saveAll(List.of(admin, naoAdmin, novoMembro));
        
        log.info("Usuários de teste criados com sucesso!");
    }
}