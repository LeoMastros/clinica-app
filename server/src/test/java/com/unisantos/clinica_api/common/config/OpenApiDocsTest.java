package com.unisantos.clinica_api.common.config;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.unisantos.clinica_api.TestcontainersConfiguration;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

/**
 * Garante que a documentacao da API continue de pe e descrevendo os campos.
 *
 * <p>Sem isto, alguem pode remover uma anotacao sem querer e a documentacao
 * volta a listar as rotas sem dizer o que enviar — que era exatamente o
 * problema que a issue #17 pediu para resolver.
 */
@SpringBootTest
@AutoConfigureMockMvc
@Import(TestcontainersConfiguration.class)
class OpenApiDocsTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void aDocumentacaoEhPublica() throws Exception {
        // Quem for consumir a API precisa ler os campos sem ter conta ainda.
        mockMvc.perform(get("/v3/api-docs")).andExpect(status().isOk());
    }

    @Test
    void asDuasRotasDeAutenticacaoEstaoDocumentadas() throws Exception {
        mockMvc.perform(get("/v3/api-docs"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.paths['/auth/login'].post.summary").isNotEmpty())
                .andExpect(jsonPath("$.paths['/auth/me'].get.summary").isNotEmpty());
    }

    @Test
    void oLoginDescreveOsCamposQueRecebeEOsCodigosQueDevolve() throws Exception {
        mockMvc.perform(get("/v3/api-docs"))
                .andExpect(status().isOk())
                // o que enviar
                .andExpect(jsonPath("$.components.schemas.LoginRequest.properties.email.description").isNotEmpty())
                .andExpect(jsonPath("$.components.schemas.LoginRequest.properties.password.description").isNotEmpty())
                // o que volta
                .andExpect(jsonPath("$.components.schemas.LoginResponse.properties.token.description").isNotEmpty())
                .andExpect(jsonPath("$.paths['/auth/login'].post.responses.200").exists())
                .andExpect(jsonPath("$.paths['/auth/login'].post.responses.401").exists());
    }

    @Test
    void oPerfilDevolvidoDescreveTodosOsCampos() throws Exception {
        mockMvc.perform(get("/v3/api-docs"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.components.schemas.UsuarioResponse.properties.id.description").isNotEmpty())
                .andExpect(jsonPath("$.components.schemas.UsuarioResponse.properties.name.description").isNotEmpty())
                .andExpect(jsonPath("$.components.schemas.UsuarioResponse.properties.email.description").isNotEmpty())
                .andExpect(jsonPath("$.components.schemas.UsuarioResponse.properties.role.description").isNotEmpty())
                .andExpect(jsonPath("$.components.schemas.UsuarioResponse.properties.active.description").isNotEmpty());
    }

    @Test
    void oEsquemaDeTokenEstaDeclaradoParaDarParaTestarPelaUI() throws Exception {
        mockMvc.perform(get("/v3/api-docs"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.components.securitySchemes.bearerAuth.scheme").value("bearer"))
                .andExpect(jsonPath("$.components.securitySchemes.bearerAuth.bearerFormat").value("JWT"));
    }

    @Test
    void oTokenEhExigidoPorPadraoEmTodaAApi() throws Exception {
        // A exigencia e declarada uma vez na raiz do documento e vale para
        // todas as rotas; o login a sobrescreve com @SecurityRequirements vazio.
        // Que o login funcione sem token e verificado de verdade no
        // AuthControllerTest, que chama a rota sem cabecalho nenhum.
        mockMvc.perform(get("/v3/api-docs"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.security[0].bearerAuth").exists());
    }
}
