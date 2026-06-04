package com.taskvault.app.model;

/** Nível de permissão de um usuário */
public enum UserRole {
    /** Convidado */
    GUEST,
    /** Usuário */
    USER,
    /** Administrador */
    ADMIN,
    /** Administrador do sistema */
    SYSADMIN;

    /**
     * Indica se é permissão elevada
     * @return
     */
    public boolean isElevated() {
        return this == UserRole.ADMIN || this == UserRole.SYSADMIN;
    }
}
