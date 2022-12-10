package by.car.rental.dao;

import by.car.rental.domain.Order;
import by.car.rental.domain.OrderStatus;
import by.car.rental.utils.ConnectionManager;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static java.sql.Statement.RETURN_GENERATED_KEYS;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class OrderDao implements Dao<Long, Order> {
    private static final OrderDao INSTANCE = new OrderDao();
    private static final String DELETE_SQL =
            "DELETE FROM orders " +
                    "WHERE id = ? ";
    private static final String SAVE_SQL =
            "INSERT INTO orders(user_id, car_id, begin_time, end_time, status, message) " +
                    "VALUES (?, ?, ?, ?, ?, ?)";
    private static final String UPDATE_SQL =
            "UPDATE orders " +
                    "SET user_id = ?, car_id = ?, begin_time = ?, end_time = ?, status = ?, message = ? " +
                    "WHERE id = ? ";
    private static final String FIND_ALL_SQL =
            "SELECT id, user_id, car_id, begin_time, end_time, status, message " +
                    "FROM orders ";
    private static final String FIND_ALL_BY_ID_SQL =
            FIND_ALL_SQL +
                    " WHERE id = ? ";


    @Override
    @SneakyThrows
    public Order save(Order order) {
        try (Connection connection = ConnectionManager.get();
             PreparedStatement preparedStatement = connection.prepareStatement(SAVE_SQL, RETURN_GENERATED_KEYS)) {
            preparedStatement.setLong(1, order.getUserId());
            preparedStatement.setLong(2, order.getCarId());
            preparedStatement.setTimestamp(3, Timestamp.valueOf(order.getBeginTime()));
            preparedStatement.setTimestamp(4, Timestamp.valueOf(order.getEndTime()));
            preparedStatement.setString(5, order.getOrderStatus().name());
            preparedStatement.setString(6, order.getMessage());

            preparedStatement.executeUpdate();
            ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
            if (generatedKeys.next()) {
                order.setId(generatedKeys.getLong("id"));
            }
            return order;
        }
    }

    @Override
    @SneakyThrows
    public void update(Order order) {
        try (Connection connection = ConnectionManager.get();
             PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_SQL)) {
            preparedStatement.setLong(1, order.getUserId());
            preparedStatement.setLong(2, order.getCarId());
            preparedStatement.setTimestamp(3, Timestamp.valueOf(order.getBeginTime()));
            preparedStatement.setTimestamp(4, Timestamp.valueOf(order.getEndTime()));
            preparedStatement.setString(5, order.getOrderStatus().name());
            preparedStatement.setString(6, order.getMessage());
            preparedStatement.setLong(7, order.getId());

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
    public List<Order> findAll() {
        try (Connection connection = ConnectionManager.get();
             PreparedStatement preparedStatement = connection.prepareStatement(FIND_ALL_SQL)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            List<Order> orders = new ArrayList<>();
            while (resultSet.next()) {
                orders.add(Order.builder()
                        .id(resultSet.getObject("id", Long.class))
                        .userId(resultSet.getObject("user_id", Long.class))
                        .carId(resultSet.getObject("car_id", Long.class))
                        .beginTime(resultSet.getTimestamp("begin_time").toLocalDateTime())
                        .endTime(resultSet.getTimestamp("end_time").toLocalDateTime())
                        .orderStatus(OrderStatus.valueOf(resultSet.getObject("status", String.class)))
                        .message(resultSet.getString("message"))
                        .build());
            }
            return orders;
        }
    }

    @Override
    @SneakyThrows
    public Optional<Order> findById(Long id) {
        try (Connection connection = ConnectionManager.get();
             PreparedStatement preparedStatement = connection.prepareStatement(FIND_ALL_BY_ID_SQL)) {
            preparedStatement.setLong(1, id);

            ResultSet resultSet = preparedStatement.executeQuery();
            Order order = resultSet.next()
                    ? Order.builder()
                    .id(resultSet.getObject("id", Long.class))
                    .userId(resultSet.getObject("user_id", Long.class))
                    .carId(resultSet.getObject("car_id", Long.class))
                    .beginTime(resultSet.getTimestamp("begin_time").toLocalDateTime())
                    .endTime(resultSet.getTimestamp("end_time").toLocalDateTime())
                    .orderStatus(OrderStatus.valueOf(resultSet.getObject("status", String.class)))
                    .message(resultSet.getString("message"))
                    .build()
                    : null;

            return Optional.ofNullable(order);
        }
    }

    public static OrderDao getInstance() {
        return INSTANCE;
    }
}
