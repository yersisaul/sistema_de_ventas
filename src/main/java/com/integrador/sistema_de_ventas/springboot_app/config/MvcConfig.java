package com.integrador.sistema_de_ventas.springboot_app.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MvcConfig implements WebMvcConfigurer {

    private final String uploadPath;

    public MvcConfig(@Value("${app.uploads.path:uploads/}") String uploadPath) {
        this.uploadPath = uploadPath.endsWith("/") ? uploadPath : uploadPath + "/";
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {

        String ruta = System.getProperty("user.dir") + "/uploads/";

        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:" + ruta);
    }
}
