package by.car.rental.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RentalTime {

    private Long id;
    private Long carId;
    private LocalDateTime beginTime;
    private LocalDateTime endTime;
    private Long orderId;
}
