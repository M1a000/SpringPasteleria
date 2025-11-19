// src/main/java/com/PasteleriaMilSabores/controller/BoletaController.java

package com.PasteleriaMilSabores.controller;

import com.PasteleriaMilSabores.model.Boleta;
import com.PasteleriaMilSabores.model.DetalleBoleta;
import com.PasteleriaMilSabores.model.Producto;
import com.PasteleriaMilSabores.model.Usuario;
import com.PasteleriaMilSabores.repository.BoletaRepository;
import com.PasteleriaMilSabores.repository.ProductoRepository;
import com.PasteleriaMilSabores.repository.UsuarioRepository;
import com.PasteleriaMilSabores.security.services.UserDetailsImpl; // Para obtener el ID del usuario logueado
import com.PasteleriaMilSabores.payload.request.CrearBoletaRequest;
import com.PasteleriaMilSabores.payload.request.ItemBoletaRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/boletas")
public class BoletaController {

    private final BoletaRepository boletaRepository;
    private final ProductoRepository productoRepository;
    private final UsuarioRepository usuarioRepository;

    public BoletaController(BoletaRepository boletaRepository, 
                            ProductoRepository productoRepository,
                            UsuarioRepository usuarioRepository) {
        this.boletaRepository = boletaRepository;
        this.productoRepository = productoRepository;
        this.usuarioRepository = usuarioRepository;
    }

    // ----------------------------------------------------
    // 1. CREAR ORDEN (Solo para usuarios Autenticados: Clientes, Vendedores o Admin)
    // ----------------------------------------------------
    @PostMapping
    @PreAuthorize("isAuthenticated()") // Permite a cualquier usuario logueado
    public ResponseEntity<Boleta> crearBoleta(@RequestBody CrearBoletaRequest request) {
        
        // --- 1. Obtener el ID del Usuario Logueado (CRÍTICO) ---
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        Long usuarioId = userDetails.getId();

        // Obtener la entidad Usuario
        Usuario usuario = usuarioRepository.findById(usuarioId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado"));

        // --- 2. Crear la Boleta (Encabezado) ---
        Boleta boleta = new Boleta();
        boleta.setUsuario(usuario);
        boleta.setDireccionEntrega(request.getDireccionEntrega());
        boleta.setEstado("PENDIENTE"); // Estado inicial
        
        // --- 3. Procesar los Ítems (Detalles) ---
        List<DetalleBoleta> detalles = new ArrayList<>();
        double totalCalculado = 0.0;
        
        for (ItemBoletaRequest itemRequest : request.getItems()) {
            
            // Buscar el producto en la base de datos para obtener el precio actual
            Producto producto = productoRepository.findById(itemRequest.getProductoId())
                .orElseThrow(() -> new ResponseStatusException(
                    HttpStatus.NOT_FOUND, 
                    "Producto con ID " + itemRequest.getProductoId() + " no encontrado"
                ));
            
            // 🚨 Validación de Stock (Simple)
            if (producto.getStock() < itemRequest.getCantidad()) {
                 throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, 
                    "Stock insuficiente para el producto: " + producto.getNombre()
                );
            }
            
            // 4. Crear el Detalle
            DetalleBoleta detalle = new DetalleBoleta();
            detalle.setBoleta(boleta);
            detalle.setProducto(producto);
            detalle.setCantidad(itemRequest.getCantidad());
            detalle.setPrecioUnitario(producto.getPrecio()); // 🚨 Capturar el precio actual del producto
            detalle.setSubtotal(producto.getPrecio() * itemRequest.getCantidad());
            
            detalles.add(detalle);
            totalCalculado += detalle.getSubtotal();
            
            // 5. 🚨 Actualizar el Stock (CRÍTICO)
            producto.setStock(producto.getStock() - itemRequest.getCantidad());
            productoRepository.save(producto);
        }
        
        // 6. Finalizar la Boleta
        boleta.setDetalles(detalles);
        boleta.setTotal(totalCalculado); // Asignar el total CALCULADO en el Backend

        // 🚨 Opcional: Validar que el totalCalculado sea cercano al total enviado por el Frontend para evitar manipulaciones.

        Boleta boletaGuardada = boletaRepository.save(boleta);
        
        return new ResponseEntity<>(boletaGuardada, HttpStatus.CREATED);
    }
    
    // ----------------------------------------------------
    // 2. VER HISTORIAL DE COMPRAS (Solo Cliente)
    // ----------------------------------------------------
    @GetMapping("/mis-compras")
    @PreAuthorize("hasAuthority('CLIENTE') or hasAuthority('ADMINISTRADOR')")
    public List<Boleta> getMisBoletas() {
        // --- Obtener el ID del Usuario Logueado ---
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        Long usuarioId = userDetails.getId();

        return boletaRepository.findByUsuarioId(usuarioId);
    }

    // ----------------------------------------------------
    // 3. GESTIÓN DE ÓRDENES (Solo Admin y Vendedor)
    // ----------------------------------------------------
    @GetMapping
    @PreAuthorize("hasAuthority('ADMINISTRADOR') or hasAuthority('VENDEDOR')")
    public List<Boleta> getAllBoletas() {
        return boletaRepository.findAll();
    }
    
    // Aquí puedes agregar un @PutMapping("/estado/{id}") para que el Vendedor o Admin cambie el estado de la boleta
}