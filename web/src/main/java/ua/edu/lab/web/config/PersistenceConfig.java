package ua.edu.lab.web.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ua.edu.lab.core.port.CatalogRepositoryPort;
import ua.edu.lab.core.port.CommentRepositoryPort;
import ua.edu.lab.core.service.CatalogService;
import ua.edu.lab.core.service.CommentService;
import ua.edu.lab.persistence.database.DataSourceFactory;
import ua.edu.lab.persistence.database.DatabaseInitializer;
import ua.edu.lab.persistence.repository.JdbcCatalogRepository;
import ua.edu.lab.persistence.repository.JdbcCommentRepository;

import javax.sql.DataSource;

@Configuration
public class PersistenceConfig {

    @Value("${app.h2.url:jdbc:h2:file:./data/guest;AUTO_SERVER=TRUE}")
    private String h2Url;

    @Bean
    public DataSource dataSource() {
        DataSource ds = DataSourceFactory.createDataSource(h2Url);
        // Initialize database on startup
        DatabaseInitializer.initialize(ds);
        return ds;
    }

    @Bean
    public CatalogRepositoryPort catalogRepository(DataSource dataSource) {
        return new JdbcCatalogRepository(dataSource);
    }

    @Bean
    public CommentRepositoryPort commentRepository(DataSource dataSource) {
        return new JdbcCommentRepository(dataSource);
    }

    @Bean
    public CatalogService catalogService(CatalogRepositoryPort catalogRepository, CommentRepositoryPort commentRepository) {
        return new CatalogService(catalogRepository, commentRepository);
    }

    @Bean
    public CommentService commentService(CommentRepositoryPort commentRepository, CatalogRepositoryPort catalogRepository) {
        return new CommentService(commentRepository, catalogRepository);
    }
}

