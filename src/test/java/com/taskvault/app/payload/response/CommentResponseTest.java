package com.taskvault.app.payload.response;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

import com.taskvault.app.model.Comment;
import com.taskvault.app.model.Task;
import com.taskvault.app.model.TaskStatus;
import com.taskvault.app.model.User;
import com.taskvault.app.model.UserRole;

/** Testes da classe de resposta de requisições de comentários */
public class CommentResponseTest {

    /** Data/hora atual para uso em testes */
    private static final LocalDateTime NOW_DATE_TIME = LocalDateTime.now();

    /** Testa conversão de {@Code CommentResponse} a partir de {@Code Comment} */
    @Test
    public void fromCommentTest() {
        var creator = new User(
            "admin",
            "Administrator",
            "admin@dev.com",
            UserRole.ADMIN,
            "SuperDuperSecretPassword@123"
        );
        var task = new Task("Tarefa teste", creator, TaskStatus.IN_PROGRESS);
        task.setId(1);
        task.setCreationDatetime(NOW_DATE_TIME.minusDays(10));
        var commenter = new User(
            "johndoe",
            "John Doe",
            "johndoe@enterprise.com",
            UserRole.USER,
            "My!P4ssw0rd"
        );
        var comment = new Comment(task, commenter, "Comentário em tarefa teste!");
        comment.setId(1);
        comment.setCreationDatetime(NOW_DATE_TIME);

        var expected = new CommentResponse(
            1,
            1,
            "johndoe",
            "Comentário em tarefa teste!",
            NOW_DATE_TIME
        );
        assertEquals(expected, CommentResponse.from(comment));
    }

}
