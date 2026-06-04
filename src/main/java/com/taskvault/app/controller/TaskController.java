package com.taskvault.app.controller;

import com.taskvault.app.model.TaskStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestParam;

import com.taskvault.app.error.MissingAuthTokenException;
import com.taskvault.app.error.TaskNotFoundException;
import com.taskvault.app.error.UserNotFoundException;
import com.taskvault.app.payload.request.CreateTaskRequest;
import com.taskvault.app.payload.request.UpdateTaskRequest;
import com.taskvault.app.payload.response.TaskResponse;
import com.taskvault.app.service.TaskService;

import java.time.LocalDate;
import java.util.List;

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
    @PreAuthorize("hasRole('USER')")
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
    @PreAuthorize("hasRole('USER')")
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
    @PreAuthorize("hasRole('GUEST')")
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
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void deleteTask(@PathVariable long taskId) throws TaskNotFoundException {
        taskService.deleteTask(taskId);
    }

    /**
     * Procura tarefas com filtros avançados
     * @param id ID do usuário
     * @param status Estado atual da tarefa
     * @param dueBefore Data limite da procura
     * @throws TaskNotFoundException Se nenhuma tarefa existir
     */
    @GetMapping
    @PreAuthorize("hasRole('GUEST')")
    @ResponseStatus(code = HttpStatus.OK)
    public List<TaskResponse> getFilteredTasks(
            @RequestParam("assignedTo") String id,
            @RequestParam(value = "status", required = false) TaskStatus status,
            @RequestParam(value = "dueBefore", required = false) LocalDate dueBefore
    ) throws TaskNotFoundException {
        return taskService.getTasksWithFilter(id, status, dueBefore)
                .stream()
                .map(TaskResponse::from)
                .toList();
    }
}
