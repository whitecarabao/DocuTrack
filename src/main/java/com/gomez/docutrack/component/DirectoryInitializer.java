package com.gomez.docutrack.component;

import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.IOException;

@Component
public class DirectoryInitializer {

    private final Path rootLocation = Paths.get(System.getProperty("user.home"), "uploads", "docs");

    @PostConstruct
    public void init() {
        try {
            // Create directories if they do not exist
            if (!Files.exists(rootLocation)) {
                Files.createDirectories(rootLocation);
                System.out.println("Created directories: " + rootLocation);
            }
        } catch (IOException e) {
            throw new RuntimeException("Could not initialize storage directories", e);
        }
    }
}
