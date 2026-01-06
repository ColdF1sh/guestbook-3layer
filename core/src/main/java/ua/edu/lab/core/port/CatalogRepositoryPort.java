package ua.edu.lab.core.port;

import ua.edu.lab.core.domain.Book;

import java.util.List;
import java.util.Optional;

public interface CatalogRepositoryPort {
    List<Book> listBooks(String query);
    Optional<Book> getBookById(long id);
    Book saveBook(Book book);
}


