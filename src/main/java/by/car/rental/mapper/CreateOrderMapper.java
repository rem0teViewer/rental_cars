package by.car.rental.mapper;

import by.car.rental.domain.Order;
import by.car.rental.dto.CreateOrderDto;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
public class CreateOrderMapper implements Mapper<CreateOrderDto, Order> {

    private static final CreateOrderMapper INSTANCE = new CreateOrderMapper();

    @Override
    public Order mapFrom(CreateOrderDto object) {
        return Order.builder()
                .userId(object.getUserId())
                .carId(object.getCarId())
                .beginTime(LocalDateTime.parse(object.getBeginTime()))
                .endTime(LocalDateTime.parse(object.getEndTime()))
                .orderStatus(object.getOrderStatus())
                .message(object.getMessage())
                .build();
    }

    public static CreateOrderMapper getInstance() {
        return INSTANCE;
    }
}
