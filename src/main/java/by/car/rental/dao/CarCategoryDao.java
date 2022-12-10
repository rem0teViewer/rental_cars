package by.car.rental.dao;

import by.car.rental.domain.CarCategory;
import by.car.rental.exception.DaoException;
import by.car.rental.utils.ConnectionManager;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static java.sql.Statement.RETURN_GENERATED_KEYS;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CarCategoryDao implements Dao<Long, CarCategory> {

    private static final CarCategoryDao INSTANCE = new CarCategoryDao();

    private static final String SAVE_SQL =
            "INSERT INTO car_category (category, day_price) " +
                    "VALUES (?, ?) ";
    private static final String UPDATE_SQL =
            "UPDATE car_category " +
                    "SET category = ?, day_price = ? " +
                    "WHERE id = ? ";
    public static final String DELETE_SQL =
            "DELETE FROM car_category " +
                    "WHERE id = ?";
    public static final String FIND_ALL_SQL =
            "SELECT id, category, day_price " +
                    "FROM car_category ";
    public static final String FIND_BY_ID_SQL =
            FIND_ALL_SQL +
                    "WHERE id = ?";
    private static final String FIND_BY_CATEGORY =
            FIND_ALL_SQL +
                    "WHERE category = ? ";

    @Override
    public CarCategory save(CarCategory carCategory) {
        try (Connection connection = ConnectionManager.get();
             PreparedStatement preparedStatement = connection.prepareStatement(SAVE_SQL, RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, carCategory.getCategory());
            preparedStatement.setBigDecimal(2, carCategory.getDayPrice());

            preparedStatement.executeUpdate();

            ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
            if (generatedKeys.next()) {
                carCategory.setId(generatedKeys.getLong("id"));
            }
            return carCategory;
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public void update(CarCategory carCategory) {
        try (Connection connection = ConnectionManager.get();
             PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_SQL)) {
            preparedStatement.setString(1, carCategory.getCategory());
            preparedStatement.setBigDecimal(2, carCategory.getDayPrice());
            preparedStatement.setLong(3, carCategory.getId());

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
    public List<CarCategory> findAll() {
        try (Connection connection = ConnectionManager.get();
             PreparedStatement preparedStatement = connection.prepareStatement(FIND_ALL_SQL)) {

            ResultSet resultSet = preparedStatement.executeQuery();
            List<CarCategory> categories = new ArrayList<>();
            while (resultSet.next()) {
                categories.add(new CarCategory(
                        resultSet.getLong("id"),
                        resultSet.getString("category"),
                        resultSet.getBigDecimal("day_price")
                ));
            }
            return categories;
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public Optional<CarCategory> findById(Long id) {
        try (Connection connection = ConnectionManager.get();
             PreparedStatement preparedStatement = connection.prepareStatement(FIND_BY_ID_SQL)) {
            preparedStatement.setLong(1, id);

            ResultSet resultSet = preparedStatement.executeQuery();
            CarCategory carCategory = resultSet.next()
                    ? new CarCategory(
                    resultSet.getLong("id"),
                    resultSet.getString("category"),
                    resultSet.getBigDecimal("day_price"))
                    : null;

            return Optional.ofNullable(carCategory);
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    public Optional<CarCategory> findByCategory(String category) {
        try (Connection connection = ConnectionManager.get();
             PreparedStatement preparedStatement = connection.prepareStatement(FIND_BY_CATEGORY)) {
            preparedStatement.setString(1, category);

            ResultSet resultSet = preparedStatement.executeQuery();
            CarCategory carCategory = resultSet.next()
                    ? new CarCategory(
                    resultSet.getLong("id"),
                    resultSet.getString("category"),
                    resultSet.getBigDecimal("day_price"))
                    : null;

            return Optional.ofNullable(carCategory);
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    public static CarCategoryDao getInstance() {
        return INSTANCE;
    }
}
