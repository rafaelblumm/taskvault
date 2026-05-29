package com.taskvault.app.repository;

import org.springframework.data.repository.CrudRepository;

import com.taskvault.app.model.Comment;

/** Acesso a persistência de comentários */
public interface CommentRepository extends CrudRepository<Comment, Long> {}
