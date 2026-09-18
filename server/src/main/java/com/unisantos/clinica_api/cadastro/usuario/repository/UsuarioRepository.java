package com.unisantos.clinica_api.cadastro.usuario.repository;

import com.unisantos.clinica_api.cadastro.usuario.entity.Usuario;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Acesso a tabela {@code usuario}.
 *
 * <p>Usuario nunca e removido: desligamento e {@code ativo = false}, pela regra
 * de retencao do CFP/CRP. Os metodos {@code delete} herdados de
 * {@link JpaRepository} existem por heranca, mas nao devem ser chamados por
 * nenhum service ou controller — o uso deles fica restrito a preparacao de
 * cenario em teste. Nenhuma rota da API expoe exclusao.
 */
public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {

    /** Busca usada na autenticacao (issue #11). O e-mail e unico na tabela. */
    Optional<Usuario> findByEmailIgnoreCase(String email);

    /** Checagem de e-mail duplicado antes de cadastrar, para responder 409 em vez de erro de banco. */
    boolean existsByEmailIgnoreCase(String email);

    /** Listagem padrao da tela de gestao, que mostra apenas quem esta em atividade. */
    List<Usuario> findByAtivoTrueOrderByNomeAsc();
}
