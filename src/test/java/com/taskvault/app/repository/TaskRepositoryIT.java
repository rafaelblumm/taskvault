package com.taskvault.app.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import com.taskvault.app.model.Task;
import com.taskvault.app.model.TaskStatus;
import com.taskvault.app.model.User;
import com.taskvault.app.model.UserRole;

/** Teste de integração da camada de persistência de tarefas */
@DataJpaTest
public class TaskRepositoryIT {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UserRepository userRepository;

    /** Teste de criação e atualização de registro de tarefa */
    @Test
    public void saveTaskTest() {
        var creator = new User(
            "johndoe",
            "John Doe",
            "johndoe@enterprise.com",
            UserRole.USER,
            "My!P4ssw0rd"
        );
        var assignee = new User(
            "tester",
            "QA Tester",
            "tester@qa.dev",
            UserRole.USER,
            "myP4s$"
        );
        for (var user : new User[] {creator, assignee})
            userRepository.save(user);

        var now = LocalDateTime.now();
        var task = new Task();
        task.setTitle("Tarefa teste");
        task.setStatus(TaskStatus.PENDING);
        task.setCreationDatetime(now);
        task.setCreator(creator);
        task.setAssignee(assignee);
        long taskId = taskRepository.save(task).getId();
        Optional<Task> result = taskRepository.findById(taskId);

        var expected = new Task();
        expected.setId(taskId);
        expected.setTitle("Tarefa teste");
        expected.setDescription(null);
        expected.setStatus(TaskStatus.PENDING);
        expected.setCreationDatetime(now);
        expected.setDueDate(null);
        expected.setCreator(creator);
        expected.setAssignee(assignee);
        expected.setDeletedAt(null);
        assertTrue(result.isPresent());
        assertEquals(expected, result.get());
    }

}
