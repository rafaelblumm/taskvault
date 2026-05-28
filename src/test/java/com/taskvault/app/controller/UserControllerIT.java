package com.taskvault.app.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.client.RestTestClient;

import com.taskvault.app.model.User;
import com.taskvault.app.model.UserRole;
import com.taskvault.app.payload.request.LoginRequest;
import com.taskvault.app.payload.response.LoginResponse;
import com.taskvault.app.payload.response.UserResponse;
import com.taskvault.app.repository.UserRepository;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class UserControllerIT {

    @LocalServerPort
    private int port;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

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

    /** Testa criação de novo usuário e tentativas de criação de usuários já existentes */
    @Test
    public void createUserTest() {
        var user = new User(
            "johndoe",
            "John Doe",
            "johndoe@enterprise.com",
            UserRole.USER,
            "My!P4ssw0rd"
        );
        var expectedResponse = new UserResponse(
            user.getId(),
            user.getName(),
            user.getEmail(),
            user.getRole()
        );
        client.post()
            .uri("/user")
            .headers((headers) -> headers.setBearerAuth(authToken))
            .accept(MediaType.APPLICATION_JSON)
            .body(user)
            .exchange()
            .expectStatus().isEqualTo(HttpStatus.CREATED)
            .expectBody(UserResponse.class)
            .consumeWith((result) -> assertEquals(expectedResponse, result.getResponseBody()));

        Optional<User> createdUserOpt = userRepository.findById(user.getId());
        assertTrue(createdUserOpt.isPresent());

        User createdUser = createdUserOpt.get();
        assertEquals(user.getId(), createdUser.getId());
        assertEquals(user.getName(), createdUser.getName());
        assertEquals(user.getEmail(), createdUser.getEmail());
        assertEquals(user.getRole(), createdUser.getRole());
        assertNotEquals(user.getPassword(), createdUser.getPassword());
        assertTrue(passwordEncoder.matches(user.getPassword(), createdUser.getPassword()));

        var duplicatedUsername = new User(
            user.getId(),
            "Same Username",
            "sameusername@dev.com",
            UserRole.USER,
            "passwd"
        );
        client.post()
            .uri("/user")
            .headers((headers) -> headers.setBearerAuth(authToken))
            .body(duplicatedUsername)
            .exchange()
            .expectStatus().isEqualTo(HttpStatus.CONFLICT);

        var duplicatedEmail = new User(
            "newid",
            "Same Email",
            user.getEmail(),
            UserRole.USER,
            "passwd"
        );
        client.post()
            .uri("/user")
            .headers((headers) -> headers.setBearerAuth(authToken))
            .body(duplicatedEmail)
            .exchange()
            .expectStatus().isEqualTo(HttpStatus.CONFLICT);
    }

}
