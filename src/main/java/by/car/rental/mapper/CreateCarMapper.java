package by.car.rental.mapper;

import by.car.rental.domain.Car;
import by.car.rental.dto.CreateCarDto;
import by.car.rental.services.CarCategoryService;
import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
public class CreateCarMapper implements Mapper<CreateCarDto, Car> {

    private static final String IMAGE_FOLDER = "cars\\";
    private static final CreateCarMapper INSTANCE = new CreateCarMapper();

    private final CarCategoryService carCategoryService = CarCategoryService.getInstance();

    @Override
    public Car mapFrom(CreateCarDto object) {
        return new Car(
                object.getModel(),
                object.getColour(),
                Integer.valueOf(object.getSeatsQuantity()),
                carCategoryService.findIdByCategory(object.getCarCategory()).orElseThrow(),
                IMAGE_FOLDER + object.getImage().getSubmittedFileName());
    }

    public static CreateCarMapper getInstance() {
        return INSTANCE;
    }
}
