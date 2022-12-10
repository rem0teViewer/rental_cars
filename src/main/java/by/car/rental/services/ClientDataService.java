package by.car.rental.services;

import by.car.rental.dao.ClientDataDao;
import by.car.rental.domain.ClientData;
import by.car.rental.dto.CreateClientDataDto;
import by.car.rental.mapper.CreateClientDataMapper;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Optional;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ClientDataService {
    private static final ClientDataService INSTANCE = new ClientDataService();

    private final ClientDataDao clientDataDao = ClientDataDao.getInstance();
    private final CreateClientDataMapper createClientDataMapper = CreateClientDataMapper.getInstance();

    public void create(CreateClientDataDto clientDataDto) {
        ClientData clientData = createClientDataMapper.mapFrom(clientDataDto);
        clientDataDao.save(clientData);
    }

    public Optional<Long> findClientDataId(Long userId) {
        return clientDataDao.findIdByUserId(userId);
    }

    public static ClientDataService getInstance() {
        return INSTANCE;
    }
}
