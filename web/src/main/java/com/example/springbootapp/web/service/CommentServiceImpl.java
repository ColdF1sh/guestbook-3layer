package com.example.springbootapp.web.service;

import com.example.springbootapp.core.model.Book;
import com.example.springbootapp.core.model.Comment;
import com.example.springbootapp.core.service.CommentService;
import com.example.springbootapp.persistence.repo.BookRepository;
import com.example.springbootapp.persistence.repo.CommentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final BookRepository bookRepository;

    public CommentServiceImpl(CommentRepository commentRepository, BookRepository bookRepository) {
        this.commentRepository = commentRepository;
        this.bookRepository = bookRepository;
    }

    @Override
    public Comment addComment(Long bookId, String authorEmail, String text) {
        Book book = bookRepository.findById(bookId)
            .orElseThrow(() -> new IllegalArgumentException("Book not found"));

        Comment comment = new Comment();
        comment.setBook(book);
        comment.setAuthorEmail(authorEmail);
        comment.setText(text);

        return commentRepository.save(comment);
    }
}
