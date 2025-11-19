// src/main/java/com/PasteleriaMilSabores/payload/request/RegisterRequest.java

package com.PasteleriaMilSabores.payload.request;

import lombok.Data;

// Este DTO recibe todos los datos del formulario de registro de React
@Data
public class RegisterRequest {
    private String nombre;
    private String apellido;
    private String email;
    private String password;
    private String direccion;
    private String telefono;
    // No necesitamos el 'rol' aquí, ya que Spring Boot lo asignará por defecto a CLIENTE.
}