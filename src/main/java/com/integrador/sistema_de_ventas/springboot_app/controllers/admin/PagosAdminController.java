package com.integrador.sistema_de_ventas.springboot_app.controllers.admin;

import com.integrador.sistema_de_ventas.springboot_app.models.Pago;
import com.integrador.sistema_de_ventas.springboot_app.services.PagoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/admin/pagos")
@CrossOrigin(origins = "*")
public class PagosAdminController {
    
    @Autowired
    private PagoService pagoService;
    
    @GetMapping
    public ResponseEntity<?> obtenerTodosPagos() {
        try {
            // Implementar en servicio si es necesario
            return ResponseEntity.ok("Endpoint para obtener todos los pagos");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error: " + e.getMessage());
        }
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerPagoPorId(@PathVariable Long id) {
        try {
            Optional<Pago> pago = pagoService.obtenerPagoPorId(id);
            if (pago.isPresent()) {
                return ResponseEntity.ok(pago.get());
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("Pago no encontrado");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error: " + e.getMessage());
        }
    }
    
    @GetMapping("/pedido/{pedidoId}")
    public ResponseEntity<?> obtenerPagosPorPedido(@PathVariable Long pedidoId) {
        try {
            List<Pago> pagos = pagoService.obtenerPagosPorPedido(pedidoId);
            return ResponseEntity.ok(pagos);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error: " + e.getMessage());
        }
    }
    
    @GetMapping("/estado/{estado}")
    public ResponseEntity<?> obtenerPagosPorEstado(@PathVariable String estado) {
        try {
            List<Pago> pagos = pagoService.obtenerPagosPorEstado(estado);
            return ResponseEntity.ok(pagos);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error: " + e.getMessage());
        }
    }
    
    @PutMapping("/{id}/confirmar")
    public ResponseEntity<?> confirmarPago(@PathVariable Long id) {
        try {
            Pago pagoConfirmado = pagoService.confirmarPago(id);
            return ResponseEntity.ok(pagoConfirmado);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("Error: " + e.getMessage());
        }
    }
    
    @PutMapping("/{id}/rechazar")
    public ResponseEntity<?> rechazarPago(@PathVariable Long id) {
        try {
            pagoService.rechazarPago(id);
            return ResponseEntity.ok("Pago rechazado exitosamente");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("Error: " + e.getMessage());
        }
    }
}
