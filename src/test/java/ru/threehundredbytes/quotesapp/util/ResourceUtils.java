package ru.threehundredbytes.quotesapp.util;

import lombok.SneakyThrows;
import org.springframework.core.io.ClassPathResource;

import java.nio.file.Files;
import java.nio.file.Path;

public class ResourceUtils {
    private ResourceUtils() {

    }

    @SneakyThrows
    public static String getResourceFileAsString(String filename) {
        Path filePath = new ClassPathResource(filename).getFile().toPath();

        return new String(Files.readAllBytes(filePath));
    }
}
