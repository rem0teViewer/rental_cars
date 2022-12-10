package by.car.rental.dao;

import by.car.rental.domain.RentalTime;
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
public class RentalTimeDao implements Dao<Long, RentalTime> {

    private static final RentalTimeDao INSTANCE = new RentalTimeDao();
    private static final String DELETE_SQL =
            "DELETE FROM rental_time " +
                    "WHERE id = ? ";
    private static final String SAVE_SQL =
            "INSERT INTO rental_time(car_id, begin_time, end_time, order_id) " +
                    "VALUES (?, ?, ?, ?)";
    private static final String UPDATE_SQL =
            "UPDATE rental_time " +
                    "SET car_id = ?, begin_time = ?, end_time = ?, order_id = ? " +
                    "WHERE id = ? ";
    private static final String FIND_ALL_SQL =
            "SELECT id, car_id, begin_time, end_time, order_id " +
                    "FROM rental_time ";
    private static final String FIND_BY_ID_SQL =
            FIND_ALL_SQL +
                    " WHERE id = ? ";
    private static final String FIND_ALL_BY_CAR_ID_SQL =
            FIND_ALL_SQL +
                    " WHERE car_id = ? ";

    @Override
    @SneakyThrows
    public RentalTime save(RentalTime rentalTime) {
        try (Connection connection = ConnectionManager.get();
             PreparedStatement preparedStatement = connection.prepareStatement(SAVE_SQL, RETURN_GENERATED_KEYS)) {
            preparedStatement.setLong(1, rentalTime.getCarId());
            preparedStatement.setTimestamp(2, Timestamp.valueOf(rentalTime.getBeginTime()));
            preparedStatement.setTimestamp(3, Timestamp.valueOf(rentalTime.getEndTime()));
            preparedStatement.setLong(4, rentalTime.getOrderId());

            preparedStatement.executeUpdate();
            ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
            if (generatedKeys.next()) {
                rentalTime.setId(generatedKeys.getLong("id"));
            }
            return rentalTime;
        }
    }

    @Override
    @SneakyThrows
    public void update(RentalTime rentalTime) {
        try (Connection connection = ConnectionManager.get();
             PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_SQL)) {
            preparedStatement.setLong(1, rentalTime.getCarId());
            preparedStatement.setTimestamp(2, Timestamp.valueOf(rentalTime.getBeginTime()));
            preparedStatement.setTimestamp(3, Timestamp.valueOf(rentalTime.getEndTime()));
            preparedStatement.setLong(4, rentalTime.getOrderId());
            preparedStatement.setLong(5, rentalTime.getId());

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
    public List<RentalTime> findAll() {
        try (Connection connection = ConnectionManager.get();
             PreparedStatement preparedStatement = connection.prepareStatement(FIND_ALL_SQL)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            List<RentalTime> timeList = new ArrayList<>();
            while (resultSet.next()) {
                timeList.add(RentalTime.builder()
                        .id(resultSet.getObject("id", Long.class))
                        .carId(resultSet.getObject("car_id", Long.class))
                        .beginTime(resultSet.getTimestamp("begin_time").toLocalDateTime())
                        .endTime(resultSet.getTimestamp("end_time").toLocalDateTime())
                        .orderId(resultSet.getObject("order_id", Long.class))
                        .build()
                );
            }
            return timeList;
        }
    }

    @Override
    @SneakyThrows
    public Optional<RentalTime> findById(Long id) {
        try (Connection connection = ConnectionManager.get();
             PreparedStatement preparedStatement = connection.prepareStatement(FIND_BY_ID_SQL)) {
            preparedStatement.setLong(1, id);

            ResultSet resultSet = preparedStatement.executeQuery();
            RentalTime rentalTime = resultSet.next()
                    ? RentalTime.builder()
                    .id(resultSet.getObject("id", Long.class))
                    .carId(resultSet.getObject("car_id", Long.class))
                    .beginTime(resultSet.getTimestamp("begin_time").toLocalDateTime())
                    .endTime(resultSet.getTimestamp("end_time").toLocalDateTime())
                    .orderId(resultSet.getObject("order_id", Long.class))
                    .build()
                    : null;

            return Optional.ofNullable(rentalTime);
        }
    }

    @SneakyThrows
    public List<RentalTime> findAllByCarId(Long id) {
        try (Connection connection = ConnectionManager.get();
             PreparedStatement preparedStatement = connection.prepareStatement(FIND_ALL_BY_CAR_ID_SQL)) {
            preparedStatement.setLong(1, id);

            ResultSet resultSet = preparedStatement.executeQuery();
            List<RentalTime> timeList = new ArrayList<>();
            while (resultSet.next()) {
                timeList.add(RentalTime.builder()
                        .id(resultSet.getObject("id", Long.class))
                        .carId(resultSet.getObject("car_id", Long.class))
                        .beginTime(resultSet.getTimestamp("begin_time").toLocalDateTime())
                        .endTime(resultSet.getTimestamp("end_time").toLocalDateTime())
                        .orderId(resultSet.getObject("order_id", Long.class))
                        .build()
                );
            }
            return timeList;
        }
    }

    public static RentalTimeDao getInstance() {
        return INSTANCE;
    }
}
