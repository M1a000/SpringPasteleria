// src/main/java/com/PasteleriaMilSabores/repository/ProductoRepository.java

package com.PasteleriaMilSabores.repository;

import com.PasteleriaMilSabores.model.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {
    // Spring Data JPA ya proporciona los métodos save, findAll, findById, deleteById, etc.
}