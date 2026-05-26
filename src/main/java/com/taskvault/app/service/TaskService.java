package com.taskvault.app.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.taskvault.app.error.MissingAuthTokenException;
import com.taskvault.app.error.UserNotFoundException;
import com.taskvault.app.model.Task;
import com.taskvault.app.model.User;
import com.taskvault.app.payload.request.CreateTaskRequest;
import com.taskvault.app.repository.TaskRepository;
import com.taskvault.app.security.SecurityUtils;

/** Serviço de gerenciamento de tarefas */
@Service
public class TaskService {

    /** Acesso a camada de dados de tarefas */
    @Autowired
    private TaskRepository taskRepository;

    /** Serviço de gerenciamento de usuários */
    @Autowired
    private UserService userService;

    /**
     * Cria novo registro de tarefa com o usuário corrente como criador
     * @param taskDto Dados da tarefa
     * @return Tarefa criada
     * @throws MissingAuthTokenException Se não for informado usuário
     * @throws UserNotFoundException Se IDs de usuários não existirem
     */
    public Task createTask(CreateTaskRequest taskDto)
    throws MissingAuthTokenException, UserNotFoundException {
        Authentication auth = SecurityUtils.getAuthenticatedUser()
            .orElseThrow(() -> new MissingAuthTokenException());
        User creator = userService.getUser(auth.getName());

        return createTask(taskDto, creator);
    }

    /**
     * Cria novo registro de tarefa
     * @param taskDto Dados da tarefa
     * @param user Usuário criador da tarefa
     * @return Tarefa criada
     * @throws UserNotFoundException Se IDs de usuários não existirem
     */
    protected Task createTask(CreateTaskRequest taskDto, User creator)
    throws UserNotFoundException {
        Optional<User> assignee = taskDto.assignee()
            .map((id) -> userService.getUser(id));
        var task = new Task(taskDto, creator, assignee);

        return taskRepository.save(task);
    }

}
