package by.car.rental.dto;

import lombok.Builder;
import lombok.Value;

import javax.servlet.http.Part;

@Value
@Builder
public class CreateCarDto {
    String model;
    String colour;
    String seatsQuantity;
    String carCategory;
    Part image;
}
