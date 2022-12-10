package by.car.rental.servlets;

import by.car.rental.dto.OrderDto;
import by.car.rental.services.OrderService;
import by.car.rental.services.SeeOrderService;
import by.car.rental.utils.JspHelper;
import lombok.SneakyThrows;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static by.car.rental.utils.UrlPath.CHECK;

@WebServlet(CHECK)
public class CheckServlet extends HttpServlet {

    private final SeeOrderService seeOrderService = SeeOrderService.getInstance();
    private final OrderService orderService = OrderService.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        Long orderId = Long.valueOf(req.getParameter("orderId"));

        seeOrderService.findById(orderId)
                .ifPresentOrElse(orderDto -> forwardCheckedOrderDto(req, resp, orderDto),
                        () -> sendError(resp));

    }

    @SneakyThrows
    private void sendError(HttpServletResponse resp) {
        resp.setStatus(400);
        resp.sendError(400, "NO ORDERS");
    }

    @SneakyThrows
    private void forwardCheckedOrderDto(HttpServletRequest req, HttpServletResponse resp, OrderDto orderDto) {
        orderService.check(orderDto);
        req.setAttribute("orders", orderService.findAll());
        req.getRequestDispatcher(JspHelper.getPath("orders"))
                .forward(req, resp);
    }

}
