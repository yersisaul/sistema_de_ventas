package com.integrador.sistema_de_ventas.springboot_app.controllers.client;

import com.integrador.sistema_de_ventas.springboot_app.models.Usuario;
import com.integrador.sistema_de_ventas.springboot_app.services.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Optional;

@RestController
@RequestMapping("/api/client/perfil")
@CrossOrigin(origins = "*")
public class PerfilClienteController {
    
    @Autowired
    private UsuarioService usuarioService;
    
    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerPerfil(@PathVariable Long id) {
        try {
            Optional<Usuario> usuario = usuarioService.obtenerUsuarioPorId(id);
            if (usuario.isPresent()) {
                return ResponseEntity.ok(usuario.get());
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("Usuario no encontrado");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error: " + e.getMessage());
        }
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarPerfil(@PathVariable Long id, @RequestBody Usuario usuario) {
        try {
            Usuario usuarioActualizado = usuarioService.actualizarUsuario(id, usuario);
            return ResponseEntity.ok(usuarioActualizado);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("Error: " + e.getMessage());
        }
    }
    
    @GetMapping("/{id}/puntos-fidelizacion")
    public ResponseEntity<?> obtenerPuntosFidelizacion(@PathVariable Long id) {
        try {
            Optional<Usuario> usuario = usuarioService.obtenerUsuarioPorId(id);
            if (usuario.isPresent()) {
                return ResponseEntity.ok(usuario.get().getPuntosFidelizacion());
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("Usuario no encontrado");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error: " + e.getMessage());
        }
    }
}
