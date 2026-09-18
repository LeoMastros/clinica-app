package com.unisantos.clinica_api.common.exception;

import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Padroniza as respostas de erro da API.
 *
 * <p>Sem isto, a excecao de credencial invalida lancada dentro do controller
 * viraria 500, porque ela nao passa pelo tratamento do filtro do Spring
 * Security.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(AuthenticationException.class)
    ProblemDetail credencialInvalida(AuthenticationException excecao) {
        ProblemDetail problema = ProblemDetail.forStatus(HttpStatus.UNAUTHORIZED);
        problema.setTitle("Falha na autenticacao");
        // Mensagem generica de proposito: nao revela se o e-mail existe.
        problema.setDetail("E-mail ou senha invalidos.");
        problema.setProperty("timestamp", Instant.now());
        return problema;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    ProblemDetail dadosInvalidos(MethodArgumentNotValidException excecao) {
        Map<String, String> campos = new LinkedHashMap<>();
        excecao.getBindingResult()
                .getFieldErrors()
                .forEach(erro -> campos.putIfAbsent(erro.getField(), erro.getDefaultMessage()));

        ProblemDetail problema = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        problema.setTitle("Dados invalidos");
        problema.setDetail("Verifique os campos informados.");
        problema.setProperty("fields", campos);
        problema.setProperty("timestamp", Instant.now());
        return problema;
    }
}
