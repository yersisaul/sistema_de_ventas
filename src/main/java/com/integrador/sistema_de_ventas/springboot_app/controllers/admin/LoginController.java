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
@CrossOrigin(origins = "*")
public class LoginController {
    @Autowired
    private UsuarioService usuarioService;
    
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        try {
            if (usuarioService.validarCredenciales(loginRequest.getNIdentificacion(), loginRequest.getContrasena())) {
                Optional<Usuario> usuario = usuarioService.obtenerUsuarioPorIdentificacion(loginRequest.getNIdentificacion());
                if (usuario.isPresent() && "ADMIN".equals(usuario.get().getRol())) {
                    LoginResponse response = new LoginResponse(
                        usuario.get().getId(),
                        usuario.get().getNombres(),
                        usuario.get().getApellidos(),
                        usuario.get().getNIdentificacion(),
                        usuario.get().getCorreo(),
                        usuario.get().getRol(),
                        "Login exitoso"
                    );
                    return ResponseEntity.ok(response);
                }
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(new LoginResponse(null, null, null, null, null, null, "Acceso denegado: no es administrador"));
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
            usuario.setRol("ADMIN");
            Usuario nuevoAdmin = usuarioService.crearUsuario(usuario);
            return ResponseEntity.status(HttpStatus.CREATED)
                .body(new LoginResponse(
                    nuevoAdmin.getId(),
                    nuevoAdmin.getNombres(),
                    nuevoAdmin.getApellidos(),
                    nuevoAdmin.getNIdentificacion(),
                    nuevoAdmin.getCorreo(),
                    nuevoAdmin.getRol(),
                    "Administrador registrado exitosamente"
                ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new LoginResponse(null, null, null, null, null, null, "Error: " + e.getMessage()));
        }
    }
}
