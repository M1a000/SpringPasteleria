// src/main/java/com/PasteleriaMilSabores/controller/ProductoController.java

package com.PasteleriaMilSabores.controller;

import com.PasteleriaMilSabores.model.Producto;
import com.PasteleriaMilSabores.repository.ProductoRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize; // 👈 ¡CLAVE para la seguridad por método!
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/productos")
public class ProductoController {

    private final ProductoRepository productoRepository;

    public ProductoController(ProductoRepository productoRepository) {
        this.productoRepository = productoRepository;
    }

    // --------------------------------------------------------------------------------
    // 1. LECTURA (Pública - Accesible por cualquiera, incluso no autenticado)
    // --------------------------------------------------------------------------------
    @GetMapping
    public List<Producto> getAllProductos() {
        return productoRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Producto> getProductoById(@PathVariable Long id) {
        Optional<Producto> producto = productoRepository.findById(id);
        return producto.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }
    
    // --------------------------------------------------------------------------------
    // 2. LECTURA DE INFORMACIÓN SENSIBLE (Vendedor o Administrador)
    // --------------------------------------------------------------------------------
    // Solo permite el acceso si el usuario tiene el rol ADMINISTRADOR o VENDEDOR
    @GetMapping("/stock")
    @PreAuthorize("hasAuthority('ADMINISTRADOR') or hasAuthority('VENDEDOR')")
    public List<Producto> getProductosConStock() {
        // En una app real, este método podría devolver un DTO más detallado que incluye stock.
        return productoRepository.findAll();
    }


    // --------------------------------------------------------------------------------
    // 3. CREACIÓN (Solo Administrador)
    // --------------------------------------------------------------------------------
    // Solo permite el acceso si el usuario tiene el rol ADMINISTRADOR
    @PostMapping
    @PreAuthorize("hasAuthority('ADMINISTRADOR')") 
    public Producto createProducto(@RequestBody Producto producto) {
        return productoRepository.save(producto);
    }

    // --------------------------------------------------------------------------------
    // 4. ACTUALIZACIÓN (Solo Administrador)
    // --------------------------------------------------------------------------------
    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMINISTRADOR')")
    public ResponseEntity<Producto> updateProducto(@PathVariable Long id, @RequestBody Producto productoDetails) {
        return productoRepository.findById(id)
            .map(producto -> {
                // Actualizar campos
                producto.setNombre(productoDetails.getNombre());
                producto.setDescripcion(productoDetails.getDescripcion());
                producto.setPrecio(productoDetails.getPrecio());
                producto.setStock(productoDetails.getStock());
                producto.setCategoria(productoDetails.getCategoria());
                return ResponseEntity.ok(productoRepository.save(producto));
            }).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // --------------------------------------------------------------------------------
    // 5. ELIMINACIÓN (Solo Administrador)
    // --------------------------------------------------------------------------------
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMINISTRADOR')")
    public ResponseEntity<Void> deleteProducto(@PathVariable Long id) {
        if (productoRepository.existsById(id)) {
            productoRepository.deleteById(id);
            return ResponseEntity.noContent().build(); // 204 No Content
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}