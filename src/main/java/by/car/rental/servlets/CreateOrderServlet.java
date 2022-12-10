package by.car.rental.servlets;

import by.car.rental.domain.OrderStatus;
import by.car.rental.dto.CarDto;
import by.car.rental.dto.CreateOrderDto;
import by.car.rental.dto.UserDto;
import by.car.rental.exception.ValidationException;
import by.car.rental.services.CarService;
import by.car.rental.services.CreateOrderService;
import by.car.rental.utils.JspHelper;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

import static by.car.rental.utils.UrlPath.CARS;
import static by.car.rental.utils.UrlPath.ORDER;

@WebServlet(ORDER)
public class CreateOrderServlet extends HttpServlet {
    private final CarService carService = CarService.getInstance();
    private final CreateOrderService createOrderService = CreateOrderService.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setAttribute("cars", carService.findAll());
        req.getRequestDispatcher(JspHelper.getPath("order"))
                .forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<CarDto> cars = carService.findAll();
        UserDto userDto = (UserDto) req.getSession().getAttribute("user");
        CreateOrderDto orderDto = CreateOrderDto.builder()
                .userId(userDto.getId())
                .carId(carService.findByDescription(cars, req.getParameter("car")))
                .beginTime(req.getParameter("beginTime"))
                .endTime(req.getParameter("endTime"))
                .orderStatus(OrderStatus.PROCESSING)
                .message("In process.")
                .build();

        try {
            createOrderService.create(orderDto);
            resp.sendRedirect(CARS);
        } catch (ValidationException exception) {
            req.setAttribute("errors", exception.getErrors());
            doGet(req, resp);
        }
    }
}
