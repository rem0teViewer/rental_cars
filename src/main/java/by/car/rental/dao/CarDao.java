package by.car.rental.dao;

import by.car.rental.domain.Car;
import by.car.rental.exception.DaoException;
import by.car.rental.utils.ConnectionManager;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CarDao implements Dao<Long, Car> {

    private static final CarDao INSTANCE = new CarDao();

    private static final String SAVE_SQL =
            "INSERT INTO car(model, colour, seats_quantity, car_category_id, image) " +
                    "VALUES (?, ?, ?, ?, ?) ";
    private static final String UPDATE_SQL =
            "UPDATE car " +
                    "SET model = ?, colour = ?, seats_quantity = ?, car_category_id = ?, image = ? " +
                    "WHERE id = ? ";
    private static final String DELETE_SQL =
            "DELETE FROM car " +
                    "WHERE id = ? ";
    private static final String FIND_ALL_SQL =
            "SELECT id, model, colour, seats_quantity, car_category_id, image  " +
                    "FROM car ";
    private static final String FIND_BY_ID_SQL =
            FIND_ALL_SQL +
                    " WHERE id = ? ";

    private final CarCategoryDao carCategoryDao = CarCategoryDao.getInstance();

    @Override
    public Car save(Car car) {
        try (Connection connection = ConnectionManager.get();
             PreparedStatement preparedStatement = connection.prepareStatement(SAVE_SQL, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, car.getModel());
            preparedStatement.setString(2, car.getColour());
            preparedStatement.setInt(3, car.getSeatsQuantity());
            preparedStatement.setLong(4, car.getCarCategory().getId());
            preparedStatement.setString(5, car.getImage());

            preparedStatement.executeUpdate();

            ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
            if (generatedKeys.next()) {
                car.setId(generatedKeys.getLong("id"));
            }
            return car;
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public void update(Car car) {
        try (Connection connection = ConnectionManager.get();
             PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_SQL)) {
            preparedStatement.setString(1, car.getModel());
            preparedStatement.setString(2, car.getColour());
            preparedStatement.setInt(3, car.getSeatsQuantity());
            preparedStatement.setLong(4, car.getCarCategory().getId());
            preparedStatement.setString(5, car.getImage());

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new DaoException(e);
        }

    }

    @Override
    public boolean delete(Long id) {
        try (Connection connection = ConnectionManager.get();
             PreparedStatement preparedStatement = connection.prepareStatement(DELETE_SQL)) {
            preparedStatement.setLong(1, id);
            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public List<Car> findAll() {
        try (Connection connection = ConnectionManager.get();
             PreparedStatement preparedStatement = connection.prepareStatement(FIND_ALL_SQL)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            List<Car> cars = new ArrayList<>();
            while (resultSet.next()) {
                cars.add(new Car(
                        resultSet.getLong("id"),
                        resultSet.getString("model"),
                        resultSet.getString("colour"),
                        resultSet.getInt("seats_quantity"),
                        carCategoryDao.findById(resultSet.getLong("car_category_id"))
                                .orElse(null),
                        resultSet.getString("image")
                ));
            }
            return cars;
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public Optional<Car> findById(Long id) {
        try (Connection connection = ConnectionManager.get();
             PreparedStatement preparedStatement = connection.prepareStatement(FIND_BY_ID_SQL)) {
            preparedStatement.setLong(1, id);

            ResultSet resultSet = preparedStatement.executeQuery();
            Car car = resultSet.next()
                    ? new Car(
                    resultSet.getLong("id"),
                    resultSet.getString("model"),
                    resultSet.getString("colour"),
                    resultSet.getInt("seats_quantity"),
                    carCategoryDao.findById(resultSet.getLong("car_category_id"))
                            .orElse(null),
                    resultSet.getString("image"))
                    : null;

            return Optional.ofNullable(car);
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    public static CarDao getInstance() {
        return INSTANCE;
    }
}
