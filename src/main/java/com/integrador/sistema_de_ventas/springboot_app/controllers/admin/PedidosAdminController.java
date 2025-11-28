package com.integrador.sistema_de_ventas.springboot_app.controllers.admin;

import com.integrador.sistema_de_ventas.springboot_app.models.Pedido;
import com.integrador.sistema_de_ventas.springboot_app.services.PedidoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/admin/pedidos")
@CrossOrigin(origins = "*")
public class PedidosAdminController {
    
    @Autowired
    private PedidoService pedidoService;
    
    @GetMapping
    public ResponseEntity<?> obtenerTodosPedidos() {
        try {
            List<Pedido> pedidos = pedidoService.obtenerTodosPedidos();
            return ResponseEntity.ok(pedidos);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error: " + e.getMessage());
        }
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerPedidoPorId(@PathVariable Long id) {
        try {
            Optional<Pedido> pedido = pedidoService.obtenerPedidoPorId(id);
            if (pedido.isPresent()) {
                return ResponseEntity.ok(pedido.get());
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("Pedido no encontrado");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error: " + e.getMessage());
        }
    }
    
    @GetMapping("/estado/{estado}")
    public ResponseEntity<?> obtenerPedidosPorEstado(@PathVariable String estado) {
        try {
            List<Pedido> pedidos = pedidoService.obtenerPedidosPorEstado(estado);
            return ResponseEntity.ok(pedidos);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error: " + e.getMessage());
        }
    }
    
    @PutMapping("/{id}/estado")
    public ResponseEntity<?> actualizarEstadoPedido(@PathVariable Long id, @RequestParam String nuevoEstado) {
        try {
            Pedido pedidoActualizado = pedidoService.actualizarEstadoPedido(id, nuevoEstado);
            return ResponseEntity.ok(pedidoActualizado);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("Error: " + e.getMessage());
        }
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarPedido(@PathVariable Long id, @RequestBody Pedido pedido) {
        try {
            Pedido pedidoActualizado = pedidoService.actualizarPedido(id, pedido);
            return ResponseEntity.ok(pedidoActualizado);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("Error: " + e.getMessage());
        }
    }
    
    @GetMapping("/fecha/rango")
    public ResponseEntity<?> obtenerPedidosPorFecha(
        @RequestParam LocalDateTime fechaInicio,
        @RequestParam LocalDateTime fechaFin) {
        try {
            List<Pedido> pedidos = pedidoService.obtenerPedidosPorFecha(fechaInicio, fechaFin);
            return ResponseEntity.ok(pedidos);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error: " + e.getMessage());
        }
    }
}
