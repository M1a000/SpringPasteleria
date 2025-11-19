// src/main/java/com/PasteleriaMilSabores/model/Boleta.java

package com.PasteleriaMilSabores.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "BOLETA")
@Data
@NoArgsConstructor
public class Boleta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Relación ManyToOne: Una Boleta pertenece a UN Usuario
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    // Relación OneToMany: Una Boleta tiene muchos Detalles (productos)
    // Usamos CascadeType.ALL para que al guardar la Boleta, se guarden los detalles.
    @OneToMany(mappedBy = "boleta", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DetalleBoleta> detalles;

    @CreationTimestamp // Spring asigna automáticamente la fecha y hora de creación
    @Column(nullable = false, updatable = false)
    private LocalDateTime fechaCompra;

    @Column(nullable = false)
    private Double total; // Total pagado por la boleta

    @Column(nullable = false)
    private String estado; // Ej: PENDIENTE, EN_PREPARACION, ENVIADO, ENTREGADO

    private String direccionEntrega;
}