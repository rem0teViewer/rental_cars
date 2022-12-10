package by.car.rental.servlets;

import by.car.rental.dto.CreateClientDataDto;
import by.car.rental.dto.UserDto;
import by.car.rental.exception.ValidationException;
import by.car.rental.services.ClientDataService;
import by.car.rental.utils.JspHelper;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;

import static by.car.rental.utils.UrlPath.CARS;
import static by.car.rental.utils.UrlPath.CLIENT_DATA;

@WebServlet(CLIENT_DATA)
public class ClientDataServlet extends HttpServlet {

    private final ClientDataService clientDataService = ClientDataService.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher(JspHelper.getPath("client-data"))
                .forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        UserDto userDto = (UserDto) req.getSession().getAttribute("user");
        CreateClientDataDto clientDataDto = CreateClientDataDto.builder()
                .userId(String.valueOf(userDto.getId()))
                .driverLicenceNo(req.getParameter("driverLicenceNo"))
                .dlExpirationDay(req.getParameter("dlExpirationDay"))
                .creditAmount(req.getParameter("creditAmount"))
                .build();

        try {
            clientDataService.create(clientDataDto);
            resp.sendRedirect(CARS);
        } catch (ValidationException exception) {
            req.setAttribute("errors", exception.getErrors());
            doGet(req, resp);
        }
    }
}
