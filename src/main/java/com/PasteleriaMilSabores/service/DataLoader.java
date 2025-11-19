// src/main/java/com/PasteleriaMilSabores/service/DataLoader.java

package com.PasteleriaMilSabores.service;

import com.PasteleriaMilSabores.model.Usuario;
import com.PasteleriaMilSabores.model.enums.Rol;
import com.PasteleriaMilSabores.repository.UsuarioRepository;
import org.springframework.boot.CommandLineRunner; // 👈 Interfaz clave
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataLoader implements CommandLineRunner {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder; // Inyectamos el componente para hashear

    public DataLoader(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // El método run se ejecuta justo después de que la aplicación arranca
    @Override
    public void run(String... args) throws Exception {
        
        // 1. Verificar si ya existe un usuario administrador
        if (!usuarioRepository.existsByEmail("admin@pasteleria.cl")) {
            System.out.println("Creando usuario ADMIN inicial...");

            // 2. Crear un usuario inicial de tipo ADMINISTRADOR
            Usuario admin = new Usuario();
            admin.setNombre("Admin");
            admin.setApellido("Sistema");
            admin.setEmail("admin@pasteleria.cl");
            
            // ⚠️ CRÍTICO: Hashear la contraseña antes de guardarla
            admin.setPassword(passwordEncoder.encode("123456")); 
            
            admin.setRol(Rol.ADMINISTRADOR);
            admin.setDireccion("Oficina Central");
            admin.setTelefono("987654321");

            // 3. Guardar en la base de datos
            usuarioRepository.save(admin);
            System.out.println("Usuario ADMIN creado. Credenciales: admin@pasteleria.cl / 123456");
        }

        // Opcional: Crear un usuario VENDEDOR y CLIENTE para pruebas de roles
        if (!usuarioRepository.existsByEmail("vendedor@pasteleria.cl")) {
             Usuario vendedor = new Usuario();
             vendedor.setNombre("Vendedor");
             vendedor.setApellido("Ventas");
             vendedor.setEmail("vendedor@pasteleria.cl");
             vendedor.setPassword(passwordEncoder.encode("vender123"));
             vendedor.setRol(Rol.VENDEDOR);
             usuarioRepository.save(vendedor);
        }
        
        if (!usuarioRepository.existsByEmail("cliente@pasteleria.cl")) {
             Usuario cliente = new Usuario();
             cliente.setNombre("Cliente");
             cliente.setApellido("Fiel");
             cliente.setEmail("cliente@pasteleria.cl");
             cliente.setPassword(passwordEncoder.encode("cliente123"));
             cliente.setRol(Rol.CLIENTE);
             usuarioRepository.save(cliente);
        }
    }
}