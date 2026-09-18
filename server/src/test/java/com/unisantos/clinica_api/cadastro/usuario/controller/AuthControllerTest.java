package com.unisantos.clinica_api.cadastro.usuario.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.unisantos.clinica_api.TestcontainersConfiguration;
import com.unisantos.clinica_api.cadastro.usuario.dto.LoginRequest;
import com.unisantos.clinica_api.cadastro.usuario.entity.Perfil;
import com.unisantos.clinica_api.cadastro.usuario.entity.Usuario;
import com.unisantos.clinica_api.cadastro.usuario.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

/**
 * Fluxo de login de ponta a ponta contra um MySQL real.
 *
 * <p>Exige Docker em execucao na maquina.
 */
@SpringBootTest
@AutoConfigureMockMvc
@Import(TestcontainersConfiguration.class)
class AuthControllerTest {

    private static final String EMAIL = "psicologa@unisantos.br";
    private static final String SENHA = "senha-de-teste-123";

    @Autowired
    private MockMvc mockMvc;

    // Instanciado direto, e nao injetado: no Spring Boot 4 o contexto nao expoe
    // um bean de ObjectMapper, e aqui ele serve so para montar e ler JSON no
    // teste — nao precisa ser o mesmo que a aplicacao usa para serializar.
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private UsuarioRepository repository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void preparar() {
        repository.deleteAll();

        Usuario usuario = new Usuario();
        usuario.setNome("Carla Psicologa");
        usuario.setEmail(EMAIL);
        usuario.setSenhaHash(passwordEncoder.encode(SENHA));
        usuario.setPerfil(Perfil.PSYCHOLOGIST);
        repository.save(usuario);
    }

    private String json(String email, String senha) throws Exception {
        return objectMapper.writeValueAsString(new LoginRequest(email, senha));
    }

    @Test
    void loginValidoDevolveTokenEPerfilNoFormatoDoFront() throws Exception {
        mockMvc.perform(post("/auth/login").contentType(MediaType.APPLICATION_JSON).content(json(EMAIL, SENHA)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").isNotEmpty())
                .andExpect(jsonPath("$.user.email").value(EMAIL))
                .andExpect(jsonPath("$.user.name").value("Carla Psicologa"))
                // O front espera o valor em minusculo, nao o nome da constante.
                .andExpect(jsonPath("$.user.role").value("psychologist"))
                .andExpect(jsonPath("$.user.active").value(true))
                // O hash da senha nunca pode sair na resposta.
                .andExpect(jsonPath("$.user.senhaHash").doesNotExist());
    }

    @Test
    void senhaErradaDevolve401() throws Exception {
        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json(EMAIL, "senha-errada")))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void usuarioDesligadoNaoConsegueEntrar() throws Exception {
        Usuario usuario = repository.findByEmailIgnoreCase(EMAIL).orElseThrow();
        usuario.setAtivo(false);
        repository.saveAndFlush(usuario);

        mockMvc.perform(post("/auth/login").contentType(MediaType.APPLICATION_JSON).content(json(EMAIL, SENHA)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void emailInvalidoDevolve400() throws Exception {
        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json("nao-e-email", SENHA)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void meExigeToken() throws Exception {
        mockMvc.perform(get("/auth/me")).andExpect(status().isUnauthorized());
    }

    @Test
    void meDevolveOPerfilDoDonoDoToken() throws Exception {
        MvcResult login = mockMvc.perform(
                        post("/auth/login").contentType(MediaType.APPLICATION_JSON).content(json(EMAIL, SENHA)))
                .andExpect(status().isOk())
                .andReturn();

        String token = objectMapper
                .readTree(login.getResponse().getContentAsString())
                .get("token")
                .asText();

        mockMvc.perform(get("/auth/me").header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value(EMAIL))
                .andExpect(jsonPath("$.role").value("psychologist"));
    }
}
