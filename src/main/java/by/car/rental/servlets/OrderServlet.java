package by.car.rental.servlets;

import by.car.rental.services.OrderService;
import by.car.rental.utils.JspHelper;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static by.car.rental.utils.UrlPath.ORDERS;

@WebServlet(ORDERS)
public class OrderServlet extends HttpServlet {

    private final OrderService orderService = OrderService.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setAttribute("orders", orderService.findAll());
        req.getRequestDispatcher(JspHelper.getPath("orders"))
                .forward(req, resp);
    }
}
