package by.car.rental.mapper;

import by.car.rental.domain.ClientData;
import by.car.rental.dto.CreateClientDataDto;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
public class CreateClientDataMapper implements Mapper<CreateClientDataDto, ClientData> {

    private static final CreateClientDataMapper INSTANCE = new CreateClientDataMapper();

    @Override
    public ClientData mapFrom(CreateClientDataDto object) {
        return ClientData.builder()
                .userId(Long.valueOf(object.getUserId()))
                .driverLicenceNo(object.getDriverLicenceNo())
                .dlExpirationDay(LocalDate.parse(object.getDlExpirationDay()))
                .creditAmount(BigDecimal.valueOf(Double.parseDouble(object.getCreditAmount())))
                .build();
    }

    public static CreateClientDataMapper getInstance() {
        return INSTANCE;
    }
}
