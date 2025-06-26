package com.example.tiengtrungapp.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.beans.factory.annotation.Value;

@Configuration
public class VideoConfig implements WebMvcConfigurer {

    @Value("${app.video.storage.path:uploads/videos/}")
    private String videoStoragePath;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Serve video files statically
        registry.addResourceHandler("/videos/**")
                .addResourceLocations("file:" + videoStoragePath);

        // Serve image files
        registry.addResourceHandler("/images/**")
                .addResourceLocations("file:uploads/images/");
    }
}