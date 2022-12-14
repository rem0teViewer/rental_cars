package by.car.rental.servlets;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

import static by.car.rental.utils.UrlPath.DOWNLOAD;
import static by.car.rental.utils.UrlPath.FILE_FULL_PATH;

@WebServlet(DOWNLOAD)
public class DownloadServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setHeader("Content-Disposition", "attachment; filename=\"report.txt\"");
        resp.setContentType("text/plain");
        resp.setCharacterEncoding(StandardCharsets.UTF_8.name());

        try (PrintWriter writer = resp.getWriter()) {
            Files.createDirectories(FILE_FULL_PATH.getParent());
            writer.write(Files.readString(FILE_FULL_PATH));
        }
    }
}
