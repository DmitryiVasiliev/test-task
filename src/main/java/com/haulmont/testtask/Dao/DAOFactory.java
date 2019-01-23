package com.haulmont.testtask.Dao;

import com.haulmont.testtask.Entity.Author;
import com.haulmont.testtask.Entity.Book;
import com.haulmont.testtask.Entity.Genre;

import java.io.IOException;
import java.sql.SQLException;

public abstract class DAOFactory {
    public static final int HSQLDB = 1;

    public abstract AbstractDao<Author, Long> getAuthorDao() throws SQLException, IOException;

    public abstract AbstractDao<Book, Long> getBookDao() throws SQLException, IOException;

    public abstract AbstractDao<Genre, Long> getGenreDao() throws SQLException, IOException;

    public static DAOFactory getDAOFactory(int whichFactory) {
        switch (whichFactory) {
            case HSQLDB:
                return new HsqlDAOFactory();
            default:
                return null;
        }
    }
}
