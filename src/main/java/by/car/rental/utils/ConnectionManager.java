package by.car.rental.utils;

import lombok.experimental.UtilityClass;

import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

@UtilityClass
public final class ConnectionManager {

    private static final String USERNAME_KEY = "db.username";
    private static final String PASSWORD_KEY = "db.password";
    private static final String URL_KEY = "db.url";
    private static final String POOL_SIZE_KEY = "db.pool.size";
    private static final String DRIVER_KEY = "db.driver";
    private static BlockingQueue<Connection> connectionPool;
    private static List<Connection> connections;
    private static final int DEFAULT_POOL_SIZE = 10;

    static {
        loadDriver();
        initConnectionPool();
    }

    private static void loadDriver() {
        try {
            Class.forName(PropertiesUtil.get(DRIVER_KEY));
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private static void initConnectionPool() {
        String size = PropertiesUtil.get(POOL_SIZE_KEY);
        int poolSize = size != null ? Integer.parseInt(size) : DEFAULT_POOL_SIZE;
        connections = new ArrayList<>(poolSize);
        connectionPool = new ArrayBlockingQueue<>(poolSize);
        for (int i = 0; i < poolSize; i++) {
            Connection connection = open();
            Connection proxyConnection = (Connection)
                    Proxy.newProxyInstance(ConnectionManager.class.getClassLoader(),
                            new Class[]{Connection.class},
                            (proxy, method, args) -> method.getName().equals("close")
                                    ? connectionPool.add((Connection) proxy)
                                    : method.invoke(connection, args));
            connections.add(connection);
            connectionPool.add(proxyConnection);

        }
    }

    private static Connection open() {
        try {
            return DriverManager.getConnection(
                    PropertiesUtil.get(URL_KEY),
                    PropertiesUtil.get(USERNAME_KEY),
                    PropertiesUtil.get(PASSWORD_KEY));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static Connection get() {
        try {
            return connectionPool.take();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public static void closePool() {
        for (Connection connection : connections) {
            try {
                connection.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

}
