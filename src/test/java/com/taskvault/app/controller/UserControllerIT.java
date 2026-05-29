package com.taskvault.app.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.taskvault.app.model.User;
import com.taskvault.app.model.UserRole;
import com.taskvault.app.payload.response.UserResponse;
import com.taskvault.app.repository.UserRepository;

/** Testes de integração dos endpoints de gestão de usuários */
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class UserControllerIT extends AuthenticatedControllerIT {

    /** Acesso a camada de persistência de dados de usuários */
    @Autowired
    private UserRepository userRepository;

    /** Codificador de senhas */
    @Autowired
    private PasswordEncoder passwordEncoder;

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
        getClient().post()
            .uri("/user")
            .headers((headers) -> headers.setBearerAuth(getAuthToken()))
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
        getClient().post()
            .uri("/user")
            .headers((headers) -> headers.setBearerAuth(getAuthToken()))
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
        getClient().post()
            .uri("/user")
            .headers((headers) -> headers.setBearerAuth(getAuthToken()))
            .body(duplicatedEmail)
            .exchange()
            .expectStatus().isEqualTo(HttpStatus.CONFLICT);
    }

}
