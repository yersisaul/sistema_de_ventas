package com.integrador.sistema_de_ventas.springboot_app.services;

import com.integrador.sistema_de_ventas.springboot_app.models.Envio;
import java.util.List;
import java.util.Optional;

public interface EnvioService {
    Envio crearEnvio(Envio envio);
    Optional<Envio> obtenerEnvioPorId(Long id);
    List<Envio> obtenerEnviosPorPedido(Long pedidoId);
    List<Envio> obtenerEnviosPorEstado(String estado);
    List<Envio> obtenerEnviosPorEmpleado(Long empleadoId);
    Envio actualizarEnvio(Long id, Envio envio);
    Envio actualizarEstadoEnvio(Long id, String nuevoEstado);
    Envio asignarEmpleado(Long envioId, Long empleadoId);
}
