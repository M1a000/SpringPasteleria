// src/main/java/com/PasteleriaMilSabores/model/Producto.java

package com.PasteleriaMilSabores.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "PRODUCTO")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombre;

    private String descripcion;
    
    // Precio de venta (visible para todos)
    @Column(nullable = false)
    private Double precio; 

    // Cantidad en stock (información sensible, solo para Admin/Vendedor)
    private Integer stock; 

    @Column(nullable = false)
    private String categoria;
}