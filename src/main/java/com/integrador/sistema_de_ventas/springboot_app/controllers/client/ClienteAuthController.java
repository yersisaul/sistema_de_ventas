package com.integrador.sistema_de_ventas.springboot_app.controllers.client;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.integrador.sistema_de_ventas.springboot_app.dto.LoginRequest;
import com.integrador.sistema_de_ventas.springboot_app.dto.LoginResponse;
import com.integrador.sistema_de_ventas.springboot_app.models.Usuario;
import com.integrador.sistema_de_ventas.springboot_app.services.UsuarioService;

@RestController
@RequestMapping("/api/client/auth")
@CrossOrigin(origins = "*")
public class ClienteAuthController {
    
    @Autowired
    private UsuarioService usuarioService;
    
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        try {
            if (usuarioService.validarCredenciales(loginRequest.getNIdentificacion(), loginRequest.getContrasena())) {
                Optional<Usuario> usuarioOpt = usuarioService.obtenerUsuarioPorIdentificacion(loginRequest.getNIdentificacion());
                
                // CORREGIDO: Usamos el Enum para la comparación
                if (usuarioOpt.isPresent()) {
                    Usuario usuario = usuarioOpt.get();
                    
                    if (usuario.getRol() == Usuario.Rol.CLIENTE) {
                        LoginResponse response = new LoginResponse(
                            usuario.getId(),
                            usuario.getNombres(),
                            usuario.getApellidos(),
                            usuario.getNIdentificacion(),
                            usuario.getCorreo(),
                            usuario.getRol().toString(), // CORREGIDO: Convertimos Enum a String
                            "Login exitoso"
                        );
                        return ResponseEntity.ok(response);
                    } else {
                        // Si existe pero no es CLIENTE (ej: es un admin intentando loguearse como cliente)
                        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                            .body(new LoginResponse(null, null, null, null, null, null, "Acceso denegado: No es cuenta de cliente"));
                    }
                }
            }
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new LoginResponse(null, null, null, null, null, null, "Credenciales inválidas"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new LoginResponse(null, null, null, null, null, null, "Error en el servidor: " + e.getMessage()));
        }
    }
    
    @PostMapping("/register")
    public ResponseEntity<?> registrarCliente(@RequestBody Usuario usuario) {
        try {
            // CORREGIDO: Usamos el Enum para asignar el rol
            usuario.setRol(Usuario.Rol.CLIENTE);
            
            Usuario nuevoCliente = usuarioService.crearUsuario(usuario);
            
            return ResponseEntity.status(HttpStatus.CREATED)
                .body(new LoginResponse(
                    nuevoCliente.getId(),
                    nuevoCliente.getNombres(),
                    nuevoCliente.getApellidos(),
                    nuevoCliente.getNIdentificacion(),
                    nuevoCliente.getCorreo(),
                    nuevoCliente.getRol().toString(), // CORREGIDO: Convertimos Enum a String
                    "Cliente registrado exitosamente"
                ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new LoginResponse(null, null, null, null, null, null, "Error: " + e.getMessage()));
        }
    }
}