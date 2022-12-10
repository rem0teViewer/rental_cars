package by.car.rental.dto;

import lombok.Builder;
import lombok.Value;

import java.math.BigDecimal;

@Value
@Builder
public class CarCategoryDto {
    Long id;
    String category;
    BigDecimal dayPrice;
}
