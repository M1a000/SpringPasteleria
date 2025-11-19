// src/main/java/com/PasteleriaMilSabores/model/Usuario.java

package com.PasteleriaMilSabores.model;

import com.PasteleriaMilSabores.model.enums.Rol; // Importamos el enum de Roles

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity 
@Table(name = "USUARIO") // Nombre de tabla en la base de datos
@Data // Anotación de Lombok para generar getters, setters, toString, etc.
@NoArgsConstructor // Constructor sin argumentos (necesario para JPA)
@AllArgsConstructor // Constructor con todos los argumentos
public class Usuario {

    // --- ID y Generación (Configuración para Oracle) ---
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "usuario_seq")
    @SequenceGenerator(name = "usuario_seq", sequenceName = "USUARIO_SEQ", allocationSize = 1)
    @Column(name = "ID_USUARIO")
    private Long id;

    // --- Campos de Información ---
    @Column(name = "NOMBRE", nullable = false)
    private String nombre;

    @Column(name = "APELLIDO")
    private String apellido;
    
    @Column(name = "EMAIL", unique = true, nullable = false)
    private String email; // Usado como username para el login

    @Column(name = "PASSWORD", nullable = false) 
    private String password; // Contraseña hasheada (BCrypt)
    
    // --- Rol (Crucial para Spring Security) ---
    @Enumerated(EnumType.STRING) // Guarda el nombre del enum (ej: "ADMINISTRADOR") como String en la DB
    @Column(name = "ROL", nullable = false)
    private Rol rol; // Usamos el tipo de dato enum Rol
    
    @Column(name = "DIRECCION")
    private String direccion;

    @Column(name = "TELEFONO")
    private String telefono;

    // --- Constructor alternativo para registro (sin ID y con rol por defecto) ---
    public Usuario(String nombre, String apellido, String email, String password, Rol rol, String direccion, String telefono) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.email = email;
        this.password = password;
        this.rol = rol;
        this.direccion = direccion;
        this.telefono = telefono;
    }
}