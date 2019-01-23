package com.haulmont.testtask.Dao;

import com.haulmont.testtask.Entity.Author;
import com.haulmont.testtask.Entity.Book;
import com.haulmont.testtask.Entity.Genre;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class HsqlDAOFactory extends DAOFactory {


    public static Connection createConnection() throws SQLException {
        ResourceBundle resourceBundle = ResourceBundle.getBundle("database");
        String url = resourceBundle.getString("db.url");
        String username = resourceBundle.getString("db.username");
        String password = resourceBundle.getString("db.password");
        Connection connection = DriverManager.getConnection(url, username, password);

        return connection;
    }


    @Override
    public AbstractDao<Author, Long> getAuthorDao() throws SQLException, IOException {
        return new AuthorDao();
    }

    @Override
    public AbstractDao<Book, Long> getBookDao() throws SQLException, IOException {
        return new BookDao();
    }

    @Override
    public AbstractDao<Genre, Long> getGenreDao() throws SQLException, IOException {
        return new GenreDao();
    }
}
