package com.taskvault.app.controller;

import com.taskvault.app.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

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

    /** Serviço de gestão de comentários */
    @Autowired
    private CommentService commentService;

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

    /**
     * Endpoint de procura de tarefa
     * @param taskId ID da tarefa
     * @return Tarefa salva no banco de dados
     * @throws TaskNotFoundException Tarefa não encontrada
     */
    @GetMapping("{taskId}")
    @ResponseStatus(code = HttpStatus.OK)
    public TaskResponse getTask(@PathVariable long taskId) throws TaskNotFoundException {
        return TaskResponse.from(taskService.getTask(taskId));
    }

    /**
     * Deleta tarefa
     * @param taskId Id da tarefa a ser deletado
     * @throws TaskNotFoundException Usuário não encontrado
     */
    @DeleteMapping("{taskId}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void deleteTask(@PathVariable long taskId) throws TaskNotFoundException {
        commentService.deleteAllComments(taskId);
        taskService.deleteTask(taskId);
    }
}
