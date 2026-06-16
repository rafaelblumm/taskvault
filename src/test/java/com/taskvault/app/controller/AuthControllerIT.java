package com.taskvault.app.controller;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.HttpStatus;

import com.taskvault.app.payload.response.LoginResponse;

/** Testes de integração dos endpoints de autenticação */
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class AuthControllerIT extends AuthenticatedControllerIT {

    /**
     * Testa atualização de tokens de usuário
     * @throws InterruptedException
     */
    @Test
    public void refreshAndDeauthUserAuthTest() throws InterruptedException {
        getClient().get()
            .uri("/user/" + TEST_USER)
            .headers((headers) -> headers.setBearerAuth(getAuthToken()))
            .exchange()
            .expectStatus().isEqualTo(HttpStatus.OK);

        Thread.sleep(2000);
        String newToken = getClient().post()
            .uri("/auth/refresh")
            .headers((headers) -> headers.setBearerAuth(getAuthToken()))
            .exchange()
            .expectStatus().isEqualTo(HttpStatus.OK)
            .expectBody(LoginResponse.class)
            .returnResult()
            .getResponseBody()
            .token();

        getClient().get()
            .uri("/user/" + TEST_USER)
            .headers((headers) -> headers.setBearerAuth(getAuthToken()))
            .exchange()
            .expectStatus().isEqualTo(HttpStatus.FORBIDDEN);

        getClient().get()
            .uri("/user/" + TEST_USER)
            .headers((headers) -> headers.setBearerAuth(newToken))
            .exchange()
            .expectStatus().isEqualTo(HttpStatus.OK);

        getClient().post()
            .uri("/auth/logout")
            .headers((headers) -> headers.setBearerAuth(newToken))
            .exchange()
            .expectStatus().isEqualTo(HttpStatus.OK);

        getClient().get()
            .uri("/user/" + TEST_USER)
            .headers((headers) -> headers.setBearerAuth(newToken))
            .exchange()
            .expectStatus().isEqualTo(HttpStatus.FORBIDDEN);
    }

}
