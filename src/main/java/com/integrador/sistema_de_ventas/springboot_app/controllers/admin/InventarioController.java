package com.integrador.sistema_de_ventas.springboot_app.controllers.admin;

import com.integrador.sistema_de_ventas.springboot_app.dto.VarianteDTO;
import com.integrador.sistema_de_ventas.springboot_app.dto.VarianteUpdateDTO;
import com.integrador.sistema_de_ventas.springboot_app.models.VarianteProducto;
import com.integrador.sistema_de_ventas.springboot_app.services.VarianteProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/admin/inventario")
@CrossOrigin(origins = "*")
public class InventarioController {
    @Autowired
    private VarianteProductoService varianteService;
    
    @GetMapping("/variantes")
    public ResponseEntity<?> obtenerTodasLasVariantes() { // Obtener todas las variantes de producto
        try {
            List<VarianteProducto> variantes = varianteService.obtenerTodasLasVariantes(); // Ajusta según tu lógica para obtener todas las variantes
            List<VarianteDTO> variantesDTO = variantes.stream()
                .map(VarianteDTO::fromVariante)
                .toList();
            
            return ResponseEntity.ok(variantesDTO);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error: " + e.getMessage());
        }
    }
    
    @GetMapping("/variantes/stock-bajo")
    public ResponseEntity<?> obtenerVariantesStockBajo() {
        try {
            List<VarianteProducto> variantes = varianteService.obtenerVariantesStockBajo();
            return ResponseEntity.ok(variantes);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error: " + e.getMessage());
        }
    }
    
    @GetMapping("/variantes/{id}")
    public ResponseEntity<?> obtenerVariantePorId(@PathVariable Long id) {
        try {
            Optional<VarianteProducto> variante = varianteService.obtenerVariantePorId(id);
            if (variante.isPresent()) {
                return ResponseEntity.ok(variante.get());
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("Variante no encontrada");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error: " + e.getMessage());
        }
    }
    
    @PutMapping("/variantes/{id}/stock")
    public ResponseEntity<?> actualizarStock(@PathVariable Long id, @RequestParam Integer cantidad) {
        try {
            varianteService.actualizarStock(id, cantidad);
            return ResponseEntity.ok("Stock actualizado exitosamente");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("Error: " + e.getMessage());
        }
    }
    
    @PutMapping("/variantes/{id}")
    public ResponseEntity<?> actualizarVariante(@PathVariable Long id, @RequestBody VarianteUpdateDTO varianteUpdateDTO) {
        try {
            VarianteProducto varianteActualizada = varianteService.actualizarVariante(id, varianteUpdateDTO);
            return ResponseEntity.ok("Variante actualizada exitosamente");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("Error: " + e.getMessage());
        }
    }
    
    @DeleteMapping("/variantes/{id}")
    public ResponseEntity<?> desactivarVariante(@PathVariable Long id) {
        try {
            varianteService.desactivarVariante(id);
            return ResponseEntity.ok("Variante desactivada exitosamente");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("Error: " + e.getMessage());
        }
    }
}
