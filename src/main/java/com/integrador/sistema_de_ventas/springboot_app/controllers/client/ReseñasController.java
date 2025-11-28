package com.integrador.sistema_de_ventas.springboot_app.controllers.client;

import com.integrador.sistema_de_ventas.springboot_app.models.Reseña;
import com.integrador.sistema_de_ventas.springboot_app.services.ReseñaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/client/reseñas")
@CrossOrigin(origins = "*")
public class ReseñasController {
    
    @Autowired
    private ReseñaService reseñaService;
    
    @PostMapping
    public ResponseEntity<?> crearReseña(@RequestBody Reseña reseña) {
        try {
            Reseña nuevaReseña = reseñaService.crearReseña(reseña);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevaReseña);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("Error: " + e.getMessage());
        }
    }
    
    @GetMapping("/producto/{productoId}")
    public ResponseEntity<?> obtenerReseñasAprobadas(@PathVariable Long productoId) {
        try {
            List<Reseña> reseñas = reseñaService.obtenerReseñasAprobadas(productoId);
            return ResponseEntity.ok(reseñas);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error: " + e.getMessage());
        }
    }
    
    @GetMapping("/producto/{productoId}/calificacion-promedio")
    public ResponseEntity<?> obtenerCalificacionPromedio(@PathVariable Long productoId) {
        try {
            Double promedio = reseñaService.obtenerCalificacionPromedio(productoId);
            return ResponseEntity.ok(promedio);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error: " + e.getMessage());
        }
    }
    
    @GetMapping("/cliente/{clienteId}")
    public ResponseEntity<?> obtenerReseñasDelCliente(@PathVariable Long clienteId) {
        try {
            List<Reseña> reseñas = reseñaService.obtenerReseñasPorCliente(clienteId);
            return ResponseEntity.ok(reseñas);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error: " + e.getMessage());
        }
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarReseña(@PathVariable Long id, @RequestBody Reseña reseña) {
        try {
            Reseña reseñaActualizada = reseñaService.actualizarReseña(id, reseña);
            return ResponseEntity.ok(reseñaActualizada);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("Error: " + e.getMessage());
        }
    }
}
