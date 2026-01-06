package ua.edu.lab.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ua.edu.lab.core.domain.Book;
import ua.edu.lab.core.service.CatalogService;

@Controller
@RequestMapping("/books")
public class BookMvcController {

    private final CatalogService catalogService;

    public BookMvcController(CatalogService catalogService) {
        this.catalogService = catalogService;
    }

    @GetMapping
    public String listBooks(Model model) {
        model.addAttribute("books", catalogService.listBooks(null));
        return "books";
    }

    @GetMapping("/add")
    public String showAddBookForm(Model model) {
        model.addAttribute("book", new Book());
        return "book-form";
    }

    @PostMapping("/add")
    public String addBook(@ModelAttribute Book book) {
        catalogService.addBook(book.getTitle(), book.getAuthor());
        return "redirect:/books";
    }
}





