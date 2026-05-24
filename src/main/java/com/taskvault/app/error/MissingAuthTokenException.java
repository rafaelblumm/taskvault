package com.taskvault.app.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Erro indicando que não foi informado nenhum token para autenticação
 * de usuário
 */
@ResponseStatus(
    code = HttpStatus.UNAUTHORIZED,
    reason = "Token de acesso não informado"
)
public class MissingAuthTokenException extends RuntimeException {}
