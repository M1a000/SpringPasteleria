// src/main/java/com/PasteleriaMilSabores/security/jwt/AuthEntryPointJwt.java

package com.PasteleriaMilSabores.security.jwt;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

@Component
public class AuthEntryPointJwt implements AuthenticationEntryPoint {

    private static final Logger logger = LoggerFactory.getLogger(AuthEntryPointJwt.class);

    // Se llama cuando un usuario no autenticado intenta acceder a un recurso protegido
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException, ServletException {
        
        logger.error("Error de acceso no autorizado: {}", authException.getMessage());
        
        // Devuelve un 401 Unauthorized al cliente de React (el token es inválido/faltante)
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Error: Acceso no autorizado / Token JWT inválido");
    }
}