package by.car.rental.services;

import by.car.rental.dao.CarCategoryDao;
import by.car.rental.domain.CarCategory;
import by.car.rental.dto.CarCategoryDto;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CarCategoryService {
    private static final CarCategoryService INSTANCE = new CarCategoryService();
    private final CarCategoryDao carCategoryDao = CarCategoryDao.getInstance();

    public List<String> findAllCategory() {
        return carCategoryDao.findAll()
                .stream()
                .map(CarCategory::getCategory)
                .collect(toList());
    }

    public Optional<CarCategoryDto> findById(Long id) {
        return carCategoryDao.findById(id)
                .map(carCategory -> CarCategoryDto.builder()
                        .id(carCategory.getId())
                        .category(carCategory.getCategory())
                        .dayPrice(carCategory.getDayPrice())
                        .build());
    }

    public Optional<CarCategory> findIdByCategory(String carCategory) {
        return carCategoryDao.findByCategory(carCategory);
    }

    public static CarCategoryService getInstance() {
        return INSTANCE;
    }
}
