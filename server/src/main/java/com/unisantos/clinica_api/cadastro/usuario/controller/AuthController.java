package com.unisantos.clinica_api.cadastro.usuario.controller;

import com.unisantos.clinica_api.cadastro.usuario.dto.LoginRequest;
import com.unisantos.clinica_api.cadastro.usuario.dto.LoginResponse;
import com.unisantos.clinica_api.cadastro.usuario.dto.UsuarioResponse;
import com.unisantos.clinica_api.cadastro.usuario.entity.Usuario;
import com.unisantos.clinica_api.cadastro.usuario.repository.UsuarioRepository;
import com.unisantos.clinica_api.common.security.TokenService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Autenticacao do sistema (issue #11).
 *
 * <p>Com o context-path {@code /api/v1}, as rotas ficam em
 * {@code /api/v1/auth/login} e {@code /api/v1/auth/me}, que e o que o client
 * declara em {@code client/src/core/api/endpoints.ts}.
 */
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;
    private final UsuarioRepository repository;

    public AuthController(
            AuthenticationManager authenticationManager,
            TokenService tokenService,
            UsuarioRepository repository) {
        this.authenticationManager = authenticationManager;
        this.tokenService = tokenService;
        this.repository = repository;
    }

    /**
     * Valida as credenciais e devolve o token junto do perfil do usuario.
     *
     * <p>Credencial errada ou conta desligada resultam em 401, sem distinguir
     * os dois casos na resposta — dizer qual dos dois falhou entregaria a
     * existencia do e-mail a quem esta tentando adivinhar.
     */
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest requisicao) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(requisicao.email(), requisicao.password()));

        Usuario usuario = repository.findByEmailIgnoreCase(requisicao.email()).orElseThrow();

        return ResponseEntity.ok(
                new LoginResponse(tokenService.gerar(usuario), UsuarioResponse.de(usuario)));
    }

    /** Perfil do usuario dono do token, usado pelo client para reidratar a sessao. */
    @GetMapping("/me")
    public ResponseEntity<UsuarioResponse> eu(@AuthenticationPrincipal Jwt token) {
        Usuario usuario = repository.findByEmailIgnoreCase(token.getSubject()).orElseThrow();
        return ResponseEntity.ok(UsuarioResponse.de(usuario));
    }
}
