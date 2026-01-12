package com.example.springbootapp.web.service;

import com.example.springbootapp.core.model.Book;
import com.example.springbootapp.core.service.BookService;
import com.example.springbootapp.persistence.repo.BookRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;
    private final MailService mailService;

    public BookServiceImpl(BookRepository bookRepository, MailService mailService) {
        this.bookRepository = bookRepository;
        this.mailService = mailService;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Book> findAllBooks() {
        return bookRepository.findAll(Sort.by(Sort.Direction.DESC, "createdAt"));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Book> findBookWithComments(Long id) {
        return bookRepository.findWithCommentsById(id);
    }

    @Override
    public Book createBook(String title, String author, int year) {
        Book book = new Book();
        book.setTitle(title);
        book.setAuthor(author);
        book.setYear(year);

        Book saved = bookRepository.save(book);
        mailService.sendNewBookNotification(saved);
        return saved;
    }

    @Override
    public void deleteBook(Long id) {
        bookRepository.deleteById(id);
    }
}
