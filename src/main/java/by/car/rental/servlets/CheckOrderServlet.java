package by.car.rental.servlets;

import by.car.rental.dto.OrderDto;
import by.car.rental.services.SeeOrderService;
import by.car.rental.utils.JspHelper;
import lombok.SneakyThrows;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static by.car.rental.utils.UrlPath.CHECK_ORDER;

@WebServlet(CHECK_ORDER)
public class CheckOrderServlet extends HttpServlet {

    private final SeeOrderService seeOrderService = SeeOrderService.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        Long orderId = Long.valueOf(req.getParameter("orderId"));

        seeOrderService.findById(orderId)
                .ifPresentOrElse(orderDto -> forwardOrderDto(req, resp, orderDto),
                        () -> sendError(resp));

    }

    @SneakyThrows
    private void sendError(HttpServletResponse resp) {
        resp.setStatus(400);
        resp.sendError(400, "NO ORDER");
    }

    @SneakyThrows
    private void forwardOrderDto(HttpServletRequest req, HttpServletResponse resp, OrderDto orderDto) {
        req.setAttribute("order", orderDto);
        req.getRequestDispatcher(JspHelper.getPath("check-order"))
                .forward(req, resp);
    }
}
