package com.taskvault.app.controller;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpMethod;
import org.springframework.test.web.servlet.client.RestTestClient;

import com.taskvault.app.payload.request.LoginRequest;
import com.taskvault.app.payload.response.LoginResponse;

/** Classe abstrata utilizada em testes de integração de controllers que acessam endpoints protegidos */
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
abstract class AuthenticatedControllerIT {

    /** Porta do servidor */
    @LocalServerPort
    private int port;

    /** Client utilizado para realizar requisições HTTP */
    private RestTestClient client;
    /** Token de autenticação com usuário de teste */
    private String authToken;

    /** Configura client e recupera token de autenticação para realizar testes */
    @BeforeEach
    public void setup() {
        client = RestTestClient.bindToServer()
            .baseUrl("http://localhost:" + port)
            .build();

        authToken = client.method(HttpMethod.GET)
            .uri("/auth/login")
            .body(new LoginRequest("testuser", "integr4tionT&st"))
            .exchange()
            .returnResult(LoginResponse.class)
            .getResponseBody()
            .token();
    }

    /**
     * Retorna client utilizado para realizar requisições HTTP
     * @return Client configurado
     */
    public RestTestClient getClient() {
        return this.client;
    }

    /**
     * Retorna token de autenticação com usuário de teste
     * @return Token de usuário
     */
    public String getAuthToken() {
        return this.authToken;
    }

}
