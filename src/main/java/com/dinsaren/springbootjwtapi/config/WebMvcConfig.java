package com.dinsaren.springbootjwtapi.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Value("${upload.server.path}")
    private String uploadPath;

    @Value("${upload.client.path}")
    private String clientPath;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {

        String path = uploadPath;

        if (!path.endsWith("/") && !path.endsWith("\\")) {
            path += "/";
        }

        registry.addResourceHandler(clientPath)
                .addResourceLocations("file:" + path);
    }
}