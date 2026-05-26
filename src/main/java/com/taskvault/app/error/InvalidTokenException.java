package com.taskvault.app.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Erro indicando que foi informado token inválido para autenticação
 * de usuário
 */
@ResponseStatus(
    code = HttpStatus.UNAUTHORIZED,
    reason = "Token de acesso inválido"
)
public class InvalidTokenException extends RuntimeException {

    /**
     * Cria nova exceção informando mensagem
     * @param message Mensagem de erro
     */
    public InvalidTokenException(String message) {
        super(message);
    }

}
