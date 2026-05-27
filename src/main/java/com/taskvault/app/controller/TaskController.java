package com.taskvault.app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.taskvault.app.error.MissingAuthTokenException;
import com.taskvault.app.error.TaskNotFoundException;
import com.taskvault.app.error.UserNotFoundException;
import com.taskvault.app.payload.request.CreateTaskRequest;
import com.taskvault.app.payload.request.UpdateTaskRequest;
import com.taskvault.app.payload.response.TaskResponse;
import com.taskvault.app.service.TaskService;

/** Controller das funcinalidades de gestão de tarefas */
@RestController
@RequestMapping("/task")
public class TaskController {

    /** Serviço de gestão de tarefas */
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

    /**
     * Endpoint de atualização de dados de tarefa
     * @param id ID da tarefa a ser atualizada
     * @param taskDto Novos dados da tarefa
     * @return Tarefa atualizada
     * @throws UserNotFoundException Usuário designado não encontrado
     * @throws TaskNotFoundException Tarefa não encontrada
     */
    @PutMapping("{taskId}")
    @ResponseStatus(code = HttpStatus.OK)
    public TaskResponse updateTask(
        @PathVariable("taskId") long id,
        @RequestBody UpdateTaskRequest taskDto
    ) throws UserNotFoundException, TaskNotFoundException {
        return TaskResponse.from(taskService.updateTask(id, taskDto));
    }

}
