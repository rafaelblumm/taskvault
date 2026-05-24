package com.taskvault.app.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Erro indicando que o usuário informado não foi encontrado
 * de usuário
 */
@ResponseStatus(
    code = HttpStatus.NOT_FOUND,
    reason = "Usuário não encontrado"
)
public class UserNotFoundException extends RuntimeException {}
