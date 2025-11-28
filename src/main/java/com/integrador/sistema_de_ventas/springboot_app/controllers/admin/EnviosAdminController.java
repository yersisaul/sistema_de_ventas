package com.integrador.sistema_de_ventas.springboot_app.controllers.admin;

import com.integrador.sistema_de_ventas.springboot_app.models.Envio;
import com.integrador.sistema_de_ventas.springboot_app.services.EnvioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/admin/envios")
@CrossOrigin(origins = "*")
public class EnviosAdminController {
    
    @Autowired
    private EnvioService envioService;
    
    @PostMapping
    public ResponseEntity<?> crearEnvio(@RequestBody Envio envio) {
        try {
            Envio nuevoEnvio = envioService.crearEnvio(envio);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevoEnvio);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("Error: " + e.getMessage());
        }
    }
    
    @GetMapping
    public ResponseEntity<?> obtenerTodosLosEnvios() {
        try {
            // Implementar en servicio si es necesario
            return ResponseEntity.ok("Endpoint para obtener todos los envíos");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error: " + e.getMessage());
        }
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerEnvioPorId(@PathVariable Long id) {
        try {
            Optional<Envio> envio = envioService.obtenerEnvioPorId(id);
            if (envio.isPresent()) {
                return ResponseEntity.ok(envio.get());
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("Envío no encontrado");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error: " + e.getMessage());
        }
    }
    
    @GetMapping("/pedido/{pedidoId}")
    public ResponseEntity<?> obtenerEnviosPorPedido(@PathVariable Long pedidoId) {
        try {
            List<Envio> envios = envioService.obtenerEnviosPorPedido(pedidoId);
            return ResponseEntity.ok(envios);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error: " + e.getMessage());
        }
    }
    
    @GetMapping("/estado/{estado}")
    public ResponseEntity<?> obtenerEnviosPorEstado(@PathVariable String estado) {
        try {
            List<Envio> envios = envioService.obtenerEnviosPorEstado(estado);
            return ResponseEntity.ok(envios);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error: " + e.getMessage());
        }
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarEnvio(@PathVariable Long id, @RequestBody Envio envio) {
        try {
            Envio envioActualizado = envioService.actualizarEnvio(id, envio);
            return ResponseEntity.ok(envioActualizado);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("Error: " + e.getMessage());
        }
    }
    
    @PutMapping("/{id}/estado")
    public ResponseEntity<?> actualizarEstadoEnvio(@PathVariable Long id, @RequestParam String nuevoEstado) {
        try {
            Envio envioActualizado = envioService.actualizarEstadoEnvio(id, nuevoEstado);
            return ResponseEntity.ok(envioActualizado);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("Error: " + e.getMessage());
        }
    }
    
    @PutMapping("/{envioId}/asignar-empleado/{empleadoId}")
    public ResponseEntity<?> asignarEmpleado(@PathVariable Long envioId, @PathVariable Long empleadoId) {
        try {
            Envio envioActualizado = envioService.asignarEmpleado(envioId, empleadoId);
            return ResponseEntity.ok(envioActualizado);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("Error: " + e.getMessage());
        }
    }
}
