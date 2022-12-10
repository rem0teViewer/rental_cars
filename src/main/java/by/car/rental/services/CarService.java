package by.car.rental.services;

import by.car.rental.dao.CarDao;
import by.car.rental.domain.Car;
import by.car.rental.dto.CarDto;
import by.car.rental.dto.CreateCarDto;
import by.car.rental.mapper.CreateCarMapper;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;

import java.util.List;

import static java.util.stream.Collectors.toList;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CarService {

    public static final CarService INSTANCE = new CarService();

    private final CarDao carDao = CarDao.getInstance();
    private final CreateCarMapper createCarMapper = CreateCarMapper.getInstance();
    private final ImageService imageService = ImageService.getInstance();

    public List<CarDto> findAll() {
        return carDao.findAll().stream()
                .map(car -> CarDto.builder()
                        .id(car.getId())
                        .description(String.format("%s, %s, %s, SEATS № %s, %s $ per day.",
                                car.getModel(), car.getColour(), car.getCarCategory().getCategory(),
                                car.getSeatsQuantity(), car.getCarCategory().getDayPrice()))
                        .carCategoryId(car.getCarCategory().getId())
                        .image(car.getImage())
                        .build())
                .collect(toList());
    }

    public Long findByDescription(List<CarDto> cars, String description) {
        return cars.stream()
                .filter(carDto -> carDto.getDescription().equals(description))
                .mapToLong(CarDto::getId)
                .sum();
    }

    @SneakyThrows
    public Long create(CreateCarDto createCarDto) {
        Car car = createCarMapper.mapFrom(createCarDto);
        imageService.upload(car.getImage(), createCarDto.getImage().getInputStream());
        carDao.save(car);
        return car.getId();
    }

    public static CarService getInstance() {
        return INSTANCE;
    }


}
