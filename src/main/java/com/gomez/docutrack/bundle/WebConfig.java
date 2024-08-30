package com.gomez.docutrack.bundle;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Paths;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Determine the root location path dynamically
        String uploadsPath = Paths.get(System.getProperty("user.home"), "uploads", "docs").toUri().toString();

        registry.addResourceHandler("/uploads/docs/**")
                .addResourceLocations(uploadsPath)
                .setCachePeriod(0);
    }
}
