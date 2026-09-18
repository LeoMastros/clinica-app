package com.unisantos.clinica_api.common.config;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.options;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.unisantos.clinica_api.TestcontainersConfiguration;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

/**
 * O front chama a API de outra origem: em desenvolvimento por causa da porta,
 * em producao porque cada um tem o seu subdominio.
 *
 * <p>Estes testes existem porque a falta de CORS nao aparece em teste de
 * servidor nenhum — o servidor responde normalmente, e quem barra e o
 * navegador. Sem eles, a regressao so apareceria como "o login parou de
 * funcionar" depois do deploy.
 */
@SpringBootTest
@AutoConfigureMockMvc
@Import(TestcontainersConfiguration.class)
class CorsConfigTest {

    private static final String ORIGEM_DO_FRONT = "http://localhost:5173";

    @Autowired
    private MockMvc mockMvc;

    @Test
    void oPreflightDoLoginEhLiberadoParaAOrigemDoFront() throws Exception {
        mockMvc.perform(options("/auth/login")
                        .header("Origin", ORIGEM_DO_FRONT)
                        .header("Access-Control-Request-Method", "POST")
                        .header("Access-Control-Request-Headers", "content-type"))
                .andExpect(status().isOk())
                .andExpect(header().string("Access-Control-Allow-Origin", ORIGEM_DO_FRONT));
    }

    @Test
    void oCabecalhoDeAutorizacaoEhAceitoNoPreflight() throws Exception {
        // Sem isto o /auth/me nao conseguiria mandar o token pelo navegador.
        // O Spring ecoa de volta apenas os cabecalhos que foram pedidos, e nao
        // a lista inteira de permitidos, entao a assercao olha so o que importa.
        mockMvc.perform(options("/auth/me")
                        .header("Origin", ORIGEM_DO_FRONT)
                        .header("Access-Control-Request-Method", "GET")
                        .header("Access-Control-Request-Headers", "authorization"))
                .andExpect(status().isOk())
                .andExpect(header().string("Access-Control-Allow-Headers", containsString("Authorization")));
    }

    @Test
    void origemDesconhecidaNaoEhLiberada() throws Exception {
        mockMvc.perform(options("/auth/login")
                        .header("Origin", "http://site-qualquer.example")
                        .header("Access-Control-Request-Method", "POST"))
                .andExpect(status().isForbidden());
    }
}
