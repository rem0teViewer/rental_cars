package by.car.rental.servlets;

import by.car.rental.domain.UsersRole;
import by.car.rental.dto.CreateUserDto;
import by.car.rental.exception.ValidationException;
import by.car.rental.services.UserService;
import by.car.rental.utils.JspHelper;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static by.car.rental.utils.UrlPath.LOGIN;
import static by.car.rental.utils.UrlPath.REGISTRATION;

@WebServlet(REGISTRATION)
public class RegistrationServlet extends HttpServlet {

    private final UserService userService = UserService.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setAttribute("roles", UsersRole.values());
        req.getRequestDispatcher(JspHelper.getPath("registration"))
                .forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        CreateUserDto user = CreateUserDto.builder()
                .firstName(req.getParameter("firstName"))
                .lastName(req.getParameter("lastName"))
                .email(req.getParameter("email"))
                .password(req.getParameter("password"))
                .role(req.getParameter("role"))
                .build();

        try {
            userService.create(user);
            resp.sendRedirect(LOGIN);
        } catch (ValidationException exception) {
            req.setAttribute("errors", exception.getErrors());
            doGet(req, resp);
        }
    }
}
