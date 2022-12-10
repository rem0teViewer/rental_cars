package by.car.rental.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ClientData {

    private Long id;
    private Long userId;
    private String driverLicenceNo;
    private LocalDate dlExpirationDay;
    private BigDecimal creditAmount;
}
