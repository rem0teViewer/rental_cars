package by.car.rental.services;

import by.car.rental.utils.PropertiesUtil;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

import static java.nio.file.StandardOpenOption.CREATE;
import static java.nio.file.StandardOpenOption.TRUNCATE_EXISTING;
import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
public class ImageService {
    private static final ImageService INSTANCE = new ImageService();

    private final String basePath = PropertiesUtil.get("upload.path");

    @SneakyThrows
    public Optional<InputStream> get(String imagePath) {
        Path imageFullPath = Path.of(basePath, imagePath);

        return Files.exists(imageFullPath)
                ? Optional.of(Files.newInputStream(imageFullPath))
                : Optional.empty();
    }

    @SneakyThrows
    public void upload(String imagePath, InputStream imageContent) {
        Path imageFullPath = Path.of(basePath, imagePath);
        try (imageContent) {
            Files.createDirectories(imageFullPath.getParent());
            Files.write(imageFullPath, imageContent.readAllBytes(), CREATE, TRUNCATE_EXISTING);
        }
    }

    public static ImageService getInstance() {
        return INSTANCE;
    }
}
