package com.taskvault.app.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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

import com.taskvault.app.model.Task;
import com.taskvault.app.model.TaskStatus;
import com.taskvault.app.model.User;
import com.taskvault.app.model.UserRole;
import com.taskvault.app.payload.request.CreateTaskRequest;
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
        task.setDueDate(null);
        task.setCreator(creator);
        task.setAssignee(assignee);
        task.setDeletedAt(null);

        when(taskRepository.save(any(Task.class)))
            .thenReturn(task);
        when(userRepository.findById(assignee.getId()))
            .thenReturn(Optional.of(assignee));

        var createdTask = taskService.createTask(taskDto, creator);
        createdTask.setDueDate(null);

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

}
