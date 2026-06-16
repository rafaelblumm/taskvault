package com.taskvault.app.payload.response;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

import com.taskvault.app.model.Jwt;

public class LoginResponseTest {

    /**
     * Testa conversão de {@Code Jwt} para {@Code LoginResponse}
     */
    @Test
    public void fromJwtTest() {
        var now = LocalDateTime.now();
        var jwt = new Jwt("jwt-token", now);
        var expected = new LoginResponse("jwt-token", now);

        assertEquals(expected, LoginResponse.from(jwt));
    }

}
