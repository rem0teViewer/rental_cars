package by.car.rental.dao;

import by.car.rental.domain.ClientData;
import by.car.rental.utils.ConnectionManager;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static java.sql.Statement.RETURN_GENERATED_KEYS;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ClientDataDao implements Dao<Long, ClientData> {
    private static final ClientDataDao INSTANCE = new ClientDataDao();
    private static final String DELETE_SQL =
            "DELETE FROM client_data " +
                    "WHERE id = ? ";
    private static final String SAVE_SQL =
            "INSERT INTO client_data(user_id, driver_licence_no, dl_expiration_day, credit_amount) " +
                    "VALUES (?, ?, ?, ?)";
    private static final String UPDATE_SQL =
            "UPDATE client_data " +
                    "SET user_id = ?, driver_licence_no = ?, dl_expiration_day = ?, credit_amount = ? " +
                    "WHERE id = ? ";
    private static final String FIND_ALL_SQL =
            "SELECT id, user_id, driver_licence_no, dl_expiration_day, credit_amount " +
                    "FROM client_data ";
    private static final String FIND_ALL_BY_ID_SQL =
            FIND_ALL_SQL +
                    " WHERE id = ? ";
    private static final String FIND_ID_BY_USER_ID_SQL =
            "SELECT id " +
                    "FROM client_data " +
                    "WHERE user_id = ? ";

    @Override
    @SneakyThrows
    public ClientData save(ClientData clientData) {
        try (Connection connection = ConnectionManager.get();
             PreparedStatement preparedStatement = connection.prepareStatement(SAVE_SQL, RETURN_GENERATED_KEYS)) {
            preparedStatement.setLong(1, clientData.getUserId());
            preparedStatement.setString(2, clientData.getDriverLicenceNo());
            preparedStatement.setTimestamp(3,
                    Timestamp.valueOf(clientData.getDlExpirationDay().atStartOfDay()));
            preparedStatement.setBigDecimal(4, clientData.getCreditAmount());

            preparedStatement.executeUpdate();
            ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
            if (generatedKeys.next()) {
                clientData.setId(generatedKeys.getLong("id"));
            }
            return clientData;
        }
    }

    @Override
    @SneakyThrows
    public void update(ClientData clientData) {
        try (Connection connection = ConnectionManager.get();
             PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_SQL)) {
            preparedStatement.setLong(1, clientData.getUserId());
            preparedStatement.setString(2, clientData.getDriverLicenceNo());
            preparedStatement.setTimestamp(3,
                    Timestamp.valueOf(clientData.getDlExpirationDay().atStartOfDay()));
            preparedStatement.setBigDecimal(4, clientData.getCreditAmount());
            preparedStatement.setLong(5, clientData.getId());

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
    public List<ClientData> findAll() {
        try (Connection connection = ConnectionManager.get();
             PreparedStatement preparedStatement = connection.prepareStatement(FIND_ALL_SQL)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            List<ClientData> clientsData = new ArrayList<>();
            while (resultSet.next()) {
                clientsData.add(ClientData.builder()
                        .id(resultSet.getObject("id", Long.class))
                        .userId(resultSet.getObject("user_id", Long.class))
                        .driverLicenceNo(resultSet.getObject("driver_licence_no", String.class))
                        .dlExpirationDay(resultSet.getDate("dl_expiration_day").toLocalDate())
                        .creditAmount(resultSet.getObject("credit_amount", BigDecimal.class))
                        .build());
            }
            return clientsData;
        }
    }

    @Override
    @SneakyThrows
    public Optional<ClientData> findById(Long id) {
        try (Connection connection = ConnectionManager.get();
             PreparedStatement preparedStatement = connection.prepareStatement(FIND_ALL_BY_ID_SQL)) {
            preparedStatement.setLong(1, id);

            ResultSet resultSet = preparedStatement.executeQuery();
            ClientData clientData = resultSet.next()
                    ? ClientData.builder()
                    .id(resultSet.getObject("id", Long.class))
                    .userId(resultSet.getObject("user_id", Long.class))
                    .driverLicenceNo(resultSet.getObject("driver_licence_no", String.class))
                    .dlExpirationDay(resultSet.getDate("dl_expiration_day").toLocalDate())
                    .creditAmount(resultSet.getObject("credit_amount", BigDecimal.class))
                    .build()
                    : null;

            return Optional.ofNullable(clientData);
        }
    }

    @SneakyThrows
    public Optional<Long> findIdByUserId(Long userId) {
        try (Connection connection = ConnectionManager.get();
             PreparedStatement preparedStatement = connection.prepareStatement(FIND_ID_BY_USER_ID_SQL)) {
            preparedStatement.setLong(1, userId);

            ResultSet resultSet = preparedStatement.executeQuery();
            Long id = resultSet.next()
                    ? resultSet.getLong("id")
                    : null;

            return Optional.ofNullable(id);
        }

    }

    public static ClientDataDao getInstance() {
        return INSTANCE;
    }
}
