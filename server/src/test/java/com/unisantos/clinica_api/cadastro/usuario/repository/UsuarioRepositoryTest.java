package com.unisantos.clinica_api.cadastro.usuario.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.unisantos.clinica_api.TestcontainersConfiguration;
import com.unisantos.clinica_api.cadastro.usuario.entity.Perfil;
import com.unisantos.clinica_api.cadastro.usuario.entity.TipoProfissional;
import com.unisantos.clinica_api.cadastro.usuario.entity.Usuario;
import java.time.LocalDate;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.dao.DataIntegrityViolationException;

/**
 * Sobe um MySQL real via Testcontainers, aplica as migrations do Flyway e
 * valida o mapeamento da entidade contra a tabela criada pela V20.
 *
 * <p>Exige Docker em execucao na maquina.
 */
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(TestcontainersConfiguration.class)
class UsuarioRepositoryTest {

    @Autowired
    private UsuarioRepository repository;

    private Usuario novoUsuario(String email, Perfil perfil) {
        Usuario usuario = new Usuario();
        usuario.setNome("Maria de Souza");
        usuario.setEmail(email);
        usuario.setSenhaHash("{bcrypt}$2a$10$abcdefghijklmnopqrstuv");
        usuario.setPerfil(perfil);
        return usuario;
    }

    @Test
    void salvaUsuarioERecuperaPeloEmailIgnorandoMaiusculas() {
        Usuario usuario = novoUsuario("maria.souza@unisantos.br", Perfil.PSYCHOLOGIST);
        usuario.setTipoProfissional(TipoProfissional.STUDENT_INTERN);
        usuario.setDataFimRegistro(LocalDate.of(2026, 11, 30));

        repository.saveAndFlush(usuario);

        Optional<Usuario> encontrado = repository.findByEmailIgnoreCase("MARIA.SOUZA@UNISANTOS.BR");

        assertThat(encontrado).isPresent();
        assertThat(encontrado.get().getId()).isNotNull();
        assertThat(encontrado.get().getPerfil()).isEqualTo(Perfil.PSYCHOLOGIST);
        assertThat(encontrado.get().getTipoProfissional()).isEqualTo(TipoProfissional.STUDENT_INTERN);
        assertThat(encontrado.get().getDataFimRegistro()).isEqualTo(LocalDate.of(2026, 11, 30));
    }

    @Test
    void usuarioNasceAtivoEComDataDeCriacaoPreenchida() {
        Usuario salvo = repository.saveAndFlush(novoUsuario("joao@unisantos.br", Perfil.SECRETARY));

        assertThat(salvo.isAtivo()).isTrue();
        assertThat(salvo.getDataCriacao()).isNotNull();
        assertThat(salvo.getDataAtualizacao()).isNull();
    }

    @Test
    void secretariaPodeFicarSemTipoProfissional() {
        Usuario salvo = repository.saveAndFlush(novoUsuario("recepcao@unisantos.br", Perfil.SECRETARY));

        assertThat(salvo.getTipoProfissional()).isNull();
    }

    @Test
    void naoPermiteDoisUsuariosComOMesmoEmail() {
        repository.saveAndFlush(novoUsuario("duplicado@unisantos.br", Perfil.ADMIN));

        assertThatThrownBy(
                        () -> repository.saveAndFlush(novoUsuario("duplicado@unisantos.br", Perfil.PSYCHOLOGIST)))
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    void listaApenasUsuariosAtivosEmOrdemDeNome() {
        Usuario desligado = novoUsuario("desligado@unisantos.br", Perfil.PSYCHOLOGIST);
        desligado.setNome("Ana Desligada");
        desligado.setAtivo(false);
        repository.saveAndFlush(desligado);

        Usuario ativo = novoUsuario("ativo@unisantos.br", Perfil.PSYCHOLOGIST);
        ativo.setNome("Bruno Ativo");
        repository.saveAndFlush(ativo);

        assertThat(repository.findByAtivoTrueOrderByNomeAsc())
                .extracting(Usuario::getEmail)
                .containsExactly("ativo@unisantos.br");
    }
}
