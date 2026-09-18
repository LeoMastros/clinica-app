package com.unisantos.clinica_api.common.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuracao da documentacao da API.
 *
 * <p>A UI fica em {@code /api/v1/swagger-ui.html} e o contrato em JSON em
 * {@code /api/v1/v3/api-docs}. As duas ficam abertas sem autenticacao, para
 * quem for consumir a API conseguir ler os campos sem precisar de conta.
 *
 * <p>O esquema de seguranca declarado aqui e o que faz aparecer o botao
 * "Authorize" na UI: da para colar o token devolvido pelo login e testar as
 * rotas protegidas direto pelo navegador, sem Postman.
 */
@Configuration
public class OpenApiConfig {

    private static final String ESQUEMA_BEARER = "bearerAuth";

    @Bean
    OpenAPI clinicaOpenApi() {
        return new OpenAPI()
                .info(new Info()
                        .title("Clinica API — PsiUnisantos")
                        .version("v1")
                        .description(
                                """
                                API do sistema de gestao da clinica-escola de psicologia.

                                Autenticacao: chame `POST /auth/login` com e-mail e senha, copie o \
                                campo `token` da resposta e informe em "Authorize" no topo desta \
                                pagina. A partir dai as rotas protegidas podem ser testadas aqui \
                                mesmo.

                                Nenhuma rota exclui dado. Desligamento e feito por inativacao, \
                                pela regra de retencao do CFP/CRP."""))
                .components(new Components()
                        .addSecuritySchemes(ESQUEMA_BEARER, new SecurityScheme()
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")
                                .description("Token devolvido por POST /auth/login.")))
                // Vale para todas as rotas; as publicas sobrescrevem com
                // @SecurityRequirements vazio no proprio controller.
                .addSecurityItem(new SecurityRequirement().addList(ESQUEMA_BEARER));
    }
}
