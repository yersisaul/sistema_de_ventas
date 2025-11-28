package com.integrador.sistema_de_ventas.springboot_app.controllers.admin;

import com.integrador.sistema_de_ventas.springboot_app.models.Usuario;
import com.integrador.sistema_de_ventas.springboot_app.services.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/admin/clientes")
@CrossOrigin(origins = "*")
public class ClientesController {
    
    @Autowired
    private UsuarioService usuarioService;
    
    @GetMapping("")
    public ResponseEntity<?> obtenerTodosLosClientes() {
        try {
            List<Usuario> clientes = usuarioService.obtenerUsuariosPorRol("CLIENTE");
            return ResponseEntity.ok(clientes);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error al obtener clientes: " + e.getMessage());
        }
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerClientePorId(@PathVariable Long id) {
        try {
            Optional<Usuario> cliente = usuarioService.obtenerUsuarioPorId(id);
            if (cliente.isPresent() && "CLIENTE".equals(cliente.get().getRol())) {
                return ResponseEntity.ok(cliente.get());
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("Cliente no encontrado");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error: " + e.getMessage());
        }
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarCliente(@PathVariable Long id, @RequestBody Usuario cliente) {
        try {
            Usuario clienteActualizado = usuarioService.actualizarUsuario(id, cliente);
            return ResponseEntity.ok(clienteActualizado);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("Error al actualizar cliente: " + e.getMessage());
        }
    }
    
    @PutMapping("/{id}/desactivar")
    public ResponseEntity<?> desactivarCliente(@PathVariable Long id) {
        try {
            usuarioService.desactivarUsuario(id);
            return ResponseEntity.ok("Cliente desactivado exitosamente");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("Error: " + e.getMessage());
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarCliente(@PathVariable Long id) {
        try {
            usuarioService.eliminarUsuario(id);
            return ResponseEntity.ok("Cliente eliminado exitosamente");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("Error: " + e.getMessage());
        }
    }
}
