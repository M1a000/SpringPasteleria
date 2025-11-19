// src/main/java/com/PasteleriaMilSabores/model/DetalleBoleta.java

package com.PasteleriaMilSabores.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "DETALLE_BOLETA")
@Data
@NoArgsConstructor
public class DetalleBoleta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Relación ManyToOne: Muchos detalles pertenecen a UNA Boleta
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "boleta_id", nullable = false)
    private Boleta boleta;

    // Relación ManyToOne: El detalle apunta al Producto que se compró
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "producto_id", nullable = false)
    private Producto producto;

    @Column(nullable = false)
    private Integer cantidad;

    // 🚨 Precio unitario al momento de la compra (CRUCIAL para no depender del precio actual del Producto)
    @Column(nullable = false)
    private Double precioUnitario; 

    @Column(nullable = false)
    private Double subtotal; // cantidad * precioUnitario
}