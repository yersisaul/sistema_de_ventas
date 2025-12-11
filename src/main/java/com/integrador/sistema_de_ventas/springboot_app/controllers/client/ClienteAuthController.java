package com.integrador.sistema_de_ventas.springboot_app.controllers.client;

import com.integrador.sistema_de_ventas.springboot_app.dto.LoginRequest;
import com.integrador.sistema_de_ventas.springboot_app.dto.LoginResponse;
import com.integrador.sistema_de_ventas.springboot_app.dto.UsuarioCreateDTO;
import com.integrador.sistema_de_ventas.springboot_app.dto.UsuarioResponseDTO;
import com.integrador.sistema_de_ventas.springboot_app.exception.BadRequestException;
import com.integrador.sistema_de_ventas.springboot_app.models.Usuario;
import com.integrador.sistema_de_ventas.springboot_app.services.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Optional;

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
                Optional<Usuario> usuario = usuarioService
                        .obtenerUsuarioPorIdentificacion(loginRequest.getNIdentificacion());
                if (usuario.isPresent() && "CLIENTE".equals(usuario.get().getRol())) {
                    LoginResponse response = new LoginResponse(
                            usuario.get().getId(),
                            usuario.get().getNombres(),
                            usuario.get().getApellidos(),
                            usuario.get().getNIdentificacion(),
                            usuario.get().getCorreo(),
                            usuario.get().getRol(),
                            "Login exitoso");
                    return ResponseEntity.ok(response);
                }
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(new LoginResponse(null, null, null, null, null, null, "Acceso denegado"));
            }
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new LoginResponse(null, null, null, null, null, null, "Credenciales inválidas"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new LoginResponse(null, null, null, null, null, null,
                            "Error en el servidor: " + e.getMessage()));
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> registrarCliente(@RequestBody UsuarioCreateDTO createDTO) {
        try {
            UsuarioResponseDTO nuevoClienteDTO = usuarioService.crearCliente(createDTO);
            LoginResponse response = new LoginResponse(
                    nuevoClienteDTO.getIdUsuario(),
                    nuevoClienteDTO.getNombres(),
                    nuevoClienteDTO.getApellidos(),
                    nuevoClienteDTO.getNIdentificacion(),
                    nuevoClienteDTO.getCorreo(),
                    "CLIENTE",
                    "Cliente registrado exitosamente");

            return ResponseEntity.status(HttpStatus.CREATED).body(response);

        } catch (BadRequestException e) {

            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new LoginResponse(null, null, null, null, null, null,
                            "Error de registro: " + e.getMessage()));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new LoginResponse(null, null, null, null, null, null, "Error: " + e.getMessage()));
        }
    }

}
