package com.example.springbootapp.core.service;

import com.example.springbootapp.core.model.Comment;

public interface CommentService {
    Comment addComment(Long bookId, String authorEmail, String text);
}
