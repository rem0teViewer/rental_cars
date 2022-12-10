package by.car.rental.dto;

import by.car.rental.domain.OrderStatus;
import lombok.Builder;
import lombok.Value;

import java.time.LocalDateTime;

@Value
@Builder
public class OrderDto {
    Long id;
    Long userId;
    Long carId;
    String description;
    LocalDateTime beginTime;
    LocalDateTime endTime;
    OrderStatus orderStatus;
    String message;
}
