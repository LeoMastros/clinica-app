package com.unisantos.clinica_api.cadastro.usuario.service;

import com.unisantos.clinica_api.cadastro.usuario.entity.Usuario;
import com.unisantos.clinica_api.cadastro.usuario.repository.UsuarioRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Entrega ao Spring Security o usuario da tabela {@code usuario} a partir do
 * e-mail informado no login.
 *
 * <p>Usuario desligado ({@code ativo = false}) entra como conta desabilitada,
 * entao o proprio Spring recusa o login sem precisarmos de checagem extra.
 * Isso mantem a regra de nao excluir ninguem sem deixar conta inativa valida.
 */
@Service
public class UsuarioDetailsService implements UserDetailsService {

    private final UsuarioRepository repository;

    public UsuarioDetailsService(UsuarioRepository repository) {
        this.repository = repository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Usuario usuario = repository.findByEmailIgnoreCase(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario nao encontrado"));

        return User.withUsername(usuario.getEmail())
                .password(usuario.getSenhaHash())
                .authorities(new SimpleGrantedAuthority("ROLE_" + usuario.getPerfil().name()))
                .disabled(!usuario.isAtivo())
                .build();
    }
}
