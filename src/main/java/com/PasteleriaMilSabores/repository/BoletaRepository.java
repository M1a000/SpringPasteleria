// src/main/java/com/PasteleriaMilSabores/repository/BoletaRepository.java

package com.PasteleriaMilSabores.repository;

import com.PasteleriaMilSabores.model.Boleta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BoletaRepository extends JpaRepository<Boleta, Long> {
    // Método para buscar boletas de un usuario específico (esencial para el perfil del cliente)
    List<Boleta> findByUsuarioId(Long usuarioId);
}