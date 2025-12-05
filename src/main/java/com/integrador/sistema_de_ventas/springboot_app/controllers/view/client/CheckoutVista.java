package com.integrador.sistema_de_ventas.springboot_app.controllers.view.client;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import com.integrador.sistema_de_ventas.springboot_app.dto.PedidoCreateDTO;
import com.integrador.sistema_de_ventas.springboot_app.models.Pedido;
import com.integrador.sistema_de_ventas.springboot_app.models.Usuario;
import com.integrador.sistema_de_ventas.springboot_app.repository.UsuarioRepository;
import com.integrador.sistema_de_ventas.springboot_app.services.impl.PedidoServiceImpl;

@Controller
public class CheckoutVista {

    @Autowired
    private PedidoServiceImpl pedidoService;
    
    @Autowired
    private UsuarioRepository usuarioRepository; // Para obtener el ID del usuario logueado

    // 1. Mostrar la VISTA de Checkout
    @GetMapping("/checkout")
    public String mostrarCheckout(Model model) {
        // Aquí podrías pasar datos del usuario si quieres pre-rellenar el formulario
        return "client/checkout"; 
    }

    // 2. Procesar la compra (Recibe JSON desde el JS)
    @PostMapping("/api/pedidos/crear")
    @ResponseBody
    public ResponseEntity<?> procesarPedido(@RequestBody PedidoCreateDTO pedidoDTO, Authentication auth) {
        try {
            // Obtener el usuario logueado
            String username = auth.getName();
            Usuario usuario = usuarioRepository.findBynIdentificacion(username)
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

            // Asignar el ID del cliente al DTO (Seguridad: no confiar solo en lo que viene del front)
            pedidoDTO.setClienteId(usuario.getId());

            // Crear el pedido
            Pedido nuevoPedido = pedidoService.crearPedido(pedidoDTO);

            return ResponseEntity.ok(Map.of("mensaje", "Pedido creado con éxito", "id", nuevoPedido.getId()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}