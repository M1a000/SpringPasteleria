// src/main/java/com/PasteleriaMilSabores/model/enums/Rol.java

package com.PasteleriaMilSabores.model.enums;

public enum Rol {
    
    // Roles del sistema. Los nombres deben estar en mayúsculas por convención.
    ADMINISTRADOR,
    VENDEDOR,
    CLIENTE
    
    // Opcional: Si quieres un nombre más amigable para mostrar en el frontend,
    // puedes añadir un constructor y un campo:
    /*
    private final String nombreVisible;

    Rol(String nombreVisible) {
        this.nombreVisible = nombreVisible;
    }

    public String getNombreVisible() {
        return nombreVisible;
    }
    */
}