package com.example.springbootapp.core.service;

import com.example.springbootapp.core.model.Book;

import java.util.List;
import java.util.Optional;

public interface BookService {
    List<Book> findAllBooks();

    Optional<Book> findBookWithComments(Long id);

    Book createBook(String title, String author, int year);

    void deleteBook(Long id);
}
