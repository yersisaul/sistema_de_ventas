package com.integrador.sistema_de_ventas.springboot_app.controllers.admin;

import com.integrador.sistema_de_ventas.springboot_app.dto.PedidoDTO;
import com.integrador.sistema_de_ventas.springboot_app.models.Pedido;
import com.integrador.sistema_de_ventas.springboot_app.services.PedidoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
            List<PedidoDTO> pedidoDTOs = pedidos.stream().map(pedido -> {
                PedidoDTO dto = new PedidoDTO();
                dto.pedidoToDTO(pedido);
                return dto;
            }).toList();

            return ResponseEntity.ok(pedidoDTOs);
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
                PedidoDTO dto = new PedidoDTO();
                dto.pedidoToDTO(pedido.get());
                return ResponseEntity.ok(dto);
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Pedido no encontrado");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());
        }
    }

    // ... imports y otros métodos ...

    @PutMapping("/{id}/estado")
    public ResponseEntity<?> actualizarEstadoPedido(@PathVariable Long id, @RequestParam String nuevoEstado) {
        try {
            // Llama a tu servicio (asegúrate que PedidoService tenga este método)
            pedidoService.actualizarEstadoPedido(id, nuevoEstado);
            return ResponseEntity.ok("Estado actualizado correctamente");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("Error: " + e.getMessage());
        }
    }
    
    // He simplificado el POST para que no dependas de PedidoCreateDTO si no lo tienes aun.
    // La creación de ventas se hace mejor desde VentaApiController (con carrito).
}