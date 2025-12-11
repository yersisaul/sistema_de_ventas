package com.integrador.sistema_de_ventas.springboot_app.controllers.admin;

import com.integrador.sistema_de_ventas.springboot_app.dto.UsuarioCreateDTO;
import com.integrador.sistema_de_ventas.springboot_app.models.Usuario;
import com.integrador.sistema_de_ventas.springboot_app.services.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import com.integrador.sistema_de_ventas.springboot_app.dto.UsuarioResponseDTO;
import com.integrador.sistema_de_ventas.springboot_app.exception.ResourceNotFoundException;
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
     public ResponseEntity<List<UsuarioResponseDTO>> obtenerTodosLosClientes() {
        try {
            List<UsuarioResponseDTO> clientesDTO = usuarioService.obtenerUsuariosPorRolDTO("CLIENTE"); 
            return ResponseEntity.ok(clientesDTO);
        } catch (Exception e) {
             return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{id}")
    // 💡 Cambio 3: El tipo de retorno ahora es UsuarioResponseDTO
    public ResponseEntity<?> obtenerClientePorId(@PathVariable Long id) {
        try {
            // 💡 Cambio 4: Usamos un método que ya devuelve el DTO y maneja la validación de rol
            UsuarioResponseDTO clienteDTO = usuarioService.obtenerClienteDTO(id);
            return ResponseEntity.ok(clienteDTO);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Cliente no encontrado");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());
        }
    }

    @PutMapping("/{id}/update")
public ResponseEntity<?> actualizarCliente(
        @PathVariable Long id,
        @RequestBody UsuarioCreateDTO dto) {
    try {
        UsuarioResponseDTO actualizado = usuarioService.actualizarCliente(id, dto);
        return ResponseEntity.ok(actualizado);
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

    @PostMapping("/create")
    public ResponseEntity<UsuarioResponseDTO> crearCliente(@RequestBody UsuarioCreateDTO dto) {
        UsuarioResponseDTO nuevo = usuarioService.crearCliente(dto);
        return ResponseEntity.ok(nuevo);
    }

  

}
