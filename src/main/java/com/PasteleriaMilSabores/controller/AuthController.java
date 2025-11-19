// src/main/java/com/PasteleriaMilSabores/controller/AuthController.java

package com.PasteleriaMilSabores.controller;

import com.PasteleriaMilSabores.model.Usuario;
import com.PasteleriaMilSabores.model.enums.Rol;
import com.PasteleriaMilSabores.repository.UsuarioRepository;
import com.PasteleriaMilSabores.security.jwt.JwtUtils;
import com.PasteleriaMilSabores.security.services.UserDetailsImpl;

import com.PasteleriaMilSabores.payload.request.LoginRequest;
import com.PasteleriaMilSabores.payload.request.RegisterRequest;
import com.PasteleriaMilSabores.payload.response.JwtResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
// ⚠️ El CORS se maneja globalmente en SecurityConfig, no es necesario aquí.
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    // Inyección de dependencias (constructor-based injection)
    public AuthController(AuthenticationManager authenticationManager, 
                          JwtUtils jwtUtils, 
                          UsuarioRepository usuarioRepository, 
                          PasswordEncoder passwordEncoder) {
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // ----------------------------------------------------
    // 🔑 ENDPOINT: LOGIN (POST /api/auth/login)
    // ----------------------------------------------------
    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest) {
        
        // 1. Autenticar el usuario usando el AuthenticationManager y el DaoAuthenticationProvider configurado
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));

        // 2. Establecer la autenticación en el contexto de seguridad
        SecurityContextHolder.getContext().setAuthentication(authentication);
        
        // 3. Generar el JWT
        String jwt = jwtUtils.generateJwtToken(authentication);
        
        // 4. Obtener los detalles del usuario logueado
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        
        // 5. Obtener el Rol (Solo necesitamos el primer rol, ya que solo manejan 1)
        String rol = userDetails.getAuthorities().stream()
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Rol no encontrado"))
                .getAuthority();

        // 6. Crear la respuesta JSON (JwtResponse) para el Frontend de React
        return ResponseEntity.ok(new JwtResponse(
            jwt,
            userDetails.getId(),
            userDetails.getNombreUsuario(),
            userDetails.getUsername(), // Email
            rol // El rol como String (ej: "ADMINISTRADOR")
        ));
    }

    // ----------------------------------------------------
    // 📝 ENDPOINT: REGISTRO (POST /api/auth/register)
    // ----------------------------------------------------
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody RegisterRequest signUpRequest) {
        
        // 1. Validar si el email ya existe
        if (usuarioRepository.existsByEmail(signUpRequest.getEmail())) {
            return new ResponseEntity<>("Error: ¡El Email ya está en uso!", HttpStatus.BAD_REQUEST);
        }

        // 2. Crear y configurar la nueva cuenta
        Usuario usuario = new Usuario();
        usuario.setNombre(signUpRequest.getNombre());
        usuario.setApellido(signUpRequest.getApellido());
        usuario.setEmail(signUpRequest.getEmail());
        usuario.setDireccion(signUpRequest.getDireccion());
        usuario.setTelefono(signUpRequest.getTelefono());
        
        // 3. 🚨 Hashear la contraseña y asignar el Rol por defecto
        usuario.setPassword(passwordEncoder.encode(signUpRequest.getPassword()));
        usuario.setRol(Rol.CLIENTE); 

        // 4. Guardar el nuevo usuario
        usuarioRepository.save(usuario);

        // 5. Devolver respuesta de éxito
        return new ResponseEntity<>("Usuario registrado exitosamente como CLIENTE.", HttpStatus.CREATED);
    }
}