package com.taskvault.app.model;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

/** Testes da classe de permissão de usuário */
public class UserRoleTest {

    /** Testa identificação de permissões elevadas */
    @Test
    public void isElevatedTest() {
        assertFalse(UserRole.GUEST.isElevated());
        assertFalse(UserRole.USER.isElevated());
        assertTrue(UserRole.ADMIN.isElevated());
        assertTrue(UserRole.SYSADMIN.isElevated());
    }

}
