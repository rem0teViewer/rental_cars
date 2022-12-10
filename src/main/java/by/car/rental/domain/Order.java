package by.car.rental.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Order {

    private Long id;
    private Long userId;
    private Long carId;
    private LocalDateTime beginTime;
    private LocalDateTime endTime;
    private OrderStatus orderStatus;
    private String message;
}
