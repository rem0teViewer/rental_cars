package by.car.rental.servlets;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;

import static by.car.rental.utils.UrlPath.LOCALE;
import static by.car.rental.utils.UrlPath.LOGIN;

@WebServlet(LOCALE)
public class LocaleServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String language = req.getParameter("lang");
        req.getSession().setAttribute("lang", language);

        String prevPage = req.getHeader("referer");
        String page = prevPage != null ? prevPage : LOGIN;
        resp.sendRedirect(page + "?lang=" + language);
    }
}
