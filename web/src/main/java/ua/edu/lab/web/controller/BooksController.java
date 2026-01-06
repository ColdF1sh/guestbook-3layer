package ua.edu.lab.web.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ua.edu.lab.core.domain.Book;
import ua.edu.lab.core.service.CatalogService;

import java.util.List;

@RestController
@RequestMapping("/api/books")
public class BooksController {

    private final CatalogService catalogService;

    public BooksController(CatalogService catalogService) {
        this.catalogService = catalogService;
    }

    @GetMapping
    public List<Book> getBooks(
            @RequestParam(name = "q", required = false) String q,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size,
            @RequestParam(name = "sort", defaultValue = "title") String sort) {
        // Map 'q' parameter to query for CatalogService
        // Note: page, size, sort are accepted but not yet implemented in service layer
        return catalogService.listBooks(q);
    }

    @GetMapping("/{id}")
    public Book getBookById(@PathVariable(name = "id") long id) {
        return catalogService.getBookById(id);
    }
}

