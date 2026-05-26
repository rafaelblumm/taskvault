package com.taskvault.app.payload.response;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

import org.junit.jupiter.api.Test;

import com.taskvault.app.model.Task;
import com.taskvault.app.model.TaskStatus;
import com.taskvault.app.model.User;
import com.taskvault.app.model.UserRole;

/** Testes do DTO de resposta de tarefas */
public class TaskResponseTest {

    /** Data/hora atual para uso em testes */
    private static final LocalDateTime NOW_DATE_TIME = LocalDateTime.now();

    /**
     * Testa conversão de {@Code Task} para {@Code TaskResponse}
     * com todas as propriedades de tarefas
     */
    @Test
    public void fromTaskAllPropertiesTest() {
        var dueDate = NOW_DATE_TIME.toLocalDate().plusDays(3);
        var task = createSampleTask(dueDate);

        var expected = new TaskResponse(
            1,
            "Tarefa teste",
            Optional.of("Descrição curta."),
            TaskStatus.IN_PROGRESS,
            NOW_DATE_TIME,
            Optional.of(dueDate),
            "admin",
            Optional.of("johndoe")
        );
        assertEquals(expected, TaskResponse.from(task));

        task.setDeletedAt(null);
        assertEquals(expected, TaskResponse.from(task));
    }

    /**
     * Testa conversão de {@Code Task} para {@Code TaskResponse}
     * com algumas propriedades opcionais de tarefas
     */
    @Test
    public void fromTaskOptionalPropertiesTest() {
        var dueDate = NOW_DATE_TIME.toLocalDate().plusDays(3);
        var task = createSampleTask(dueDate);
        task.setDescription(null);
        task.setAssignee(null);

        var expected = new TaskResponse(
            1,
            "Tarefa teste",
            Optional.empty(),
            TaskStatus.IN_PROGRESS,
            NOW_DATE_TIME,
            Optional.of(dueDate),
            "admin",
            Optional.empty()
        );
        assertEquals(expected, TaskResponse.from(task));
    }

    /**
     * Testa conversão de {@Code Task} para {@Code TaskResponse}
     * somente com propriedades obrigatórias de tarefas
     */
    @Test
    public void fromTaskRequiredPropertiesTest() {
        var task = createSampleTask(null);
        task.setDescription(null);
        task.setDueDate(null);
        task.setAssignee(null);

        var expected = new TaskResponse(
            1,
            "Tarefa teste",
            Optional.empty(),
            TaskStatus.IN_PROGRESS,
            NOW_DATE_TIME,
            Optional.empty(),
            "admin",
            Optional.empty()
        );
        assertEquals(expected, TaskResponse.from(task));
    }

    /**
     * Cria tarefa padrão para testes
     * @param dueDate Data de conclusão
     * @return Tarefa padrão
     */
    private Task createSampleTask(LocalDate dueDate) {
        var creator = new User(
            "admin",
            "Administrator",
            "admin@dev.com",
            UserRole.ADMIN,
            "SuperDuperSecretPassword@123"
        );
        var assignee = new User(
            "johndoe",
            "John Doe",
            "johndoe@enterprise.com",
            UserRole.USER,
            "My!P4ssw0rd"
        );
        var deletedAt = NOW_DATE_TIME.plusDays(10);
        var task = new Task();
        task.setId(1);
        task.setTitle("Tarefa teste");
        task.setDescription("Descrição curta.");
        task.setStatus(TaskStatus.IN_PROGRESS);
        task.setCreationDatetime(NOW_DATE_TIME);
        task.setDueDate(dueDate);
        task.setCreator(creator);
        task.setAssignee(assignee);
        task.setDeletedAt(deletedAt);

        return task;
    }

}
