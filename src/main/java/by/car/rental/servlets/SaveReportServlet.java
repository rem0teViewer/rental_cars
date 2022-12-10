package by.car.rental.servlets;

import by.car.rental.services.ReportService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;

import static by.car.rental.utils.UrlPath.*;
import static java.nio.file.StandardOpenOption.CREATE;

@WebServlet(SAVE)
public class SaveReportServlet extends HttpServlet {

    private final ReportService reportService = ReportService.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Files.writeString(FILE_FULL_PATH, reportService.createReport(), CREATE, StandardOpenOption.TRUNCATE_EXISTING);
        resp.sendRedirect(CARS);
    }
}
