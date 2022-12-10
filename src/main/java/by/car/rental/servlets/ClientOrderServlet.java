package by.car.rental.servlets;

import by.car.rental.dto.OrderDto;
import by.car.rental.dto.UserDto;
import by.car.rental.services.OrderService;
import by.car.rental.utils.JspHelper;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

import static by.car.rental.utils.UrlPath.CLIENT_ORDERS;

@WebServlet(CLIENT_ORDERS)
public class ClientOrderServlet extends HttpServlet {

    private final OrderService orderService = OrderService.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        UserDto userDto = (UserDto) req.getSession().getAttribute("user");
        List<OrderDto> ordersByUser = orderService.ordersByUser(userDto.getId());
        req.setAttribute("orders", ordersByUser);
        req.getRequestDispatcher(JspHelper.getPath("client-orders"))
                .forward(req, resp);
    }
}
