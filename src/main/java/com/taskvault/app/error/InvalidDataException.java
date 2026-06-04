package com.taskvault.app.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/** Erro indicando que usuário informou dados inválidos */
@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "Dados inválidos")
public class InvalidDataException extends RuntimeException {}
