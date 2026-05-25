package com.taskvault.app.security;

import java.util.Optional;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/** Classe com funções utilitárias de rotinas de segurança */
public class SecurityUtils {

    /**
     * Busca usuário autenticado
     * @return Dados do usuário autenticado, se houver
     */
    public static Optional<Authentication> getAuthenticatedUser() {
        return Optional.ofNullable(SecurityContextHolder.getContext().getAuthentication());
    }

}
