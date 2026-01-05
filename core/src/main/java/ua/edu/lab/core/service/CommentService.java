package ua.edu.lab.core.service;

import ua.edu.lab.core.domain.Comment;
import ua.edu.lab.core.exception.ValidationException;
import ua.edu.lab.core.port.CatalogRepositoryPort;
import ua.edu.lab.core.port.CommentRepositoryPort;

public class CommentService {
    private static final int MAX_AUTHOR_LENGTH = 100;
    private static final int MAX_TEXT_LENGTH = 1000;

    private final CommentRepositoryPort commentRepository;
    private final CatalogRepositoryPort catalogRepository;

    public CommentService(CommentRepositoryPort commentRepository, CatalogRepositoryPort catalogRepository) {
        this.commentRepository = commentRepository;
        this.catalogRepository = catalogRepository;
    }

    public Comment addComment(long bookId, String author, String text) {
        validateId(bookId);
        validateAuthor(author);
        validateText(text);
        
        // Verify book exists
        if (catalogRepository.getBookById(bookId).isEmpty()) {
            throw new ValidationException("Book with id " + bookId + " does not exist");
        }

        return commentRepository.addComment(bookId, author, text);
    }

    private void validateId(long id) {
        if (id <= 0) {
            throw new ValidationException("Book id must be greater than 0");
        }
    }

    private void validateAuthor(String author) {
        if (author == null || author.trim().isEmpty()) {
            throw new ValidationException("Comment author is required");
        }
        if (author.length() > MAX_AUTHOR_LENGTH) {
            throw new ValidationException("Comment author must not exceed " + MAX_AUTHOR_LENGTH + " characters");
        }
    }

    private void validateText(String text) {
        if (text == null || text.trim().isEmpty()) {
            throw new ValidationException("Comment text is required");
        }
        if (text.length() > MAX_TEXT_LENGTH) {
            throw new ValidationException("Comment text must not exceed " + MAX_TEXT_LENGTH + " characters");
        }
    }
}



