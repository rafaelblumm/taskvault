package com.taskvault.app.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.taskvault.app.error.MissingAuthTokenException;
import com.taskvault.app.error.UserNotFoundException;
import com.taskvault.app.model.Task;
import com.taskvault.app.model.User;
import com.taskvault.app.payload.request.CreateTaskRequest;
import com.taskvault.app.payload.response.TaskResponse;
import com.taskvault.app.repository.TaskRepository;
import com.taskvault.app.repository.UserRepository;
import com.taskvault.app.security.SecurityUtils;

/** Controller das funcinalidades de gestão de tarefas */
@RestController
@RequestMapping("/task")
public class TaskController {

    /** Acesso a camada de dados de tarefas */
    @Autowired
    private TaskRepository taskRepository;

    /** Acesso a camada de dados de usuários */
    @Autowired
    private UserRepository userRepository;

    /**
     * Endpoint de criação de tarefas
     * @param taskData DTO com dados da tarefa
     * @return Tarefa criada
     * @throws MissingAuthTokenException ID de usuário não encontrado no token
     * @throws UserNotFoundException Usuário não encontrado (criador ou designado)
     */
    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    public TaskResponse createTask(@RequestBody CreateTaskRequest taskDto)
    throws MissingAuthTokenException, UserNotFoundException {
        Authentication auth = SecurityUtils.getAuthenticatedUser()
            .orElseThrow(() -> new MissingAuthTokenException());
        User creator = userRepository.findById(auth.getPrincipal().toString())
            .orElseThrow(() -> new UserNotFoundException());

        var task = new Task(taskDto, creator, getAssigneeFromDto(taskDto));
        Task createdTask = taskRepository.save(task);

        return TaskResponse.from(createdTask);
    }

    /**
     * Busca dados de usuário a partir do ID informado no DTO
     * @param taskDto DTO com dados da tarefa
     * @return Usuário, se informado ID no DTO
     * @throws UserNotFoundException Usuário não encontrado
     */
    private Optional<User> getAssigneeFromDto(CreateTaskRequest taskDto)
    throws UserNotFoundException {
        if (taskDto.assignee().isEmpty()) return Optional.empty();

        return taskDto.assignee()
            .map((id) -> userRepository.findById(id))
            .orElseThrow(() -> new UserNotFoundException());
    }

}
