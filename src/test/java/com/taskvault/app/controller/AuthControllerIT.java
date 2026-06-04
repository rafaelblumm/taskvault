package com.taskvault.app.controller;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.HttpStatus;

/** Testes de integração dos endpoints de autenticação */
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class AuthControllerIT extends AuthenticatedControllerIT {

    /** Testa desautenticação de usuários */
    @Test
    public void deauthenticateUserTest() {
        getClient().get()
            .uri("/user/" + TEST_USER)
            .headers((headers) -> headers.setBearerAuth(getAuthToken()))
            .exchange()
            .expectStatus().isEqualTo(HttpStatus.OK);

        getClient().post()
            .uri("/auth/logout")
            .headers((headers) -> headers.setBearerAuth(getAuthToken()))
            .exchange()
            .expectStatus().isEqualTo(HttpStatus.OK);

        getClient().get()
            .uri("/user/" + TEST_USER)
            .headers((headers) -> headers.setBearerAuth(getAuthToken()))
            .exchange()
            .expectStatus().isEqualTo(HttpStatus.FORBIDDEN);
    }

}
