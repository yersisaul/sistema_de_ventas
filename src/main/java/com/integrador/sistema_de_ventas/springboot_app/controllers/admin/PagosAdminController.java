package com.integrador.sistema_de_ventas.springboot_app.controllers.admin;

import com.integrador.sistema_de_ventas.springboot_app.models.ComprobantePago;
import com.integrador.sistema_de_ventas.springboot_app.models.Pago;
import com.integrador.sistema_de_ventas.springboot_app.services.PagoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import org.springframework.core.io.Resource;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/admin/pagos")
@CrossOrigin(origins = "*")
public class PagosAdminController {

    @Autowired
    private PagoService pagoService;

    @GetMapping
    public ResponseEntity<?> obtenerTodosPagos() {
        try {
            // Implementar en servicio si es necesario
            return ResponseEntity.ok("Endpoint para obtener todos los pagos");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error: " + e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerPagoPorId(@PathVariable Long id) {
        try {
            Optional<Pago> pago = pagoService.obtenerPagoPorId(id);
            if (pago.isPresent()) {
                return ResponseEntity.ok(pago.get());
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Pago no encontrado");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error: " + e.getMessage());
        }
    }

    @GetMapping("/pedido/{pedidoId}")
    public ResponseEntity<?> obtenerPagosPorPedido(@PathVariable Long pedidoId) {
        try {
            List<Pago> pagos = pagoService.obtenerPagosPorPedido(pedidoId);
            return ResponseEntity.ok(pagos);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error: " + e.getMessage());
        }
    }

    @GetMapping("/estado/{estado}")
    public ResponseEntity<?> obtenerPagosPorEstado(@PathVariable String estado) {
        try {
            List<Pago> pagos = pagoService.obtenerPagosPorEstado(estado);
            return ResponseEntity.ok(pagos);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error: " + e.getMessage());
        }
    }

    @PutMapping("/{id}/confirmar")
    public ResponseEntity<?> confirmarPago(@PathVariable Long id) {
        try {
            Pago pagoConfirmado = pagoService.confirmarPago(id);
            return ResponseEntity.ok(pagoConfirmado);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Error: " + e.getMessage());
        }
    }

    @PutMapping("/{id}/rechazar")
    public ResponseEntity<?> rechazarPago(@PathVariable Long id) {
        try {
            pagoService.rechazarPago(id);
            return ResponseEntity.ok("Pago rechazado exitosamente");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Error: " + e.getMessage());
        }
    }

     // Subir comprobante
    @PostMapping("/{id}/comprobante")
    public ResponseEntity<?> subirComprobante(
            @PathVariable Long id,
            @RequestParam("archivo") MultipartFile archivo) {
        try {
            if (archivo.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("Archivo vacío");
            }

            String nombreArchivo = System.currentTimeMillis() + "_" + archivo.getOriginalFilename();
            ComprobantePago comprobante = pagoService.subirComprobante(id, archivo);
            return ResponseEntity.ok(comprobante);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Error: " + e.getMessage());
        }
    }

    // Listar comprobantes
    @GetMapping("/{id}/comprobantes")
    public ResponseEntity<?> obtenerComprobantes(@PathVariable Long id) {
        try {
            List<ComprobantePago> comprobantes = pagoService.obtenerComprobantesPorPago(id);
            return ResponseEntity.ok(comprobantes);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error: " + e.getMessage());
        }
    }

    // Descargar comprobante
    @GetMapping("/{id}/comprobante/{comprobanteId}/descargar")
    public ResponseEntity<Resource> descargarComprobante(
            @PathVariable Long id,
            @PathVariable Long comprobanteId) {
        try {
            ComprobantePago comprobante = pagoService.obtenerComprobantePorId(comprobanteId);
            Path path = Paths.get(comprobante.getRutaArchivo());
            Resource resource = new UrlResource(path.toUri());

            if (!resource.exists()) {
                return ResponseEntity.notFound().build();
            }

            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .header(HttpHeaders.CONTENT_DISPOSITION,
                            "attachment; filename=\"" + comprobante.getNombreArchivo() + "\"")
                    .body(resource);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

}
