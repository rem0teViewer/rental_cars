package by.car.rental.servlets;

import by.car.rental.dto.CarCategoryDto;
import by.car.rental.services.CarCategoryService;
import by.car.rental.utils.JspHelper;
import lombok.SneakyThrows;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static by.car.rental.utils.UrlPath.CAR_CATEGORY;

@WebServlet(CAR_CATEGORY)
public class CarCategoryServlet extends HttpServlet {

    private final CarCategoryService carCategoryService = CarCategoryService.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        Long carCategoryId = Long.valueOf(req.getParameter("carCategoryId"));

        carCategoryService.findById(carCategoryId)
                .ifPresentOrElse(carCategoryDto -> forwardCarCategoryDto(req, resp, carCategoryDto),
                        () -> sendError(resp));

    }

    @SneakyThrows
    private void sendError(HttpServletResponse resp) {
        resp.setStatus(400);
        resp.sendError(400, "NO CATEGORY");
    }

    @SneakyThrows
    private void forwardCarCategoryDto(HttpServletRequest req, HttpServletResponse resp, CarCategoryDto carCategoryDto) {
        req.setAttribute("carCategory", carCategoryDto);
        req.getRequestDispatcher(JspHelper.getPath("car-category"))
                .forward(req, resp);
    }
}
