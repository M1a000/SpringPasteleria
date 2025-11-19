// src/main/java/com/PasteleriaMilSabores/config/CorsConfig.java

package com.PasteleriaMilSabores.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        // Permite todas las solicitudes desde tu frontend de React
        registry.addMapping("/api/**") // Aplica CORS a todos los endpoints de tu API
                .allowedOrigins("http://localhost:5173", "http://127.0.0.1:5173") // ⚠️ Asegúrate que el puerto de React sea el correcto
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true);
    }
}