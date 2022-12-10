package by.car.rental.dao;

import by.car.rental.domain.User;
import by.car.rental.domain.UsersRole;
import by.car.rental.utils.ConnectionManager;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static java.sql.Statement.RETURN_GENERATED_KEYS;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserDao implements Dao<Long, User> {

    private static final UserDao INSTANCE = new UserDao();
    private static final String DELETE_SQL =
            "DELETE FROM users " +
                    "WHERE id = ? ";
    private static final String SAVE_SQL =
            "INSERT INTO users(first_name, last_name, email, password, role) " +
                    "VALUES (?, ?, ?, ?, ?)";
    private static final String UPDATE_SQL =
            "UPDATE users " +
                    "SET first_name = ?, last_name = ?, email = ?, password = ?, role = ? " +
                    "WHERE id = ? ";
    private static final String FIND_ALL_SQL =
            "SELECT id, first_name, last_name, email, password, role " +
                    "FROM users ";
    private static final String FIND_ALL_BY_ID_SQL =
            FIND_ALL_SQL +
                    " WHERE id = ? ";
    private static final String FIND_ALL_BY_EMAIL_AND_PASSWORD_SQL =
            "SELECT id, first_name, last_name, email, password, role " +
                    "FROM users " +
                    "WHERE email = ? " +
                    "AND password = ? ";

    @Override
    @SneakyThrows
    public User save(User user) {
        try (Connection connection = ConnectionManager.get();
             PreparedStatement preparedStatement = connection.prepareStatement(SAVE_SQL, RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, user.getFirstName());
            preparedStatement.setString(2, user.getLastName());
            preparedStatement.setString(3, user.getEmail());
            preparedStatement.setString(4, user.getPassword());
            preparedStatement.setString(5, user.getRole().name());

            preparedStatement.executeUpdate();
            ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
            if (generatedKeys.next()) {
                user.setId(generatedKeys.getLong("id"));
            }
            return user;
        }
    }

    @Override
    @SneakyThrows
    public void update(User user) {
        try (Connection connection = ConnectionManager.get();
             PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_SQL)) {
            preparedStatement.setString(1, user.getFirstName());
            preparedStatement.setString(2, user.getLastName());
            preparedStatement.setString(3, user.getEmail());
            preparedStatement.setString(4, user.getPassword());
            preparedStatement.setString(5, user.getRole().name());
            preparedStatement.setLong(6, user.getId());

            preparedStatement.executeUpdate();
        }
    }

    @Override
    @SneakyThrows
    public boolean delete(Long id) {
        try (Connection connection = ConnectionManager.get();
             PreparedStatement preparedStatement = connection.prepareStatement(DELETE_SQL)) {
            preparedStatement.setLong(1, id);
            return preparedStatement.executeUpdate() > 0;
        }
    }

    @Override
    @SneakyThrows
    public List<User> findAll() {
        try (Connection connection = ConnectionManager.get();
             PreparedStatement preparedStatement = connection.prepareStatement(FIND_ALL_SQL)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            List<User> users = new ArrayList<>();
            while (resultSet.next()) {
                users.add(User.builder()
                        .id(resultSet.getObject("id", Long.class))
                        .firstName(resultSet.getObject("first_name", String.class))
                        .lastName(resultSet.getObject("last_name", String.class))
                        .email(resultSet.getObject("email", String.class))
                        .password(resultSet.getObject("password", String.class))
                        .role(UsersRole.valueOf(resultSet.getObject("role", String.class)))
                        .build()
                );
            }
            return users;
        }
    }

    @Override
    @SneakyThrows
    public Optional<User> findById(Long id) {
        try (Connection connection = ConnectionManager.get();
             PreparedStatement preparedStatement = connection.prepareStatement(FIND_ALL_BY_ID_SQL)) {
            preparedStatement.setLong(1, id);

            ResultSet resultSet = preparedStatement.executeQuery();
            User user = resultSet.next()
                    ? User.builder()
                    .id(resultSet.getObject("id", Long.class))
                    .firstName(resultSet.getObject("first_name", String.class))
                    .lastName(resultSet.getObject("last_name", String.class))
                    .email(resultSet.getObject("email", String.class))
                    .password(resultSet.getObject("password", String.class))
                    .role(UsersRole.valueOf(resultSet.getObject("role", String.class)))
                    .build()
                    : null;

            return Optional.ofNullable(user);
        }
    }

    @SneakyThrows
    public Optional<User> findByEmailAndPassword(String email, String password) {
        try (Connection connection = ConnectionManager.get();
             PreparedStatement preparedStatement = connection.prepareStatement(FIND_ALL_BY_EMAIL_AND_PASSWORD_SQL)) {
            preparedStatement.setString(1, email);
            preparedStatement.setString(2, password);

            ResultSet resultSet = preparedStatement.executeQuery();
            User user = resultSet.next()
                    ? User.builder()
                    .id(resultSet.getObject("id", Long.class))
                    .firstName(resultSet.getObject("first_name", String.class))
                    .lastName(resultSet.getObject("last_name", String.class))
                    .email(resultSet.getObject("email", String.class))
                    .password(resultSet.getObject("password", String.class))
                    .role(UsersRole.valueOf(resultSet.getObject("role", String.class)))
                    .build()
                    : null;

            return Optional.ofNullable(user);
        }
    }

    public static UserDao getInstance() {
        return INSTANCE;
    }
}
