package by.car.rental.services;

import by.car.rental.dao.UserDao;
import by.car.rental.domain.User;
import by.car.rental.dto.OrderDto;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ReportService {
    private static final ReportService INSTANCE = new ReportService();

    private final OrderService orderService = OrderService.getInstance();
    private final UserDao userDao = UserDao.getInstance();

    public String createReport() {
        List<OrderDto> orderDtoList = orderService.findAll();
        StringBuffer report = new StringBuffer();
        for (OrderDto orderDto : orderDtoList) {
            User user = userDao.findById(orderDto.getUserId()).orElseThrow();
            String order = String.format("ORDER ID: %s, USER: %s %s, %s;" + System.lineSeparator(),
                    orderDto.getId(), user.getFirstName(), user.getLastName(), orderDto.getDescription());
            report.append(order);
        }
        return report.toString();
    }

    public static ReportService getInstance() {
        return INSTANCE;
    }
}
