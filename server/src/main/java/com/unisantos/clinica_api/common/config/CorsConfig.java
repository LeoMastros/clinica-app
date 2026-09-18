package com.unisantos.clinica_api.common.config;

import java.time.Duration;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

/**
 * Libera o front para chamar a API.
 *
 * <p>O client e a API rodam em origens diferentes — em desenvolvimento por
 * causa da porta, e em producao porque cada um tem o seu subdominio. Sem esta
 * configuracao o navegador barra a requisicao no preflight e o login nao
 * funciona, mesmo com o servidor no ar e respondendo.
 *
 * <p>As origens permitidas vem do ambiente para a VM declarar o subdominio
 * real sem precisar recompilar. O padrao cobre o Vite e a porta que o
 * docker-compose publica para o client.
 */
@Configuration
public class CorsConfig {

    @Bean
    CorsConfigurationSource corsConfigurationSource(
            @Value("${clinica.seguranca.cors.origens}") List<String> origensPermitidas) {

        CorsConfiguration configuracao = new CorsConfiguration();
        configuracao.setAllowedOrigins(origensPermitidas);
        configuracao.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        configuracao.setAllowedHeaders(List.of("Authorization", "Content-Type"));
        // A autenticacao e por token no cabecalho, nao por cookie, entao nao ha
        // credencial de navegador para permitir aqui.
        configuracao.setAllowCredentials(false);
        configuracao.setMaxAge(Duration.ofHours(1));

        UrlBasedCorsConfigurationSource fonte = new UrlBasedCorsConfigurationSource();
        fonte.registerCorsConfiguration("/**", configuracao);
        return fonte;
    }
}
