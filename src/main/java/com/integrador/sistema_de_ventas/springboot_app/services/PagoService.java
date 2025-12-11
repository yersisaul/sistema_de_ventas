package com.integrador.sistema_de_ventas.springboot_app.services;

import com.integrador.sistema_de_ventas.springboot_app.models.ComprobantePago;
import com.integrador.sistema_de_ventas.springboot_app.models.Pago;
import java.util.List;
import java.util.Optional;


import org.springframework.web.multipart.MultipartFile;

public interface PagoService {
    Pago crearPago(Pago pago);
    Optional<Pago> obtenerPagoPorId(Long id);
    List<Pago> obtenerPagosPorPedido(Long pedidoId);
    List<Pago> obtenerPagosPorEstado(String estado);
    Optional<Pago> obtenerPagoPorReferencia(String referencia);
    Pago actualizarEstadoPago(Long id, String nuevoEstado);
    Pago confirmarPago(Long id);
    void rechazarPago(Long id);
    // Comprobantes
    ComprobantePago subirComprobante(Long pagoId, MultipartFile archivo) throws Exception;
    List<ComprobantePago> obtenerComprobantesPorPago(Long pagoId);
    ComprobantePago obtenerComprobantePorId(Long comprobanteId);  // Nuevo método

    // Todos los pagos
    List<Pago> obtenerTodosPagos(); // Nuevo método

}
