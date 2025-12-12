package com.integrador.sistema_de_ventas.springboot_app.controllers.admin;

import com.integrador.sistema_de_ventas.springboot_app.dto.LoginRequest;
import com.integrador.sistema_de_ventas.springboot_app.dto.LoginResponse;
import com.integrador.sistema_de_ventas.springboot_app.models.Usuario;
import com.integrador.sistema_de_ventas.springboot_app.services.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/admin/auth")
@CrossOrigin(origins = "*") // Permite peticiones desde cualquier frontend (React, Angular, etc.)
public class LoginController {

    @Autowired
    private UsuarioService usuarioService;
    
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        try {
            // 1. Validar credenciales (DNI y Contraseña)
            if (usuarioService.validarCredenciales(loginRequest.getNIdentificacion(), loginRequest.getContrasena())) {
                
                Optional<Usuario> usuarioOpt = usuarioService.obtenerUsuarioPorIdentificacion(loginRequest.getNIdentificacion());
                
                // 2. Verificar si existe y si el Rol es ADMINISTRADOR (Usando el Enum)
                if (usuarioOpt.isPresent()) {
                    Usuario usuario = usuarioOpt.get();
                    
                    if (usuario.getRol() == Usuario.Rol.ADMINISTRADOR) {
                        LoginResponse response = new LoginResponse(
                            usuario.getId(),
                            usuario.getNombres(),
                            usuario.getApellidos(),
                            usuario.getNIdentificacion(),
                            usuario.getCorreo(),
                            usuario.getRol().toString(), // Convertimos el Enum a String para enviarlo al frontend
                            "Login exitoso"
                        );
                        return ResponseEntity.ok(response);
                    } else {
                        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                            .body(new LoginResponse(null, null, null, null, null, null, "Acceso denegado: No tienes permisos de administrador"));
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
    
    @PostMapping("/register-admin")
    public ResponseEntity<?> registrarAdmin(@RequestBody Usuario usuario) {
        try {
            // CORREGIDO: Asignamos el Enum directamente, no un String
            usuario.setRol(Usuario.Rol.ADMINISTRADOR);
            
            Usuario nuevoAdmin = usuarioService.crearUsuario(usuario);
            
            return ResponseEntity.status(HttpStatus.CREATED)
                .body(new LoginResponse(
                    nuevoAdmin.getId(),
                    nuevoAdmin.getNombres(),
                    nuevoAdmin.getApellidos(),
                    nuevoAdmin.getNIdentificacion(),
                    nuevoAdmin.getCorreo(),
                    nuevoAdmin.getRol().toString(),
                    "Administrador registrado exitosamente"
                ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new LoginResponse(null, null, null, null, null, null, "Error: " + e.getMessage()));
        }
    }
}