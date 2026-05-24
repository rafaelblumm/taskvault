package com.taskvault.app.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/** Erro indicando que o usuário sendo criado já existe na aplicação */
@ResponseStatus(code = HttpStatus.CONFLICT, reason = "Usuário já existe")
public class UserAlreadyExistsException extends RuntimeException {};
