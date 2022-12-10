package by.car.rental.servlets;

import by.car.rental.services.CarService;
import by.car.rental.utils.JspHelper;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static by.car.rental.utils.UrlPath.CARS;

@WebServlet(CARS)
public class CarServlet extends HttpServlet {

    private final CarService carService = CarService.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setAttribute("cars", carService.findAll());
        req.getRequestDispatcher(JspHelper.getPath("cars"))
                .forward(req, resp);
    }
}
