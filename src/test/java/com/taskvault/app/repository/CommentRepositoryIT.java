package com.taskvault.app.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import com.taskvault.app.model.Comment;
import com.taskvault.app.model.Task;
import com.taskvault.app.model.TaskStatus;
import com.taskvault.app.model.User;
import com.taskvault.app.model.UserRole;

/** Testes de integração da camada de persistência de comentários de tarefas */
@DataJpaTest
public class CommentRepositoryIT {

    /** Data/hora atual para uso em testes */
    private static final LocalDateTime NOW_DATE_TIME = LocalDateTime.now();

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    public void saveCommentTest() {
        var creator = new User(
            "admin",
            "Administrator",
            "admin@dev.com",
            UserRole.ADMIN,
            "SuperDuperSecretPassword@123"
        );
        creator = userRepository.save(creator);

        var commenter = new User(
            "johndoe",
            "John Doe",
            "johndoe@enterprise.com",
            UserRole.USER,
            "My!P4ssw0rd"
        );
        commenter = userRepository.save(commenter);

        var task = new Task("Tarefa teste", creator, TaskStatus.IN_PROGRESS);
        task.setCreationDatetime(NOW_DATE_TIME.minusDays(10));
        task = taskRepository.save(task);

        var comment = new Comment(task, commenter, "Comentário em tarefa teste!");
        comment.setCreationDatetime(NOW_DATE_TIME);
        comment = commentRepository.save(comment);

        Optional<Comment> result = commentRepository.findById(comment.getId());
        var expected = new Comment(task, commenter, "Comentário em tarefa teste!");
        expected.setId(comment.getId());
        expected.setCreationDatetime(NOW_DATE_TIME);

        assertTrue(result.isPresent());
        assertEquals(expected, result.get());
    }

}
