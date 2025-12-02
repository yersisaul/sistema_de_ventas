package com.integrador.sistema_de_ventas.springboot_app.controllers.admin;

import com.integrador.sistema_de_ventas.springboot_app.models.Producto;
import com.integrador.sistema_de_ventas.springboot_app.dto.ProductoCreateDTO;
import com.integrador.sistema_de_ventas.springboot_app.dto.ProductoResponseDTO;
import com.integrador.sistema_de_ventas.springboot_app.dto.ProductoUpdateDTO;
import com.integrador.sistema_de_ventas.springboot_app.dto.VarianteDTO;
import com.integrador.sistema_de_ventas.springboot_app.models.VarianteProducto;
import com.integrador.sistema_de_ventas.springboot_app.services.ProductoService;
import com.integrador.sistema_de_ventas.springboot_app.services.VarianteProductoService;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/admin/productos")
@CrossOrigin(origins = "*")
public class ProductosAdminController {
    
    @Autowired
    private ProductoService productoService;
    
    @Autowired
    private VarianteProductoService varianteService;  

    @PostMapping
    public ResponseEntity<?> crearProducto(@RequestBody @Valid ProductoCreateDTO productoDTO) {
        try {
            Producto nuevoProducto = productoService.crearProducto(productoDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(ProductoResponseDTO.fromProducto(nuevoProducto));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("Error al crear producto: " + e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarProducto(@PathVariable Long id, @RequestBody ProductoUpdateDTO producto) {
        try {
            Producto productoActualizado = productoService.actualizarProducto(id, producto);
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(ProductoResponseDTO.fromProducto(productoActualizado));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("Error: " + e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<?> obtenerTodosLosProductos() {
        try {
            List<Producto> productos = productoService.obtenerTodosLosProductos();
            List<ProductoResponseDTO> response = productos.stream().map(ProductoResponseDTO::fromProducto).collect(Collectors.toList());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error: " + e.getMessage());
        }
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerProductoPorId(@PathVariable Long id) {
        try {
            Optional<Producto> producto = productoService.obtenerProductoPorId(id);
            Optional<ProductoResponseDTO> productoDTO = producto.map(ProductoResponseDTO::fromProducto);
            if (producto.isPresent()) {
                return ResponseEntity.ok(productoDTO.get());
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("Producto no encontrado");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error: " + e.getMessage());
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<?> desactivarProducto(@PathVariable Long id) {
        try {
            productoService.desactivarProducto(id);
            return ResponseEntity.ok("Producto desactivado exitosamente");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("Error: " + e.getMessage());
        }
    }
    
    @PostMapping("/{productoId}/variantes")
    public ResponseEntity<?> crearVariante(@PathVariable Long productoId, @RequestBody VarianteDTO varianteDTO) {
        try {
            Optional<Producto> producto = productoService.obtenerProductoPorId(productoId);
            VarianteProducto nuevo_variante = new VarianteProducto();
            if (producto.isPresent()) {
                nuevo_variante.setProducto(producto.get());
                nuevo_variante.setTalla(varianteDTO.getTalla());
                nuevo_variante.setStock(varianteDTO.getStock());
                nuevo_variante.setPrecio(varianteDTO.getPrecio());
                nuevo_variante.setActivo(true);
                VarianteProducto nuevaVariante = varianteService.crearVariante(nuevo_variante);
                return ResponseEntity.status(HttpStatus.CREATED).body(VarianteDTO.fromVariante(nuevaVariante));
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("Producto no encontrado");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
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
}
