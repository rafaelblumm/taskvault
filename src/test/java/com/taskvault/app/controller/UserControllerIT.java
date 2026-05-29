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
import com.taskvault.app.payload.request.CreateUserRequest;
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
        var user = new CreateUserRequest(
            "johndoe",
            "John Doe",
            "johndoe@enterprise.com",
            "My!P4ssw0rd",
            UserRole.USER
        );
        var expectedResponse = new UserResponse(
            user.id(),
            user.name(),
            user.email(),
            user.role()
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

        Optional<User> createdUserOpt = userRepository.findById(user.id());
        assertTrue(createdUserOpt.isPresent());

        User createdUser = createdUserOpt.get();
        assertEquals(user.id(), createdUser.getId());
        assertEquals(user.name(), createdUser.getName());
        assertEquals(user.email(), createdUser.getEmail());
        assertEquals(user.role(), createdUser.getRole());
        assertNotEquals(user.password(), createdUser.getPassword());
        assertTrue(passwordEncoder.matches(user.password(), createdUser.getPassword()));

        var duplicatedUsername = new User(
            user.id(),
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
            user.email(),
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

    /** Testa remoção de usuário */
    @Test
    public void deleteUserTest() {
        var user = new CreateUserRequest(
            "johndoeivy",
            "John Doe Ivy",
            "johndoeivy@enterprise.com",
            "My!P4ssw0rd",
            UserRole.USER
        );
        getClient().post()
            .uri("/user")
            .headers((headers) -> headers.setBearerAuth(getAuthToken()))
            .body(user)
            .exchange();

        getClient().delete()
            .uri("/user/johndoeivy")
            .headers((headers) -> headers.setBearerAuth(getAuthToken()))
            .exchange()
            .expectStatus().isEqualTo(HttpStatus.NO_CONTENT);
        assertTrue(userRepository.findById("johndoeivy").isEmpty());
    }

    /** Testa remoção de usuário inexistente */
    @Test
    public void deleteInexistentUserTest() {
        getClient().delete()
            .uri("/user/inexistent")
            .headers((headers) -> headers.setBearerAuth(getAuthToken()))
            .exchange()
            .expectStatus().isEqualTo(HttpStatus.NOT_FOUND);
    }

}
