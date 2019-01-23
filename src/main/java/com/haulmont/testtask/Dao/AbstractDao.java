package com.haulmont.testtask.Dao;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public abstract class AbstractDao<E, K> {
    public abstract List<E> getAll() throws SQLException, IOException;

    public abstract E getEntityById(K id) throws SQLException, IOException;

    public abstract boolean update(E entity) throws SQLException;

    public abstract boolean delete(K id) throws SQLException;

    public abstract long create(E entity) throws SQLException;

    public int statGenre(Long id) throws SQLException, IOException {
        return 0;
    }

    ;


}
