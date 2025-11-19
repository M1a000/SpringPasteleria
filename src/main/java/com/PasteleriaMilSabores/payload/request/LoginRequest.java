// src/main/java/com/PasteleriaMilSabores/payload/request/LoginRequest.java

package com.PasteleriaMilSabores.payload.request;

import lombok.Data;

@Data
public class LoginRequest {
    private String email;
    private String password;
}