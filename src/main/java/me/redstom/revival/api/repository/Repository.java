package me.redstom.revival.api.repository;

import java.sql.Connection;
import java.sql.SQLException;

public interface Repository<T> {

    void hydrate(Connection connection);

    void create() throws SQLException;
    void drop() throws SQLException;

    void insert(T object) throws SQLException;
    void update(T object) throws SQLException;
    void delete(T object) throws SQLException;
}
