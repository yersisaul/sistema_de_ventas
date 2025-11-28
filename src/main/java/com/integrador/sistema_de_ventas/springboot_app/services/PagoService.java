package com.integrador.sistema_de_ventas.springboot_app.services;

import com.integrador.sistema_de_ventas.springboot_app.models.Pago;
import java.util.List;
import java.util.Optional;

public interface PagoService {
    Pago crearPago(Pago pago);
    Optional<Pago> obtenerPagoPorId(Long id);
    List<Pago> obtenerPagosPorPedido(Long pedidoId);
    List<Pago> obtenerPagosPorEstado(String estado);
    Optional<Pago> obtenerPagoPorReferencia(String referencia);
    Pago actualizarEstadoPago(Long id, String nuevoEstado);
    Pago confirmarPago(Long id);
    void rechazarPago(Long id);
}
