package me.redstom.revival.api.repository;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class RepositoryManager {

    private static final RepositoryManager instance = new RepositoryManager();
    private Map<Class<? extends Repository<?>>, Repository<?>> repositories = new HashMap<>();

    private RepositoryManager() {
    }

    public void register(Repository<?> instance) {
        Class<? extends Repository<?>> clazz = (Class<? extends Repository<?>>) instance.getClass();

        System.out.println("Registered " + clazz + " as " + instance);

        repositories.put(clazz, instance);
    }

    public <T, U extends Repository<T>> U get(Class<U> clazz) {
        U u = (U) repositories.get(clazz);
        System.out.println("Found " + u + " for " + clazz);
        return u;
    }

    public Repository<?>[] repositories() {
        return repositories.values().toArray(Repository[]::new);
    }

    public void hydrateAll(Connection connection) {
        for (Repository<?> repo : repositories()) {
            repo.hydrate(connection);
        }
    }

    public void createAll() throws SQLException {
        for (Repository<?> repo : repositories.values()) {
            repo.create();
        }
    }

    public static RepositoryManager getInstance() {
        return instance;
    }
}
