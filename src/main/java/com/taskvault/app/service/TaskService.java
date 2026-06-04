package com.taskvault.app.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import com.taskvault.app.model.TaskStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.taskvault.app.error.InvalidDataException;
import com.taskvault.app.error.MissingAuthTokenException;
import com.taskvault.app.error.TaskNotFoundException;
import com.taskvault.app.error.UnauthorizedException;
import com.taskvault.app.error.UserNotFoundException;
import com.taskvault.app.model.Task;
import com.taskvault.app.model.User;
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

    /** Acesso a camada de persistência de comentários */
    @Autowired
    private CommentRepository commentRepository;

    /** Serviço de gerenciamento de usuários */
    @Autowired
    private UserService userService;

    /** Serviço de autenticação de usuários */
    @Autowired
    private AuthService authService;

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
    protected Task createTask(CreateTaskRequest taskDto, User creator) throws UserNotFoundException {
        taskDto.dueDate().ifPresent(dueDate -> {
            if (dueDate.isBefore(LocalDate.now()))
                throw new InvalidDataException();
        });

        return taskRepository.save(new Task(taskDto, creator, getUserFromDto(taskDto)));
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
        taskDto.dueDate().ifPresent(dueDate -> {
            if (dueDate.isBefore(task.getCreationDatetime().toLocalDate()))
                throw new InvalidDataException();
        });

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
        if (!authService.getCurrentUser().orElseThrow(UserNotFoundException::new).isElevated())
            throw new UnauthorizedException();

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
     * Busca todas tarefas que satisfazem aos filtros
     * @param id ID do usuário
     * @param status Estado atual da tarefa
     * @param dueBefore Data limite da procura
     * @return todas tarefas encontrada
     * @throws TaskNotFoundException Se a tarefa não existir
     */
    public List<Task> getTasksWithFilter(
            String id,
            TaskStatus status,
            LocalDate dueBefore
    ) throws TaskNotFoundException {
        if (status == null && dueBefore == null)
            return taskRepository.findAllByAssigneeIdOrderByCreationDatetimeAsc(id);

        return taskRepository.findAllByAssigneeIdAndStatusAndDueDateBeforeOrderByCreationDatetimeAsc(
                id,
                status,
                dueBefore
        );
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
