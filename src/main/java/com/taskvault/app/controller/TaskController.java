package com.taskvault.app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.taskvault.app.error.MissingAuthTokenException;
import com.taskvault.app.error.UserNotFoundException;
import com.taskvault.app.payload.request.CreateTaskRequest;
import com.taskvault.app.payload.response.TaskResponse;
import com.taskvault.app.service.TaskService;

/** Controller das funcinalidades de gestão de tarefas */
@RestController
@RequestMapping("/task")
public class TaskController {

    /** Acesso a camada de dados de usuários */
    @Autowired
    private TaskService taskService;

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
        return TaskResponse.from(taskService.createTask(taskDto));
    }

}
