package com.integrador.sistema_de_ventas.springboot_app.controllers.client;

import com.integrador.sistema_de_ventas.springboot_app.models.Producto;
import com.integrador.sistema_de_ventas.springboot_app.models.VarianteProducto;
import com.integrador.sistema_de_ventas.springboot_app.services.ProductoService;
import com.integrador.sistema_de_ventas.springboot_app.services.VarianteProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/client/productos")
@CrossOrigin(origins = "*")
public class ProductosController {
    
    @Autowired
    private ProductoService productoService;
    
    @Autowired
    private VarianteProductoService varianteService;
    
    @GetMapping
    public ResponseEntity<?> obtenerProductosActivos() {
        try {
            List<Producto> productos = productoService.obtenerProductosActivos();
            return ResponseEntity.ok(productos);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error: " + e.getMessage());
        }
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerProductoPorId(@PathVariable Long id) {
        try {
            Optional<Producto> producto = productoService.obtenerProductoPorId(id);
            if (producto.isPresent() && producto.get().getActivo()) {
                return ResponseEntity.ok(producto.get());
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("Producto no encontrado");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error: " + e.getMessage());
        }
    }
    
    @GetMapping("/categoria/{categoriaId}")
    public ResponseEntity<?> obtenerProductosPorCategoria(@PathVariable Long categoriaId) {
        try {
            List<Producto> productos = productoService.obtenerProductosPorCategoria(categoriaId);
            return ResponseEntity.ok(productos);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error: " + e.getMessage());
        }
    }
    
    @GetMapping("/buscar")
    public ResponseEntity<?> buscarProductos(@RequestParam String nombre) {
        try {
            List<Producto> productos = productoService.buscarProductosPorNombre(nombre);
            return ResponseEntity.ok(productos);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error: " + e.getMessage());
        }
    }
    
    @GetMapping("/{productoId}/variantes")
    public ResponseEntity<?> obtenerVariantesPorProducto(@PathVariable Long productoId) {
        try {
            List<VarianteProducto> variantes = varianteService.obtenerVariantesPorProducto(productoId);
            return ResponseEntity.ok(variantes);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error: " + e.getMessage());
        }
    }
    
    @GetMapping("/{productoId}/variantes/stock")
    public ResponseEntity<?> obtenerVariantesConStock(@PathVariable Long productoId) {
        try {
            List<VarianteProducto> variantes = varianteService.obtenerVariantesConStock(productoId);
            return ResponseEntity.ok(variantes);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error: " + e.getMessage());
        }
    }
}
