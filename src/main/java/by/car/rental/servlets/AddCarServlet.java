package by.car.rental.servlets;

import by.car.rental.dto.CreateCarDto;
import by.car.rental.services.CarCategoryService;
import by.car.rental.services.CarService;
import by.car.rental.utils.JspHelper;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;

import static by.car.rental.utils.UrlPath.ADD_CAR;
import static by.car.rental.utils.UrlPath.CARS;

@MultipartConfig(fileSizeThreshold = 1024 * 1024)
@WebServlet(ADD_CAR)
public class AddCarServlet extends HttpServlet {

    private final CarCategoryService carCategoryService = CarCategoryService.getInstance();
    private final CarService carService = CarService.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setAttribute("carCategory", carCategoryService.findAllCategory());
        req.getRequestDispatcher(JspHelper.getPath("add-car"))
                .forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        CreateCarDto carDto = CreateCarDto.builder()
                .model(req.getParameter("model"))
                .colour(req.getParameter("colour"))
                .seatsQuantity(req.getParameter("seatsQuantity"))
                .carCategory(req.getParameter("carCategory"))
                .image(req.getPart("image"))
                .build();

        carService.create(carDto);
        resp.sendRedirect(CARS);
    }
}
