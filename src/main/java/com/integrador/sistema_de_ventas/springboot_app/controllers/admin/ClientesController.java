package com.integrador.sistema_de_ventas.springboot_app.controllers.admin;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.integrador.sistema_de_ventas.springboot_app.models.Usuario;
import com.integrador.sistema_de_ventas.springboot_app.services.UsuarioService;

@RestController
@RequestMapping("/api/admin/clientes")
@CrossOrigin(origins = "*")
public class ClientesController {
    
    @Autowired
    private UsuarioService usuarioService;
    
    @GetMapping("")
    public ResponseEntity<?> obtenerTodosLosClientes() {
        try {
            // CORREGIDO: Usamos el Enum Usuario.Rol.CLIENTE en lugar del String "CLIENTE"
            List<Usuario> clientes = usuarioService.obtenerUsuariosPorRol(Usuario.Rol.CLIENTE);
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
            
            // CORREGIDO: Comparamos el Enum con == en lugar de .equals con String
            if (cliente.isPresent() && cliente.get().getRol() == Usuario.Rol.CLIENTE) {
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
            // Opcional: Podrías validar aquí que el usuario a editar sea realmente un cliente
            // antes de enviarlo al servicio, pero el servicio se encargará de actualizar los datos.
            
            // Aseguramos que no se cambie el rol accidentalmente a otra cosa que no sea cliente
            // (Opcional, depende de tu lógica de negocio)
            // cliente.setRol(Usuario.Rol.CLIENTE); 
            
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