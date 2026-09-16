package com.unisantos.clinica_api.cadastro.usuario.repository;

import com.unisantos.clinica_api.cadastro.usuario.entity.Usuario;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Acesso a tabela {@code usuario}.
 *
 * <p>Sem nenhum metodo de exclusao: a remocao de usuario e proibida pela regra
 * de retencao do projeto. Desligamento e {@code ativo = false}.
 */
public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {

    /** Busca usada na autenticacao (issue #11). O e-mail e unico na tabela. */
    Optional<Usuario> findByEmailIgnoreCase(String email);

    /** Checagem de e-mail duplicado antes de cadastrar, para responder 409 em vez de erro de banco. */
    boolean existsByEmailIgnoreCase(String email);

    /** Listagem padrao da tela de gestao, que mostra apenas quem esta em atividade. */
    List<Usuario> findByAtivoTrueOrderByNomeAsc();
}
