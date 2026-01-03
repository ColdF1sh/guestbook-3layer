package ua.edu.lab.core.service;

import ua.edu.lab.core.domain.Book;
import ua.edu.lab.core.exception.NotFoundException;
import ua.edu.lab.core.exception.ValidationException;
import ua.edu.lab.core.port.CatalogRepositoryPort;
import ua.edu.lab.core.port.CommentRepositoryPort;

import java.util.List;
import java.util.Optional;

public class CatalogService {
    private final CatalogRepositoryPort catalogRepository;
    private final CommentRepositoryPort commentRepository;

    public CatalogService(CatalogRepositoryPort catalogRepository, CommentRepositoryPort commentRepository) {
        this.catalogRepository = catalogRepository;
        this.commentRepository = commentRepository;
    }

    public List<Book> listBooks(String query) {
        validateQuery(query);
        return catalogRepository.listBooks(query);
    }

    public Book getBookById(long id) {
        validateId(id);
        Optional<Book> book = catalogRepository.getBookById(id);
        if (book.isEmpty()) {
            throw new NotFoundException("Book with id " + id + " not found");
        }
        
        Book bookWithComments = book.get();
        List<ua.edu.lab.core.domain.Comment> comments = commentRepository.listCommentsByBookId(id);
        bookWithComments.setComments(comments);
        return bookWithComments;
    }

    private void validateId(long id) {
        if (id <= 0) {
            throw new ValidationException("Id must be greater than 0");
        }
    }

    private void validateQuery(String query) {
        // Query can be null or empty, no validation needed
    }
}


