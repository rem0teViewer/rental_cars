package by.car.rental.services;

import by.car.rental.dao.CarDao;
import by.car.rental.dao.OrderDao;
import by.car.rental.dto.OrderDto;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.format.DateTimeFormatter;
import java.util.Optional;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SeeOrderService {
    private static final SeeOrderService INSTANCE = new SeeOrderService();

    private final OrderDao orderDao = OrderDao.getInstance();
    private final CarDao carDao = CarDao.getInstance();

    public Optional<OrderDto> findById(Long id) {
        return orderDao.findById(id)
                .map(order -> OrderDto.builder()
                        .id(order.getId())
                        .userId(order.getUserId())
                        .carId(order.getCarId())
                        .description(String.format("%s, BEGIN: %s, END: %s, STATUS: %s, MESSAGE: %s",
                                carDao.findById(order.getCarId()).orElseThrow().getModel(),
                                order.getBeginTime().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                                order.getEndTime().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                                order.getOrderStatus().name(),
                                order.getMessage()))
                        .beginTime(order.getBeginTime())
                        .endTime(order.getEndTime())
                        .orderStatus(order.getOrderStatus())
                        .message(order.getMessage())
                        .build());
    }

    public static SeeOrderService getInstance() {
        return INSTANCE;
    }

}
