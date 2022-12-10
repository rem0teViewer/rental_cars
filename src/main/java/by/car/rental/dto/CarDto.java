package by.car.rental.dto;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class CarDto {
    Long id;
    String description;
    Long carCategoryId;
    String image;
}
