package com.taskvault.app.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.taskvault.app.error.CommentNotFoundException;
import com.taskvault.app.error.MissingAuthTokenException;
import com.taskvault.app.error.TaskNotFoundException;
import com.taskvault.app.error.UnauthorizedException;
import com.taskvault.app.model.Comment;
import com.taskvault.app.model.Task;
import com.taskvault.app.model.User;
import com.taskvault.app.payload.request.CommentRequest;
import com.taskvault.app.repository.CommentRepository;

/** Serviço de gerenciamento de comentários em tarefas */
@Service
public class CommentService {

    /** Acesso a camada de dados de comentários */
    @Autowired
    private CommentRepository commentRepository;

    /** Serviço de gerenciamento de tarefas */
    @Autowired
    private TaskService taskService;

    /** Serviço de autenticação de usuários */
    @Autowired
    private AuthService authService;

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
        User creator = authService.getCurrentUser()
            .map(auth -> auth.getUser())
            .orElseThrow(MissingAuthTokenException::new);

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
     * @param commentId ID do comentário
     * @throws CommentNotFoundException não encontrar comentário
     */
    public void deleteComment(long commentId) throws CommentNotFoundException {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(CommentNotFoundException::new);

        if (!authService.canUpdateResource(comment.getCreator()))
            throw new UnauthorizedException();

        comment.setDeletedAt(LocalDateTime.now());
        commentRepository.save(comment);
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
