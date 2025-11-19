// src/main/java/com/PasteleriaMilSabores/payload/request/CrearBoletaRequest.java

package com.PasteleriaMilSabores.payload.request;

import lombok.Data;
import java.util.List;

@Data
public class CrearBoletaRequest {
    // 1. Información para la Boleta
    private String direccionEntrega;
    private Double total; // Se puede calcular en el Backend, pero se envía para validación
    
    // 2. Información para los Detalles de la Boleta (el carrito de compras)
    private List<ItemBoletaRequest> items;
}