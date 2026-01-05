package ua.edu.lab.persistence.repository;

import ua.edu.lab.core.domain.Comment;
import ua.edu.lab.core.port.CommentRepositoryPort;
import ua.edu.lab.persistence.database.DataSourceFactory;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class JdbcCommentRepository implements CommentRepositoryPort {
    private final DataSource dataSource;

    public JdbcCommentRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public List<Comment> listCommentsByBookId(long bookId) {
        List<Comment> comments = new ArrayList<>();
        String sql = "SELECT id, book_id, author, text FROM comments WHERE book_id = ? ORDER BY id";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, bookId);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Comment comment = new Comment();
                    comment.setId(rs.getLong("id"));
                    comment.setBookId(rs.getLong("book_id"));
                    comment.setAuthor(rs.getString("author"));
                    comment.setText(rs.getString("text"));
                    comments.add(comment);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error listing comments for book id: " + bookId, e);
        }

        return comments;
    }

    @Override
    public Comment addComment(long bookId, String author, String text) {
        String sql = "INSERT INTO comments (book_id, author, text) VALUES (?, ?, ?)";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setLong(1, bookId);
            stmt.setString(2, author);
            stmt.setString(3, text);

            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new RuntimeException("Failed to insert comment");
            }

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    long id = generatedKeys.getLong(1);
                    Comment comment = new Comment();
                    comment.setId(id);
                    comment.setBookId(bookId);
                    comment.setAuthor(author);
                    comment.setText(text);
                    return comment;
                } else {
                    throw new RuntimeException("Failed to get generated comment id");
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error adding comment", e);
        }
    }
}



