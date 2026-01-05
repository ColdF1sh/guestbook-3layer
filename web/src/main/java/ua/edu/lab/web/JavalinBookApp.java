package ua.edu.lab.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.javalin.Javalin;
import io.javalin.json.JavalinJackson;
import ua.edu.lab.core.domain.Book;
import ua.edu.lab.core.domain.Comment;
import ua.edu.lab.core.exception.NotFoundException;
import ua.edu.lab.core.exception.ValidationException;
import ua.edu.lab.core.port.CatalogRepositoryPort;
import ua.edu.lab.core.port.CommentRepositoryPort;
import ua.edu.lab.core.service.CatalogService;
import ua.edu.lab.core.service.CommentService;
import ua.edu.lab.persistence.database.DataSourceFactory;
import ua.edu.lab.persistence.database.DatabaseInitializer;
import ua.edu.lab.persistence.repository.JdbcCatalogRepository;
import ua.edu.lab.persistence.repository.JdbcCommentRepository;
import ua.edu.lab.web.dto.CreateCommentRequest;
import ua.edu.lab.web.util.ErrorResponse;

import javax.sql.DataSource;
import java.util.List;

public class JavalinBookApp {

    private static final String H2_URL = System.getProperty("app.h2.url", "jdbc:h2:file:./data/guest;AUTO_SERVER=TRUE");
    private static final int PORT = Integer.parseInt(System.getProperty("server.port", "8080"));

    private final CatalogService catalogService;
    private final CommentService commentService;
    private final ObjectMapper objectMapper;

    public JavalinBookApp(CatalogService catalogService, CommentService commentService) {
        this.catalogService = catalogService;
        this.commentService = commentService;
        this.objectMapper = new ObjectMapper();
    }

    public static void main(String[] args) {
        // Wire dependencies manually (no Spring)
        DataSource dataSource = DataSourceFactory.createDataSource(H2_URL);
        DatabaseInitializer.initialize(dataSource);

        CatalogRepositoryPort catalogRepository = new JdbcCatalogRepository(dataSource);
        CommentRepositoryPort commentRepository = new JdbcCommentRepository(dataSource);

        CatalogService catalogService = new CatalogService(catalogRepository, commentRepository);
        CommentService commentService = new CommentService(commentRepository, catalogRepository);

        JavalinBookApp app = new JavalinBookApp(catalogService, commentService);
        app.start();
    }

    public void start() {
        Javalin app = Javalin.create(config -> {
            config.jsonMapper(new JavalinJackson(objectMapper));
        });

        // Request logging middleware
        app.before(ctx -> {
            System.out.println("Request: " + ctx.method() + " " + ctx.path());
        });

        // Exception handlers
        app.exception(ValidationException.class, (e, ctx) -> {
            ErrorResponse error = new ErrorResponse(400, "Bad Request", e.getMessage());
            ctx.status(400);
            ctx.contentType("application/json; charset=utf-8");
            ctx.json(error);
        });

        app.exception(NotFoundException.class, (e, ctx) -> {
            ErrorResponse error = new ErrorResponse(404, "Not Found", e.getMessage());
            ctx.status(404);
            ctx.contentType("application/json; charset=utf-8");
            ctx.json(error);
        });

        app.exception(IllegalArgumentException.class, (e, ctx) -> {
            ErrorResponse error = new ErrorResponse(400, "Bad Request", e.getMessage());
            ctx.status(400);
            ctx.contentType("application/json; charset=utf-8");
            ctx.json(error);
        });

        app.exception(Exception.class, (e, ctx) -> {
            ErrorResponse error = new ErrorResponse(500, "Internal Server Error", 
                e.getMessage() != null ? e.getMessage() : "An unexpected error occurred");
            ctx.status(500);
            ctx.contentType("application/json; charset=utf-8");
            ctx.json(error);
        });

        // Routes
        app.get("/books", ctx -> {
            String q = ctx.queryParam("q");
            String pageParam = ctx.queryParam("page");
            String sizeParam = ctx.queryParam("size");
            String sort = ctx.queryParam("sort");

            // Parse and validate query parameters
            int page = pageParam != null ? Integer.parseInt(pageParam) : 0;
            int size = sizeParam != null ? Integer.parseInt(sizeParam) : 10;
            if (sort == null || sort.isEmpty()) {
                sort = "title";
            }

            List<Book> books = catalogService.listBooks(q);
            ctx.contentType("application/json; charset=utf-8");
            ctx.json(books);
        });

        app.get("/books/{id}", ctx -> {
            long id = Long.parseLong(ctx.pathParam("id"));
            Book book = catalogService.getBookById(id);
            ctx.contentType("application/json; charset=utf-8");
            ctx.json(book);
        });

        app.post("/comments", ctx -> {
            CreateCommentRequest request = objectMapper.readValue(ctx.body(), CreateCommentRequest.class);
            Comment comment = commentService.addComment(
                request.getBookId(),
                request.getAuthor(),
                request.getText()
            );
            ctx.status(204);
        });

        app.start(PORT);
        System.out.println("Javalin app started on http://localhost:" + PORT);
    }
}

