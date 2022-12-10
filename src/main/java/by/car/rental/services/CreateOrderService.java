package by.car.rental.services;

import by.car.rental.dao.OrderDao;
import by.car.rental.domain.Order;
import by.car.rental.dto.CreateOrderDto;
import by.car.rental.mapper.CreateOrderMapper;
import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
public class CreateOrderService {
    private static final CreateOrderService INSTANCE = new CreateOrderService();

    private final OrderDao orderDao = OrderDao.getInstance();
    private final CreateOrderMapper createOrderMapper = CreateOrderMapper.getInstance();

    public void create(CreateOrderDto createOrderDto) {
        Order order = createOrderMapper.mapFrom(createOrderDto);
        orderDao.save(order);
    }

    public static CreateOrderService getInstance() {
        return INSTANCE;
    }
}
