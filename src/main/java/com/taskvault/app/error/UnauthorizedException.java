package com.taskvault.app.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/** Erro indicando que usuário não possui permissão de acesso ao recurso */
@ResponseStatus(code = HttpStatus.FORBIDDEN, reason = "Usuário não tem permissão")
public class UnauthorizedException extends RuntimeException {}
