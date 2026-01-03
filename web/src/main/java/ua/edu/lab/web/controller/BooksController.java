package ua.edu.lab.web.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Clock;
import java.time.Instant;
import java.util.Map;

@RestController
public class BooksController {

    private final Clock clock;

    public BooksController(Clock clock) {
        this.clock = clock;
    }

    @GetMapping("/books")
    public Map<String, Object> getBooks() {
        return Map.of(
            "message", "Books endpoint - Constructor Injection Demo",
            "timestamp", Instant.now(clock).toString(),
            "injectionType", "Constructor Injection",
            "books", java.util.List.of(
                Map.of("id", 1, "title", "Spring in Action"),
                Map.of("id", 2, "title", "Effective Java")
            )
        );
    }
}

