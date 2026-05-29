package com.taskvault.app.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.taskvault.app.model.Comment;

/** Acesso a persistência de comentários */
public interface CommentRepository extends CrudRepository<Comment, Long> {

    /**
     * Busca todos os comentários vinculados a uma tarefa ordenados por data de criação crescente
     * @param taskId ID da tarefa
     * @return Lista de comentários
     */
    public List<Comment> findAllByTaskIdOrderByCreationDatetimeAsc(long taskId);

}
