package com.example.springbootapp.web.controller;

import com.example.springbootapp.core.model.Book;
import com.example.springbootapp.core.service.BookService;
import com.example.springbootapp.core.service.CommentService;
import com.example.springbootapp.web.dto.BookForm;
import com.example.springbootapp.web.dto.CommentForm;
import jakarta.validation.Valid;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.server.ResponseStatusException;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@Controller
public class BookController {

    private final BookService bookService;
    private final CommentService commentService;

    public BookController(BookService bookService, CommentService commentService) {
        this.bookService = bookService;
        this.commentService = commentService;
    }

    @GetMapping("/books")
    public String list(Model model) {
        model.addAttribute("books", bookService.findAllBooks());
        return "books/list";
    }

    @GetMapping("/books/{id}")
    public String details(@PathVariable("id") Long id, Model model) {
        Book book = bookService.findBookWithComments(id)
            .orElseThrow(() -> new ResponseStatusException(NOT_FOUND));
        model.addAttribute("book", book);
        model.addAttribute("commentForm", new CommentForm());
        return "books/detail";
    }

    @GetMapping("/admin/books/new")
    public String newBookForm(Model model) {
        model.addAttribute("bookForm", new BookForm());
        return "books/new";
    }

    @PostMapping("/admin/books")
    public String newBookSubmit(@Valid @ModelAttribute("bookForm") BookForm form,
                                BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "books/new";
        }

        Book book = bookService.createBook(form.getTitle(), form.getAuthor(), form.getYear());
        return "redirect:/books/" + book.getId();
    }

    @PostMapping("/admin/books/{id}/delete")
    public String deleteBook(@PathVariable("id") Long id) {
        bookService.findBookWithComments(id)
            .orElseThrow(() -> new ResponseStatusException(NOT_FOUND));
        bookService.deleteBook(id);
        return "redirect:/books";
    }

    @PostMapping("/books/{id}/comments")
    public String addComment(@PathVariable("id") Long id,
                             @AuthenticationPrincipal UserDetails userDetails,
                             @Valid @ModelAttribute("commentForm") CommentForm form,
                             BindingResult bindingResult,
                             Model model) {
        Book book = bookService.findBookWithComments(id)
            .orElseThrow(() -> new ResponseStatusException(NOT_FOUND));

        if (bindingResult.hasErrors()) {
            model.addAttribute("book", book);
            return "books/detail";
        }

        commentService.addComment(id, userDetails.getUsername(), form.getText());
        return "redirect:/books/" + id;
    }
}
