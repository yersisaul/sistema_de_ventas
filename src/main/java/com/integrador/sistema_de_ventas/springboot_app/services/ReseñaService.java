package com.integrador.sistema_de_ventas.springboot_app.services;

import com.integrador.sistema_de_ventas.springboot_app.models.Reseña;
import java.util.List;
import java.util.Optional;

public interface ReseñaService {
    Reseña crearReseña(Reseña reseña);
    Optional<Reseña> obtenerReseñaPorId(Long id);
    List<Reseña> obtenerReseñasPorProducto(Long productoId);
    List<Reseña> obtenerReseñasAprobadas(Long productoId);
    List<Reseña> obtenerReseñasPorCliente(Long clienteId);
    List<Reseña> obtenerReseñasPendientesAprobacion();
    Double obtenerCalificacionPromedio(Long productoId);
    Reseña actualizarReseña(Long id, Reseña reseña);
    void aprobarReseña(Long id);
    void rechazarReseña(Long id);
}
