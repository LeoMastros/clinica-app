package com.unisantos.clinica_api.common.security;

import com.unisantos.clinica_api.cadastro.usuario.entity.Usuario;
import java.time.Duration;
import java.time.Instant;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

/** Emite o token JWT devolvido no login. */
@Service
public class TokenService {

    private final JwtEncoder encoder;
    private final Duration validade;

    public TokenService(JwtEncoder encoder, @Value("${clinica.seguranca.jwt.validade}") Duration validade) {
        this.encoder = encoder;
        this.validade = validade;
    }

    public String gerar(Usuario usuario) {
        Instant agora = Instant.now();

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("clinica-api")
                .issuedAt(agora)
                .expiresAt(agora.plus(validade))
                // O e-mail identifica o usuario no token; e unico na tabela.
                .subject(usuario.getEmail())
                .claim("perfil", usuario.getPerfil().name())
                .claim("nome", usuario.getNome())
                .build();

        JwsHeader cabecalho = JwsHeader.with(MacAlgorithm.HS256).build();
        return encoder.encode(JwtEncoderParameters.from(cabecalho, claims)).getTokenValue();
    }
}
