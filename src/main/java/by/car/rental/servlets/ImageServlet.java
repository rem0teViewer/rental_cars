package by.car.rental.servlets;

import by.car.rental.services.ImageService;
import lombok.SneakyThrows;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import static by.car.rental.utils.UrlPath.IMAGES;

@WebServlet(IMAGES + "/*")
public class ImageServlet extends HttpServlet {

    private final ImageService imageService = ImageService.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String requestURI = req.getRequestURI();
        String imagePath = requestURI.replace("/images", "");

        imageService.get(imagePath)
                .ifPresentOrElse(image -> {
                            resp.setContentType("application/octet-stream");
                            writeImage(image, resp);
                        },
                        () -> resp.setStatus(404));
    }

    @SneakyThrows
    private void writeImage(InputStream image, HttpServletResponse resp) {
        try (image; OutputStream outputStream = resp.getOutputStream()) {
            int currentByte;
            while ((currentByte = image.read()) != -1) {
                outputStream.write(currentByte);
            }
        }
    }
}
