package by.car.rental.servlets;

import by.car.rental.domain.UsersRole;
import by.car.rental.dto.UserDto;
import by.car.rental.services.ClientDataService;
import by.car.rental.services.UserService;
import by.car.rental.utils.JspHelper;
import lombok.SneakyThrows;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;

import static by.car.rental.utils.UrlPath.*;

@WebServlet(LOGIN)
public class LoginServlet extends HttpServlet {
    private final UserService userService = UserService.getInstance();
    private final ClientDataService clientDataService = ClientDataService.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher(JspHelper.getPath("login"))
                .forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        userService.login(req.getParameter("email"), req.getParameter("password"))
                .ifPresentOrElse(
                        user -> onLoginSuccess(user, req, resp),
                        () -> onLoginFail(req, resp)
                );
    }

    @SneakyThrows
    private void onLoginFail(HttpServletRequest req, HttpServletResponse resp) {
        resp.sendRedirect(LOGIN + "?error&email=" + req.getParameter("email"));
    }

    @SneakyThrows
    private void onLoginSuccess(UserDto user, HttpServletRequest req, HttpServletResponse resp) {
        req.getSession().setAttribute("user", user);
        if (user.getRole().equals(UsersRole.CLIENT)) {
            if (clientDataService.findClientDataId(user.getId()).isPresent()) {
                resp.sendRedirect(CARS);
            } else {
                resp.sendRedirect(CLIENT_DATA);
            }
        } else {
            resp.sendRedirect(CARS);
        }
    }
}
