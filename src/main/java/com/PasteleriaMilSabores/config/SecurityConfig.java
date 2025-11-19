// src/main/java/com/PasteleriaMilSabores/config/SecurityConfig.java

package com.PasteleriaMilSabores.config;

import com.PasteleriaMilSabores.security.jwt.AuthEntryPointJwt;
import com.PasteleriaMilSabores.security.jwt.AuthTokenFilter;
import com.PasteleriaMilSabores.security.services.UserDetailsServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableMethodSecurity // Permite usar @PreAuthorize y @PostAuthorize en tus controladores
public class SecurityConfig {

    private final UserDetailsServiceImpl userDetailsService;
    private final AuthEntryPointJwt unauthorizedHandler;

    public SecurityConfig(UserDetailsServiceImpl userDetailsService, AuthEntryPointJwt unauthorizedHandler) {
        this.userDetailsService = userDetailsService;
        this.unauthorizedHandler = unauthorizedHandler;
    }

    // Bean para el filtro que procesará el JWT en cada solicitud
    @Bean
    public AuthTokenFilter authenticationJwtTokenFilter() {
        return new AuthTokenFilter();
    }
    
    // Configura el proveedor de autenticación para usar tu UserDetailsServiceImpl y el PasswordEncoder
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    // Bean para el Administrador de Autenticación (usado en el LoginController)
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    // Bean para el hash de la contraseña (BCrypt es el estándar recomendado)
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // Configuración de las reglas de seguridad
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable()) // Deshabilitar CSRF (común en APIs REST)
            .exceptionHandling(exception -> exception.authenticationEntryPoint(unauthorizedHandler)) // Manejo de errores 401
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // No usar sesiones (JWT es STATELESS)
            
            // Reglas de autorización de las rutas
            .authorizeHttpRequests(auth -> 
                auth.requestMatchers("/api/auth/**").permitAll() // 💡 Permite acceso a /api/auth/login y /api/auth/register
                    .requestMatchers("/api/productos/**").permitAll() // 💡 Permite acceso al catálogo (ajustar luego por métodos)
                    .requestMatchers("/api/test/**").permitAll() // Rutas de prueba
                    .anyRequest().authenticated() // Cualquier otra ruta requiere autenticación (JWT)
            );
        
        http.authenticationProvider(authenticationProvider());
        http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}