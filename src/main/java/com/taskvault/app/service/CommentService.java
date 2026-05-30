package com.taskvault.app.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.taskvault.app.error.CommentNotFoundException;
import com.taskvault.app.error.MissingAuthTokenException;
import com.taskvault.app.error.TaskNotFoundException;
import com.taskvault.app.model.Comment;
import com.taskvault.app.model.Task;
import com.taskvault.app.model.User;
import com.taskvault.app.payload.request.CommentRequest;
import com.taskvault.app.repository.CommentRepository;
import com.taskvault.app.security.SecurityUtils;

/** Serviço de gerenciamento de comentários em tarefas */
@Service
public class CommentService {

    /** Acesso a camada de dados de comentários */
    @Autowired
    private CommentRepository commentRepository;

    /** Serviço de gerenciamento de usuários */
    @Autowired
    private UserService userService;

    /** Serviço de gerenciamento de tarefas */
    @Autowired
    private TaskService taskService;

    /**
     * Cria nova tarefa
     * @param commentDto Dados da tarefa
     * @param taskId ID da tarefa
     * @return Tarefa criada
     * @throws MissingAuthTokenException
     * @throws TaskNotFoundException Se não encontrar tarefa
     */
    public Comment createComment(CommentRequest commentDto, long taskId)
    throws MissingAuthTokenException, TaskNotFoundException {
        Authentication auth = SecurityUtils.getAuthenticatedUser()
            .orElseThrow(MissingAuthTokenException::new);
        User creator = userService.getUser(auth.getName());
        Task task = taskService.getTask(taskId);
        var comment = new Comment(task, creator, commentDto.message());

        return commentRepository.save(comment);
    }

    /**
     * Lista comentários de uma tarefa
     * @param taskId ID da tarefa
     * @return Lista de comentários
     * @throws TaskNotFoundException Se não encontrar tarefa
     */
    public List<Comment> listComments(long taskId) throws TaskNotFoundException {
        if (!taskService.taskExists(taskId)) throw new TaskNotFoundException();

        return commentRepository.findAllByTaskIdOrderByCreationDatetimeAsc(taskId);
    }

    /**
     * Deleta comentário de uma tarefa
     * @param commentId ID da tarefa
     * @throws CommentNotFoundException não encontrar tarefa
     */
    public void deleteComment(long commentId) throws CommentNotFoundException {
        if (!commentExists(commentId)) throw new CommentNotFoundException();

        Comment comment = commentRepository.findById(commentId).orElseThrow(CommentNotFoundException::new);
        comment.setDeletedAt(LocalDateTime.now());
        commentRepository.save(comment);
    }

    /**
     * Deleta comentário de uma tarefa
     * @param taskId ID da tarefa
     * @throws TaskNotFoundException não encontrar tarefa
     */
    public void deleteAllComments(long taskId) throws TaskNotFoundException {
        if (!taskService.taskExists(taskId)) throw new TaskNotFoundException();

        List<Comment> comments = listComments(taskId);
        int counter = comments.size();

        for (int i = 0; i < counter; i++) {
            Comment comment = comments.removeFirst();
            deleteComment(comment.getId());
        }
    }


    /**
     * Verifica se o comentário existe
     * @param id ID do comentário
     * @return Se o comentário existe
     */
    public boolean commentExists(long id) {
        return commentRepository.existsById(id);
    }

}
