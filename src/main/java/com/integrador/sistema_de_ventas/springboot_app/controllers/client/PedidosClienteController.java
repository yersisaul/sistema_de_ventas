package com.integrador.sistema_de_ventas.springboot_app.controllers.client;

import com.integrador.sistema_de_ventas.springboot_app.dto.CrearPedidoRequest;
import com.integrador.sistema_de_ventas.springboot_app.models.Pedido;
import com.integrador.sistema_de_ventas.springboot_app.models.Usuario;
import com.integrador.sistema_de_ventas.springboot_app.services.PedidoService;
import com.integrador.sistema_de_ventas.springboot_app.services.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/client/pedidos")
@CrossOrigin(origins = "*")
public class PedidosClienteController {
    
    @Autowired
    private PedidoService pedidoService;
    
    @Autowired
    private UsuarioService usuarioService;
    
    @PostMapping
    public ResponseEntity<?> crearPedido(@RequestBody CrearPedidoRequest request) {
        try {
            Optional<Usuario> cliente = usuarioService.obtenerUsuarioPorId(request.getClienteId());
            if (cliente.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Cliente no encontrado");
            }
            
            Pedido pedido = new Pedido();
            pedido.setCliente(cliente.get());
            pedido.setDireccionEnvio(request.getDireccionEnvio());
            pedido.setTelefonoContacto(request.getTelefonoContacto());
            pedido.setNotas(request.getNotas());
            pedido.setSubtotal(request.getSubtotal());
            pedido.setImpuestos(request.getImpuestos());
            pedido.setCostoEnvio(request.getCostoEnvio());
            pedido.setTotal(request.getTotal());
            
            Pedido nuevoPedido = pedidoService.crearPedido(pedido);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevoPedido);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("Error: " + e.getMessage());
        }
    }
    
    @GetMapping("/cliente/{clienteId}")
    public ResponseEntity<?> obtenerPedidosDelCliente(@PathVariable Long clienteId) {
        try {
            List<Pedido> pedidos = pedidoService.obtenerPedidosPorCliente(clienteId);
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
    
    @GetMapping("/{id}/estado")
    public ResponseEntity<?> obtenerEstadoPedido(@PathVariable Long id) {
        try {
            Optional<Pedido> pedido = pedidoService.obtenerPedidoPorId(id);
            if (pedido.isPresent()) {
                return ResponseEntity.ok(pedido.get().getEstado());
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("Pedido no encontrado");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error: " + e.getMessage());
        }
    }
    
    @PutMapping("/{id}/cancelar")
    public ResponseEntity<?> cancelarPedido(@PathVariable Long id) {
        try {
            pedidoService.cancelarPedido(id);
            return ResponseEntity.ok("Pedido cancelado exitosamente");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("Error: " + e.getMessage());
        }
    }
}
