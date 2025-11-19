// src/main/java/com/PasteleriaMilSabores/payload/request/ItemBoletaRequest.java

package com.PasteleriaMilSabores.payload.request;

import lombok.Data;

@Data
public class ItemBoletaRequest {
    private Long productoId;
    private Integer cantidad;
    // No necesitamos enviar el precio aquí, lo obtendremos del Producto en el Backend
}