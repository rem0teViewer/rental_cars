package by.car.rental.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Car {

    private Long id;
    private String model;
    private String colour;
    private Integer seatsQuantity;
    private CarCategory carCategory;
    private String image;

    public Car(String model, String colour, Integer seatsQuantity, CarCategory carCategory, String image) {
        this.model = model;
        this.colour = colour;
        this.seatsQuantity = seatsQuantity;
        this.carCategory = carCategory;
        this.image = image;
    }
}
