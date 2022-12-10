package by.car.rental.services;

import by.car.rental.dao.CarDao;
import by.car.rental.dao.ClientDataDao;
import by.car.rental.dao.OrderDao;
import by.car.rental.dao.RentalTimeDao;
import by.car.rental.domain.ClientData;
import by.car.rental.domain.Order;
import by.car.rental.domain.OrderStatus;
import by.car.rental.domain.RentalTime;
import by.car.rental.dto.OrderDto;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class OrderService {
    private static final OrderService INSTANCE = new OrderService();

    private final OrderDao orderDao = OrderDao.getInstance();
    private final CarDao carDao = CarDao.getInstance();
    private final RentalTimeDao rentalTimeDao = RentalTimeDao.getInstance();
    private final ClientDataDao clientDataDao = ClientDataDao.getInstance();

    public List<OrderDto> findAll() {
        return orderDao.findAll().stream()
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
                        .build())
                .collect(toList());
    }

    public List<OrderDto> ordersByUser(Long userId) {
        List<OrderDto> orders = findAll();
        return orders.stream()
                .filter(orderDto -> orderDto.getUserId().equals(userId))
                .collect(toList());
    }

    public void check(OrderDto orderDto) {

        Long userId = orderDto.getUserId();
        Optional<Long> idByUserId = clientDataDao.findIdByUserId(userId);
        Optional<ClientData> clientData = clientDataDao.findById(idByUserId.orElseThrow());
        LocalDate dlExpirationDay = clientData.orElseThrow().getDlExpirationDay();
        double clientCreditAmount = clientData.orElseThrow().getCreditAmount().doubleValue();
        LocalDateTime beginTime = orderDto.getBeginTime();
        LocalDateTime endTime = orderDto.getEndTime();
        BigDecimal dayPrice = carDao.findById(orderDto.getCarId()).orElseThrow().getCarCategory().getDayPrice();

        double endPrice = calculateEndPrice(dayPrice, beginTime, endTime);

        List<RentalTime> rentalTimeList = rentalTimeDao.findAllByCarId(orderDto.getCarId());

        if (orderDto.getOrderStatus().equals(OrderStatus.PROCESSING)) {
            String timeMassage = "";
            if (isCarReserved(beginTime, endTime, rentalTimeList)) {
                timeMassage = " Chosen time is not available. Choose other time or other car. ";
            }
            String moneyMassage = "";
            if (isMoneyNotEnough(endPrice, clientCreditAmount)) {
                moneyMassage = " Not enough money in your account. Add money and try again. ";
            }
            String dlExpirationMassage = "";
            if (isDlExpirationNotValid(endTime, dlExpirationDay)) {
                dlExpirationMassage = " Your driving license is not valid during car using time. " +
                        "Add valid driver license in your account. ";
            }
            String message = " Have a nice trip! ";
            OrderStatus orderStatus = OrderStatus.ACCEPTED;
            if (isCarReserved(beginTime, endTime, rentalTimeList)
                    || isMoneyNotEnough(endPrice, clientCreditAmount)
                    || isDlExpirationNotValid(endTime, dlExpirationDay)) {
                orderStatus = OrderStatus.CANCELED;
                message = String.format("%s %s %s", timeMassage, moneyMassage, dlExpirationMassage);
            } else {
                ClientData clientDataToUpdate = ClientData.builder()
                        .id(idByUserId.orElseThrow())
                        .userId(userId)
                        .driverLicenceNo(clientData.orElseThrow().getDriverLicenceNo())
                        .dlExpirationDay(dlExpirationDay)
                        .creditAmount(BigDecimal.valueOf(clientCreditAmount - endPrice))
                        .build();
                clientDataDao.update(clientDataToUpdate);

                RentalTime rentalTime = RentalTime.builder()
                        .carId(orderDto.getCarId())
                        .orderId(orderDto.getId())
                        .beginTime(beginTime)
                        .endTime(endTime)
                        .build();
                rentalTimeDao.save(rentalTime);

                message = message + "TOTAL PRICE:" + endPrice + "$";
            }
            Optional<Order> order = orderDao.findById(orderDto.getId());
            if (order.isPresent()) {
                Order orderToUpdate = Order.builder()
                        .id(orderDto.getId())
                        .carId(orderDto.getCarId())
                        .userId(orderDto.getUserId())
                        .beginTime(orderDto.getBeginTime())
                        .endTime(orderDto.getEndTime())
                        .orderStatus(orderStatus)
                        .message(message)
                        .build();
                orderDao.update(orderToUpdate);
            }
        }
    }

    public static OrderService getInstance() {
        return INSTANCE;
    }

    private boolean isDlExpirationNotValid(LocalDateTime endTime, LocalDate dlExpirationDay) {
        return dlExpirationDay.isBefore(endTime.toLocalDate());
    }

    private boolean isMoneyNotEnough(double endPrice, double clientCreditAmount) {
        return endPrice > clientCreditAmount;
    }

    private boolean isCarReserved(LocalDateTime beginTime, LocalDateTime endTime, List<RentalTime> rentalTimeList) {
        boolean result = false;
        for (RentalTime rentalTime : rentalTimeList) {
            if ((beginTime.isAfter(rentalTime.getBeginTime()) && beginTime.isBefore(rentalTime.getEndTime())) ||
                    (endTime.isAfter(rentalTime.getBeginTime()) && endTime.isBefore(rentalTime.getEndTime()))) {
                result = true;
            }
        }
        return result;
    }

    private double calculateEndPrice(BigDecimal dayPrice, LocalDateTime beginTime, LocalDateTime endTime) {
        double result = 0;
        long days = ChronoUnit.DAYS.between(beginTime, endTime);
        return result = days * dayPrice.doubleValue();
    }
}
