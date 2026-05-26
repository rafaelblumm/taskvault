package com.taskvault.app.payload.response;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import com.taskvault.app.model.User;
import com.taskvault.app.model.UserRole;

/** Testes do DTO de resposta de usuários */
public class UserResponseTest {

    /**
     * Testa conversão de {@Code User} para {@Code UserResponse}
     */
    @Test
    public void fromUserTest() {
        var user = new User(
            "johndoe",
            "John Doe",
            "johndoe@enterprise.com",
            UserRole.USER,
            "My!P4ssw0rd"
        );
        var expected = new UserResponse(
            "johndoe",
            "John Doe",
            "johndoe@enterprise.com",
            UserRole.USER
        );
        assertEquals(expected, UserResponse.from(user));
    }

}
