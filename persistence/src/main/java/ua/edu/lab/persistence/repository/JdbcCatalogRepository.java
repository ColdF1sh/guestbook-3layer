package ua.edu.lab.persistence.repository;

import ua.edu.lab.core.domain.Book;
import ua.edu.lab.core.port.CatalogRepositoryPort;
import ua.edu.lab.persistence.database.DataSourceFactory;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class JdbcCatalogRepository implements CatalogRepositoryPort {
    private final DataSource dataSource;

    public JdbcCatalogRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public List<Book> listBooks(String query) {
        List<Book> books = new ArrayList<>();
        String sql = "SELECT id, title, author FROM books";
        
        if (query != null && !query.trim().isEmpty()) {
            sql += " WHERE title LIKE ? OR author LIKE ?";
        }
        sql += " ORDER BY id";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            if (query != null && !query.trim().isEmpty()) {
                String searchPattern = "%" + query.trim() + "%";
                stmt.setString(1, searchPattern);
                stmt.setString(2, searchPattern);
            }

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Book book = new Book();
                    book.setId(rs.getLong("id"));
                    book.setTitle(rs.getString("title"));
                    book.setAuthor(rs.getString("author"));
                    books.add(book);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error listing books", e);
        }

        return books;
    }

    @Override
    public Optional<Book> getBookById(long id) {
        String sql = "SELECT id, title, author FROM books WHERE id = ?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Book book = new Book();
                    book.setId(rs.getLong("id"));
                    book.setTitle(rs.getString("title"));
                    book.setAuthor(rs.getString("author"));
                    return Optional.of(book);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error getting book by id: " + id, e);
        }

        return Optional.empty();
    }
}


