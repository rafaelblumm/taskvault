package com.taskvault.app.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.taskvault.app.payload.request.CreateUserRequest;
import com.taskvault.app.payload.request.UpdateUserRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.taskvault.app.model.User;
import com.taskvault.app.model.UserRole;
import com.taskvault.app.repository.UserRepository;

import java.util.Optional;

/** Testes da classe de gestão de usuários */
@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AuthService authService;

    /** Testa criação de novo usuário */
    @Test
    public void createUserTest() {
        var userDto = new CreateUserRequest(
            "teste",
            "Usuário Teste",
            "teste@dev.com",
            UserRole.GUEST,
            "SuperSecretPassword123"
        );

        String encodedPassword = userDto.password() + "[encoded]";
        var expected = new User(
            userDto.id(),
            userDto.name(),
            userDto.email(),
            userDto.role(),
            encodedPassword
        );

        when(passwordEncoder.encode(userDto.password())).thenReturn(encodedPassword);
        when(userRepository.save(any(User.class))).thenReturn(expected);
        var createdUser = userService.createUser(userDto);

        verify(userRepository, times(1)).save(ArgumentMatchers.any());

        assertEquals(expected, createdUser);
    }

    /** Testa atualização de usuário **/
    @Test
    public void updateUserTest() {
        var user = new User(
            "teste",
            "Usuário Teste",
            "teste@dev.com",
            UserRole.GUEST,
            "SuperSecretPassword123"
        );
        var userDto = new UpdateUserRequest(
            "testeAtualizado",
            "testeAtualizado@dev.com",
            UserRole.GUEST,
            "SenhaAtualizada"
        );

        String encodedPassword = userDto.password() + "[encoded]";
        var expected = new User(
            "teste",
            "testeAtualizado",
            "testeAtualizado@dev.com",
            UserRole.GUEST,
            encodedPassword
        );

        when(passwordEncoder.encode(userDto.password())).thenReturn(encodedPassword);
        when(userRepository.findById("teste")).thenReturn(Optional.of(user));
        when(authService.canUpdateResource(user)).thenReturn(true);
        when(userRepository.save(any(User.class))).thenReturn(expected);

        var createdUser = userService.updateUser("teste", userDto);

        assertEquals(expected, createdUser);
    }

    /** Testa procura de usuário **/
    @Test
    public void getUserTest() {
        var user = new User(
            "teste",
            "Usuário Teste",
            "teste@dev.com",
            UserRole.GUEST,
            "SuperSecretPassword123"
        );

        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));

        assertEquals(userService.getUser(user.getId()), user);
    }

    /** Testa deleção de usuário **/
    @Test
    public void deleteUserTest() {
        var user = new User(
            "teste",
            "Usuário Teste",
            "teste@dev.com",
            UserRole.GUEST,
            "SuperSecretPassword123"
        );
        when(userRepository.findById("teste")).thenReturn(Optional.of(user));
        userService.deleteUser(user.getId());

        assertTrue(user.getDeletedAt().isPresent());
        verify(userRepository, never()).deleteById("teste");
    }
}
