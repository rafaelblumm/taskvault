package com.taskvault.app.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import com.taskvault.app.error.TaskNotFoundException;
import com.taskvault.app.model.Task;
import com.taskvault.app.model.TaskStatus;
import com.taskvault.app.model.User;
import com.taskvault.app.model.UserRole;
import com.taskvault.app.payload.request.CreateTaskRequest;
import com.taskvault.app.payload.request.UpdateTaskRequest;
import com.taskvault.app.repository.TaskRepository;
import com.taskvault.app.repository.UserRepository;

/** Testes da classe de gestão de tarefas */
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private TaskService taskService;

    @Mock
    private UserService userService;

    private static final List<User> SAMPLE_USERS = List.of(
        new User(
            "johndoe",
            "John Doe",
            "johndoe@enterprise.com",
            UserRole.USER,
            "My!P4ssw0rd"
        ),
        new User(
            "tester",
            "QA Tester",
            "tester@qa.dev",
            UserRole.USER,
            "myP4s$"
        )
    );

    /** Teste de criação de nova tarefa com usuário designado */
    @Test
    public void createTaskWithAssigneeTest() {
        var creator = SAMPLE_USERS.get(0);
        var assignee = SAMPLE_USERS.get(1);
        var taskDto = new CreateTaskRequest(
            "Tarefa teste",
            Optional.empty(),
            Optional.empty(),
            Optional.of(assignee.getId())
        );
        var task = new Task();
        task.setId(1);
        task.setTitle("Tarefa teste");
        task.setDescription(null);
        task.setStatus(TaskStatus.IN_PROGRESS);
        task.setCreationDatetime(LocalDateTime.now());
        task.setDueDate(null);
        task.setCreator(creator);
        task.setAssignee(assignee);
        task.setDeletedAt(null);

        when(taskRepository.save(any(Task.class)))
            .thenReturn(task);
        when(userRepository.findById(assignee.getId()))
            .thenReturn(Optional.of(assignee));
        var createdTask = taskService.createTask(taskDto, creator);

        verify(taskRepository).save(ArgumentMatchers.any());

        assertEquals(task, createdTask);
    }

    /** Teste de criação de nova tarefa sem usuário designado */
    @Test
    public void createTaskUnassignedTest() {
        var creator = SAMPLE_USERS.get(0);
        var taskDto = new CreateTaskRequest(
            "Tarefa teste",
            Optional.empty(),
            Optional.empty(),
            Optional.empty()
        );
        var task = new Task();
        task.setId(1);
        task.setTitle("Tarefa teste");
        task.setDescription(null);
        task.setStatus(TaskStatus.IN_PROGRESS);
        task.setCreationDatetime(LocalDateTime.now());
        task.setDueDate(null);
        task.setCreator(creator);
        task.setDeletedAt(null);

        when(taskRepository.save(any(Task.class)))
            .thenReturn(task);

        var createdTask = taskService.createTask(taskDto, creator);
        createdTask.setDueDate(null);

        verify(taskRepository).save(ArgumentMatchers.any());

        assertEquals(task, createdTask);
    }

    /** Teste de atualização de tarefa não existente */
    @Test
    public void updateTaskInexistentTest() {
        long taskId = 1;
        when(taskRepository.findById(taskId)).thenReturn(Optional.empty());
        var taskDto = new UpdateTaskRequest(
            "Novo título",
            Optional.of("Descrição"),
            TaskStatus.DONE,
            Optional.empty(),
            Optional.empty()
        );
        assertThrows(
            TaskNotFoundException.class,
            () -> taskService.updateTask(taskId, taskDto)
        );

        verify(taskRepository).findById(taskId);
    }

    /** Teste de atualização de tarefa informando todos os dados */
    @Test
    public void updateTaskAllPropertiesTest() {
        var task = new Task();
        task.setId(1);
        task.setTitle("Tarefa teste");
        task.setDescription("Descrição original");
        task.setCreationDatetime(LocalDateTime.now());
        task.setCreator(SAMPLE_USERS.get(0));
        task.setStatus(TaskStatus.IN_PROGRESS);
        task.setCreationDatetime(LocalDateTime.now());
        task.setDueDate(null);
        task.setDeletedAt(null);
        var assignee = SAMPLE_USERS.get(1);
        var taskDto = new UpdateTaskRequest(
            "Novo título",
            Optional.of("Nova descrição"),
            TaskStatus.DONE,
            Optional.of(LocalDate.now()),
            Optional.of(assignee.getId())
        );

        testUpdate(task, taskDto, Optional.of(assignee));
    }

    /** Teste de atualização de tarefa informando alguns dados */
    @Test
    public void updateTaskSomePropertiesTest() {
        var task = new Task();
        task.setId(1);
        task.setTitle("Tarefa teste");
        task.setDescription("Descrição original");
        task.setCreationDatetime(LocalDateTime.now());
        task.setCreator(SAMPLE_USERS.get(0));
        task.setStatus(TaskStatus.IN_PROGRESS);
        task.setCreationDatetime(LocalDateTime.now());
        task.setDueDate(null);
        task.setDeletedAt(null);
        var assignee = SAMPLE_USERS.get(1);
        var taskDto = new UpdateTaskRequest(
            "Novo título",
            Optional.empty(),
            TaskStatus.DONE,
            Optional.empty(),
            Optional.of(assignee.getId())
        );

        testUpdate(task, taskDto, Optional.of(assignee));
    }

    /**
     * Método centralizado para testar atualização de dados de tarefas
     * @param existingTask Tarefa a ser atualizada
     * @param taskDto Dados a serem atualizados
     * @param assignee Usuário designado, se houver
     */
    private void testUpdate(
        Task existingTask,
        UpdateTaskRequest taskDto,
        Optional<User> assignee
    ) {
        var expected = new Task();
        expected.setId(existingTask.getId());
        expected.setTitle(taskDto.title());
        expected.setDescription(taskDto.description().orElse(null));
        expected.setCreationDatetime(existingTask.getCreationDatetime());
        expected.setCreator(existingTask.getCreator());
        expected.setAssignee(assignee.orElse(null));
        expected.setStatus(taskDto.status());
        expected.setDueDate(taskDto.dueDate().orElse(null));
        expected.setDeletedAt(existingTask.getDeletedAt().orElse(null));

        when(taskRepository.findById(existingTask.getId()))
            .thenReturn(Optional.of(existingTask));
        when(taskRepository.save(ArgumentMatchers.any()))
            .thenReturn(expected);
        if (assignee.isPresent())
            when(userRepository.findById(assignee.get().getId())).thenReturn(assignee);

        Task updatedTask = taskService.updateTask(existingTask.getId(), taskDto);

        verify(taskRepository).findById(existingTask.getId());
        verify(taskRepository).save(ArgumentMatchers.any());
        assertEquals(expected, updatedTask);
    }

}
