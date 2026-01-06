package ua.edu.lab.web.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ua.edu.lab.core.domain.Book;
import ua.edu.lab.core.service.CatalogService;
import ua.edu.lab.web.service.MailService;

@Controller
@RequestMapping("/books")
public class BookMvcController {

    private static final Logger logger = LoggerFactory.getLogger(BookMvcController.class);

    private final CatalogService catalogService;
    private final MailService mailService;

    public BookMvcController(CatalogService catalogService, MailService mailService) {
        this.catalogService = catalogService;
        this.mailService = mailService;
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
        Book savedBook = catalogService.addBook(book.getTitle(), book.getAuthor());
        
        // Send email notification (non-blocking - if it fails, book is still added)
        try {
            mailService.sendNewBookEmail(savedBook);
        } catch (Exception e) {
            logger.warn("Failed to send email notification for book: " + savedBook.getTitle(), e);
        }
        
        return "redirect:/books";
    }
}






