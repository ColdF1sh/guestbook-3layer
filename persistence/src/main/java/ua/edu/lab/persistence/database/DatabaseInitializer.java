package ua.edu.lab.persistence.database;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseInitializer {
    public static void initialize(DataSource dataSource) {
        try (Connection conn = dataSource.getConnection();
             Statement stmt = conn.createStatement()) {
            
            // Create books table
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS books (
                    id BIGINT AUTO_INCREMENT PRIMARY KEY,
                    title VARCHAR(255) NOT NULL,
                    author VARCHAR(255) NOT NULL
                )
                """);
            
            // Create comments table
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS comments (
                    id BIGINT AUTO_INCREMENT PRIMARY KEY,
                    book_id BIGINT NOT NULL,
                    author VARCHAR(100) NOT NULL,
                    text VARCHAR(1000) NOT NULL,
                    FOREIGN KEY (book_id) REFERENCES books(id)
                )
                """);
            
            // Insert sample data if tables are empty
            if (isEmpty(stmt, "books")) {
                stmt.execute("INSERT INTO books (title, author) VALUES ('Spring in Action', 'Craig Walls')");
                stmt.execute("INSERT INTO books (title, author) VALUES ('Effective Java', 'Joshua Bloch')");
                stmt.execute("INSERT INTO books (title, author) VALUES ('Clean Code', 'Robert Martin')");
            }
            
        } catch (SQLException e) {
            throw new RuntimeException("Error initializing database", e);
        }
    }
    
    private static boolean isEmpty(Statement stmt, String table) throws SQLException {
        try (var rs = stmt.executeQuery("SELECT COUNT(*) FROM " + table)) {
            if (rs.next()) {
                return rs.getInt(1) == 0;
            }
        }
        return true;
    }
}



