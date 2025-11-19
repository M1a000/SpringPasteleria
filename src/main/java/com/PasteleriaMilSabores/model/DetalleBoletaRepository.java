// src/main/java/com/PasteleriaMilSabores/repository/DetalleBoletaRepository.java

package com.PasteleriaMilSabores.repository;

import com.PasteleriaMilSabores.model.DetalleBoleta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DetalleBoletaRepository extends JpaRepository<DetalleBoleta, Long> {
    // No se necesita lógica adicional aquí por ahora
}