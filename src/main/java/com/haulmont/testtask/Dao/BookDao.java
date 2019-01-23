package com.haulmont.testtask.Dao;

import com.haulmont.testtask.Entity.Book;
import com.haulmont.testtask.Entity.Genre;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BookDao extends AbstractDao<Book, Long> {
    private final String book_id = "book_id";
    private final String title = "title";
    private final String author_id = "author_id";
    private final String genre_id = "genre_id";
    private final String publisher = "publisher";
    private final String year = "year";
    private final String city = "city";
    private final String ALL_BOOK = "SELECT book_id, title, author_id, genre_id, publisher, year, city FROM BOOK";
    private final String GET_BOOK_ID = "SELECT book_id, title, author_id, genre_id, publisher, year, city FROM BOOK WHERE book_id = ?";
    private final String UPDATE_BOOK = "UPDATE BOOK SET title = ?, author_id = ?, genre_id = ?, publisher = ?, year = ?, city = ? WHERE book_id = ?";
    private final String DELETE_BOOK = "DELETE FROM BOOK WHERE book_id = ? ";
    private final String CHECK_EMPTY = "SELECT COUNT(BOOK_ID) AS \"COUNT\"FROM BOOK";
    private final String CREATE_BOOK = "INSERT INTO BOOK (title,author_id,genre_id,publisher,year,city) VALUES (?,?,?,?,?,?)";
    private final String DB_INIT = "src/main/hsqldb/db_init_book.sql";
    private final String DB_CONTENT = "src/main/hsqldb/db_content_book.sql";
    Connection connection;

    public BookDao() throws SQLException, IOException {
        connection = HsqlDAOFactory.createConnection();
        AuthorDao authorDao = new AuthorDao();
        GenreDao genreDao = new GenreDao();
        createDB();
        setContentDB();
    }

    private void setContentDB() throws IOException, SQLException {
        if (checkEmpty()) return;
        String sql = "";
        ArrayList<String> lists = new ArrayList<>();
        PreparedStatement statement = null;
        BufferedReader bufferedReader = new BufferedReader(new FileReader(DB_CONTENT));
        StringBuilder stringBuilder = new StringBuilder();
        String line;
        while ((line = bufferedReader.readLine()) != null) {
            lists.add(line);
        }

        for (String s : lists
        ) {
            statement = connection.prepareStatement(s);
            statement.execute();
        }


    }

    private void createDB() throws SQLException, IOException {
        String sql = "";
        ArrayList<String> lists = new ArrayList<>();
        PreparedStatement statement = null;
        BufferedReader bufferedReader = new BufferedReader(new FileReader(DB_INIT));
        StringBuilder stringBuilder = new StringBuilder();
        String line;
        while ((line = bufferedReader.readLine()) != null) {
            stringBuilder.append(line);
        }
        sql = stringBuilder.toString();
        statement = connection.prepareStatement(sql);
        statement.execute();


    }

    private boolean checkEmpty() throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(CHECK_EMPTY);
        ResultSet resultSet = preparedStatement.executeQuery();
        resultSet.next();
        int check = resultSet.getInt("COUNT");
        if (check > 0) return true;
        else return false;
    }

    @Override
    public List<Book> getAll() throws SQLException, IOException {
        List<Book> books = new ArrayList<>();
        PreparedStatement statement = connection.prepareStatement(ALL_BOOK);
        ResultSet resultSet = statement.executeQuery();
        AuthorDao authorDao = new AuthorDao();
        GenreDao genreDao = new GenreDao();
        while (resultSet.next()) {
            books.add(new Book(resultSet.getLong(book_id), resultSet.getString(title), authorDao.getEntityById(resultSet.getLong(author_id)),
                    genreDao.getEntityById(resultSet.getLong(genre_id)), resultSet.getString(publisher), resultSet.getString(year), resultSet.getString(city)));
        }

        return books;
    }

    @Override
    public Book getEntityById(Long id) throws SQLException, IOException {
        Book book = null;
        PreparedStatement statement = connection.prepareStatement(GET_BOOK_ID);
        statement.setLong(1, id);
        ResultSet resultSet = statement.executeQuery();
        AuthorDao authorDao = new AuthorDao();
        GenreDao genreDao = new GenreDao();
        if (resultSet.next())
            book = new Book(resultSet.getLong(book_id), resultSet.getString(title), authorDao.getEntityById(resultSet.getLong(author_id)),
                    genreDao.getEntityById(resultSet.getLong(genre_id)), resultSet.getString(publisher), resultSet.getString(year), resultSet.getString(city));

        return book;
    }

    @Override
    public boolean update(Book entity) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(UPDATE_BOOK);
        statement.setString(1, entity.getTitle());
        statement.setLong(2, entity.getAuthor().getId());
        statement.setLong(3, entity.getGenre().getId());
        statement.setString(4, entity.getPublisher());
        statement.setString(5, entity.getYear());
        statement.setString(6, entity.getCity());
        statement.setLong(7, entity.getId());
        boolean flag = statement.execute();

        return flag;
    }

    @Override
    public boolean delete(Long id) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(DELETE_BOOK);
        statement.setLong(1, id);
        boolean flag = statement.execute();

        return flag;
    }

    @Override
    public long create(Book entity) throws SQLException {
        long id = 0;
        PreparedStatement statement = connection.prepareStatement(CREATE_BOOK, PreparedStatement.RETURN_GENERATED_KEYS);
        statement.setString(1, entity.getTitle());
        statement.setLong(2, entity.getAuthor().getId());
        statement.setLong(3, entity.getGenre().getId());
        statement.setString(4, entity.getPublisher());
        statement.setString(5, entity.getYear());
        statement.setString(6, entity.getCity());
        int check = statement.executeUpdate();
        if (check == 1) {
            ResultSet resultSet = statement.getGeneratedKeys();
            resultSet.next();
            id = resultSet.getLong(1);
        } else throw new SQLException("Error data books");

        return id;
    }

    public int statGenre(String title) throws SQLException, IOException {
        List<Book> books = DAOFactory.getDAOFactory(DAOFactory.HSQLDB).getBookDao().getAll();
        int count = 0;
        for (Book book : books
        ) {
            if ((book.getGenre().getTitle().toLowerCase().trim()).equals(title.toLowerCase().trim()))
                count++;
        }
        return count;
    }


}
