package by.car.rental.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CarCategory {

    private Long id;
    private String category;
    private BigDecimal dayPrice;
}
