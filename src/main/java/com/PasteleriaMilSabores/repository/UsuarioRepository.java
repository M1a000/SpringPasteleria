// src/main/java/com/PasteleriaMilSabores/repository/UsuarioRepository.java

package com.PasteleriaMilSabores.repository;

import com.PasteleriaMilSabores.model.Usuario;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    // 💡 Método CRÍTICO para Spring Security: buscar por email (el username)
    Optional<Usuario> findByEmail(String email);
    
    // Opcional: verificar si el email ya existe en el registro
    boolean existsByEmail(String email);
}