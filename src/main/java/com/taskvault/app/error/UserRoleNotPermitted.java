package com.taskvault.app.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(
        code = HttpStatus.UNAUTHORIZED,
        reason = "Usuário não tem permissão"
)
public class UserRoleNotPermitted extends RuntimeException {}
