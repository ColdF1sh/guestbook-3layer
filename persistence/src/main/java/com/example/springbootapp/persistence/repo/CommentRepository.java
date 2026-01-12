package com.example.springbootapp.persistence.repo;

import com.example.springbootapp.core.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}
