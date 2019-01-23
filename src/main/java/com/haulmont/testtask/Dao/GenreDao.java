package com.haulmont.testtask.Dao;

import com.haulmont.testtask.Entity.Genre;
import com.vaadin.ui.Notification;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class GenreDao extends AbstractDao<Genre, Long> {
    private final String genre_id = "genre_id";
    private final String title = "title";
    private final String ALL_GENRE = "SELECT genre_id, title FROM GENRE";
    private final String GET_GENRE_ID = "SELECT genre_id, title FROM GENRE WHERE genre_id = ?";
    private final String UPDATE_GENRE = "UPDATE GENRE SET title = ? WHERE genre_id = ?";
    private final String DELETE_GENRE = "DELETE FROM GENRE WHERE genre_id = ? ";
    private final String CREATE_GENRE = "INSERT INTO GENRE (title) VALUES (?);";
    private final String CHECK_EMPTY = "SELECT COUNT(GENRE_ID) AS \"COUNT\"FROM GENRE";
    private final String DB_INIT = "src/main/hsqldb/db_init_genre.sql";
    private final String DB_CONTENT = "src/main/hsqldb/db_content_genre.sql";
    Connection connection;

    public GenreDao() throws SQLException, IOException {
        connection = HsqlDAOFactory.createConnection();
        createDB();
        setContentDB();
    }

    private void setContentDB() throws IOException, SQLException {
        if (checkEmpty()) return;
        String sql = "";
        ArrayList<String> lists = new ArrayList<>();
        PreparedStatement statement = null;
        BufferedReader bufferedReader = new BufferedReader(new FileReader(DB_CONTENT));
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
    public List<Genre> getAll() throws SQLException {
        List<Genre> genres = new ArrayList<>();
        PreparedStatement statement = connection.prepareStatement(ALL_GENRE);
        ResultSet resultSet = statement.executeQuery();
        while (resultSet.next()) {
            genres.add(new Genre(resultSet.getLong(genre_id), resultSet.getString(title)));
        }

        return genres;
    }

    @Override
    public Genre getEntityById(Long id) throws SQLException {
        Genre genre = null;
        PreparedStatement statement = connection.prepareStatement(GET_GENRE_ID);
        statement.setLong(1, id);
        ResultSet resultSet = statement.executeQuery();
        if (resultSet.next())
            genre = new Genre(resultSet.getLong(genre_id), resultSet.getString(title));

        return genre;
    }

    @Override
    public boolean update(Genre entity) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(UPDATE_GENRE);
        statement.setString(1, entity.getTitle());
        statement.setLong(2, entity.getId());
        boolean flag = statement.execute();

        return flag;
    }

    @Override
    public boolean delete(Long id) throws SQLException {
        boolean flag = true;
        try {
            PreparedStatement statement = connection.prepareStatement(DELETE_GENRE);
            statement.setLong(1, id);
            statement.execute();
            connection.commit();
        } catch (SQLIntegrityConstraintViolationException ex) {
            Notification.show("Нельзя удалить жанр по которому есть книга");
            flag = false;
        }
        return flag;
    }

    @Override
    public long create(Genre entity) throws SQLException {
        long id = 0;
        PreparedStatement statement = connection.prepareStatement(CREATE_GENRE, PreparedStatement.RETURN_GENERATED_KEYS);
        statement.setString(1, entity.getTitle());
        int check = statement.executeUpdate();
        if (check == 1) {
            ResultSet resultSet = statement.getGeneratedKeys();
            resultSet.next();
            id = resultSet.getLong(1);
        } else throw new SQLException("No data");


        return id;
    }


}
