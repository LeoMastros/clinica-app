package com.unisantos.clinica_api.common.security;

import com.unisantos.clinica_api.cadastro.usuario.entity.Perfil;
import com.unisantos.clinica_api.cadastro.usuario.entity.Usuario;
import com.unisantos.clinica_api.cadastro.usuario.repository.UsuarioRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * Cria o primeiro administrador no boot, quando a tabela ainda esta vazia.
 *
 * <p>Sem isto ninguem conseguiria entrar no sistema recem-instalado, ja que nao
 * ha cadastro publico. E-mail e senha vem de variavel de ambiente: colocar um
 * hash fixo numa migration deixaria a senha do admin versionada no git.
 *
 * <p>Se as variaveis nao estiverem definidas, nada e criado e fica o aviso no
 * log — assim o deploy nao cria uma conta com senha previsivel por descuido.
 */
@Component
public class AdminInicial implements ApplicationRunner {

    private static final Logger log = LoggerFactory.getLogger(AdminInicial.class);

    private final UsuarioRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final String nome;
    private final String email;
    private final String senha;

    public AdminInicial(
            UsuarioRepository repository,
            PasswordEncoder passwordEncoder,
            @Value("${clinica.admin-inicial.nome:Administrador}") String nome,
            @Value("${clinica.admin-inicial.email:}") String email,
            @Value("${clinica.admin-inicial.senha:}") String senha) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
        this.nome = nome;
        this.email = email;
        this.senha = senha;
    }

    @Override
    public void run(ApplicationArguments args) {
        if (repository.count() > 0) {
            return;
        }

        if (email.isBlank() || senha.isBlank()) {
            log.warn("Nenhum usuario cadastrado e CLINICA_ADMIN_INICIAL_EMAIL/SENHA nao foram "
                    + "definidos. O administrador inicial nao foi criado.");
            return;
        }

        Usuario admin = new Usuario();
        admin.setNome(nome);
        admin.setEmail(email);
        admin.setSenhaHash(passwordEncoder.encode(senha));
        admin.setPerfil(Perfil.ADMIN);
        repository.save(admin);

        log.info("Administrador inicial criado para {}.", email);
    }
}
