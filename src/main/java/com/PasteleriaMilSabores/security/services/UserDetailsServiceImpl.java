// src/main/java/com/PasteleriaMilSabores/security/services/UserDetailsServiceImpl.java

package com.PasteleriaMilSabores.security.services;

import com.PasteleriaMilSabores.model.Usuario;
import com.PasteleriaMilSabores.repository.UsuarioRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;

    public UserDetailsServiceImpl(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    // 💡 Método de Spring Security: Cargar usuario por su 'username' (que es el email)
    @Override
    @Transactional
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepository.findByEmail(email)
            .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado con email: " + email));

        // Convierte la Entidad Usuario al formato de Spring Security (UserDetailsImpl)
        return UserDetailsImpl.build(usuario);
    }
}