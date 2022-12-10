package by.car.rental.dto;

import by.car.rental.domain.OrderStatus;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class CreateOrderDto {
    Long userId;
    Long carId;
    String beginTime;
    String endTime;
    OrderStatus orderStatus;
    String message;
}
