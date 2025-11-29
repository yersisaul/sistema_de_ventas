package com.integrador.sistema_de_ventas.springboot_app.config;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MvcConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Esto expone la carpeta "uploads" al navegador
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:uploads/");
    }
}