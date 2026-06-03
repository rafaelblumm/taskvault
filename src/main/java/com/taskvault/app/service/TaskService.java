package com.taskvault.app.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.taskvault.app.error.UserRoleNotPermitted;
import com.taskvault.app.error.MissingAuthTokenException;
import com.taskvault.app.error.TaskNotFoundException;
import com.taskvault.app.error.UserNotFoundException;
import com.taskvault.app.model.Task;
import com.taskvault.app.model.User;
import com.taskvault.app.model.UserRole;
import com.taskvault.app.payload.request.CreateTaskRequest;
import com.taskvault.app.payload.request.UpdateTaskRequest;
import com.taskvault.app.repository.TaskRepository;
import com.taskvault.app.repository.CommentRepository;
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

    /** Acesso a camada de persistência de comentários */
    @Autowired
    private CommentRepository commentRepository;

    /**
     * Cria novo registro de tarefa com o usuário corrente como criador
     * @param taskDto Dados da tarefa
     * @return Tarefa criada
     * @throws MissingAuthTokenException Se não for informado usuário
     * @throws UserNotFoundException Se IDs de usuários não existirem
     */
    public Task createTask(CreateTaskRequest taskDto)
    throws MissingAuthTokenException, UserNotFoundException {
        Authentication auth = SecurityUtils.getAuthenticatedUser().orElseThrow(MissingAuthTokenException::new);
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
        var task = new Task(taskDto, creator, getUserFromDto(taskDto));

        return taskRepository.save(task);
    }

    /**
     * Atualiza dados de uma tarefa
     * @param taskId ID da tarefa a ser atualizada
     * @param taskDto Dados da tarefa
     * @return Tarefa atualizada
     */
    public Task updateTask(long taskId, UpdateTaskRequest taskDto)
    throws TaskNotFoundException, UserNotFoundException {
        Task task = taskRepository.findById(taskId).orElseThrow(TaskNotFoundException::new);

        task.setTitle(taskDto.title());
        task.setDescription(taskDto.description().orElse(null));
        task.setStatus(taskDto.status());
        task.setDueDate(taskDto.dueDate().orElse(null));
        if (taskDto.assignee().isPresent())
            getUserFromDto(taskDto).ifPresent(task::setAssignee);
        else
            task.setAssignee(null);

        return taskRepository.save(task);
    }

    /**
     * Deleta tarefa
     * @param taskId ID da tarefa a ser deletada
     */
    @Transactional
    public void deleteTask(long taskId) throws TaskNotFoundException {
        Authentication auth = SecurityUtils.getAuthenticatedUser().orElseThrow(MissingAuthTokenException::new);
        User requester = userService.getUser(auth.getName());


        if (!(requester.getRole().equals(UserRole.SYSADMIN) || requester.getRole().equals(UserRole.ADMIN)))
            throw new UserRoleNotPermitted();


        var deletionTime = LocalDateTime.now();
        Task task = taskRepository.findById(taskId).orElseThrow(TaskNotFoundException::new);
        task.setDeletedAt(deletionTime);

        commentRepository.updateDeletedAtByTaskId(deletionTime, task.getId());
        taskRepository.save(task);
    }

    /**
     * Busca tarefa a partir do ID informado
     * @param id Id da tarefa
     * @return tarefa encontrada
     */
    public Task getTask(long id) {
        return taskRepository.findById(id).orElseThrow(TaskNotFoundException::new);
    }

    /**
     * Busca todas tarefas a partir do ID de usuário
     * @param id Id do usuário
     * @return todas tarefas encontrada
     * @throws UserNotFoundException Se IDs de usuários não existirem
     */
    public List<Task> getAllTasksFromUser(String id) throws UserNotFoundException {
        return taskRepository.findAllByAssigneeIdOrderByCreationDatetimeAsc(id);
    }

    /**
     * Verifica se tarefa existe
     * @param id ID da tarefa
     * @return Se tarefa existe
     */
    public boolean taskExists(long id) {
        return taskRepository.existsById(id);
    }

    /**
     * Busca {@Code User} a partir do ID informado no DTO
     * @param taskDto Dados da tarefa
     * @return Usuário encontrado, se informado
     */
    private Optional<User> getUserFromDto(CreateTaskRequest taskDto)
    throws UserNotFoundException {
        return taskDto.assignee().map(userService::getUser);
    }

    /**
     * Busca {@Code User} a partir do ID informado no DTO
     * @param taskDto Dados da tarefa
     * @return Usuário encontrado, se informado
     */
    private Optional<User> getUserFromDto(UpdateTaskRequest taskDto)
    throws UserNotFoundException {
        return taskDto.assignee().map(userService::getUser);
    }

}
