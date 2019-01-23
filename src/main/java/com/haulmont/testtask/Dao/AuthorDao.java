package com.haulmont.testtask.Dao;

import com.haulmont.testtask.Entity.Author;
import com.vaadin.ui.Notification;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AuthorDao extends AbstractDao<Author, Long> {
    private final String author_id = "author_id";
    private final String name = "name";
    private final String surname = "surname";
    private final String middlename = "middlename";
    private final String ALL_AUTHOR = "SELECT author_id, name, middlename,surname FROM AUTHOR";
    private final String GET_AUTHOR_ID = "SELECT author_id, name, middlename, surname FROM AUTHOR WHERE author_id = ?";
    private final String UPDATE_AUTHOR = "UPDATE AUTHOR SET name = ?, middlename = ?, surname = ? WHERE author_id = ?";
    private final String DELETE_AUTHOR = "DELETE FROM AUTHOR WHERE author_id = ? ";
    private final String CREATE_AUTHOR = "INSERT INTO AUTHOR (name, middlename ,surname) VALUES (?, ?, ?)";
    private final String CHECK_EMPTY = "SELECT COUNT(AUTHOR_ID) AS \"COUNT\"FROM AUTHOR";
    private final String DB_INIT = "src/main/hsqldb/db_init_author.sql";
    private final String DB_CONTENT = "src/main/hsqldb/db_content_author.sql";
    Connection connection;

    public AuthorDao() throws SQLException, IOException {
        connection = HsqlDAOFactory.createConnection();
        createDB();
        setContentDB();
    }

    public void setContentDB() throws IOException, SQLException {
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
        try {


            for (String s : lists
            ) {

                statement = connection.prepareStatement(s);
                statement.execute();
            }
        } catch (SQLIntegrityConstraintViolationException e) {
            throw e;
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
    public List<Author> getAll() throws SQLException {
        List<Author> authors = new ArrayList<>();
        PreparedStatement statement = connection.prepareStatement(ALL_AUTHOR);
        ResultSet resultSet = statement.executeQuery();
        while (resultSet.next()) {
            authors.add(new Author(resultSet.getLong(author_id), resultSet.getString(name), resultSet.getString(surname), resultSet.getString(middlename)));
        }

        return authors;
    }

    @Override
    public Author getEntityById(Long id) throws SQLException {
        Author author = null;
        PreparedStatement statement = connection.prepareStatement(GET_AUTHOR_ID);
        statement.setLong(1, id);
        ResultSet resultSet = statement.executeQuery();
        if (resultSet.next())
            author = new Author(resultSet.getLong(author_id), resultSet.getString(name), resultSet.getString(surname), resultSet.getString(middlename));

        return author;
    }

    @Override
    public boolean update(Author entity) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(UPDATE_AUTHOR);
        statement.setString(1, entity.getName());
        statement.setString(2, entity.getMiddlename());
        statement.setString(3, entity.getSurname());
        statement.setLong(4, entity.getId());
        boolean flag = statement.execute();

        return flag;
    }

    @Override
    public boolean delete(Long id) throws SQLException {
        boolean flag = true;
        try {
            PreparedStatement statement = connection.prepareStatement(DELETE_AUTHOR);
            statement.setLong(1, id);
            statement.execute();
            connection.commit();

        } catch (SQLIntegrityConstraintViolationException e) {
            Notification.show("Нельзя удалить автора у которого есть книги");
            flag = false;
        }
        return flag;
    }

    @Override
    public long create(Author entity) throws SQLException {
        long id = 0;
        PreparedStatement statement = connection.prepareStatement(CREATE_AUTHOR, PreparedStatement.RETURN_GENERATED_KEYS);
        statement.setString(1, entity.getName());
        statement.setString(2, entity.getMiddlename());
        statement.setString(3, entity.getSurname());
        int check = statement.executeUpdate();
        if (check == 1) {
            ResultSet resultSet = statement.getGeneratedKeys();
            resultSet.next();
            id = resultSet.getLong(1);
        } else throw new SQLException("Error data authors");

        return id;
    }
}
