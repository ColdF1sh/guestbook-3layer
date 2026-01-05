package ua.edu.lab.persistence.database;

import org.h2.jdbcx.JdbcDataSource;

import javax.sql.DataSource;

public class DataSourceFactory {
    public static DataSource createDataSource(String url) {
        JdbcDataSource dataSource = new JdbcDataSource();
        dataSource.setURL(url);
        return dataSource;
    }
}



