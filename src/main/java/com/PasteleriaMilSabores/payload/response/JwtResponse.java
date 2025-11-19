// src/main/java/com/PasteleriaMilSabores/payload/response/JwtResponse.java

package com.PasteleriaMilSabores.payload.response;

import lombok.Data;

@Data
public class JwtResponse {
    private String token;
    private String type = "Bearer";
    private Long id;
    private String nombreUsuario;
    private String email;
    private String rol; // El rol que React usará para la autorización

    public JwtResponse(String accessToken, Long id, String nombreUsuario, String email, String rol) {
        this.token = accessToken;
        this.id = id;
        this.nombreUsuario = nombreUsuario;
        this.email = email;
        this.rol = rol;
    }
}