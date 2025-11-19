// src/main/java/com/PasteleriaMilSabores/security/jwt/JwtUtils.java

package com.PasteleriaMilSabores.security.jwt;

import java.security.Key;
import java.util.Date;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import com.PasteleriaMilSabores.security.services.UserDetailsImpl;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtils {
    private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);

    // ⚠️ CRÍTICO: Estas variables deben ir en application.properties
    @Value("${jwt.secret}") // Ejemplo: jwt.secret=MiClaveSecretaSuperLargaYSegura1234567890
    private String jwtSecret;

    @Value("${jwt.expiration.ms}") // Ejemplo: jwt.expiration.ms=86400000 (24 horas)
    private int jwtExpirationMs;

    // --- Métodos de Generación ---
    public String generateJwtToken(Authentication authentication) {
        // Obtiene los detalles del usuario autenticado
        UserDetailsImpl userPrincipal = (UserDetailsImpl) authentication.getPrincipal();

        // 💡 Importante: Se podría añadir el ROL aquí como un claim.
        String rol = userPrincipal.getAuthorities().stream().findFirst().get().getAuthority();
        
        return Jwts.builder()
                .setSubject((userPrincipal.getUsername())) // El email
                .claim("rol", rol) // Añadimos el rol para evitar consultar la DB después
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
                .signWith(key(), SignatureAlgorithm.HS256)
                .compact();
    }

    private Key key() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
    }

    // --- Métodos de Validación y Extracción ---
    public String getEmailFromJwtToken(String token) {
        return Jwts.parserBuilder().setSigningKey(key()).build()
                .parseClaimsJws(token).getBody().getSubject(); // Retorna el email (Subject)
    }

    public boolean validateJwtToken(String authToken) {
        try {
            Jwts.parserBuilder().setSigningKey(key()).build().parse(authToken);
            return true;
        } catch (MalformedJwtException e) {
            logger.error("Token JWT inválido: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            logger.error("Token JWT expirado: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            logger.error("Token JWT no soportado: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            logger.error("Cadena de claims JWT vacía: {}", e.getMessage());
        }
        return false;
    }
}