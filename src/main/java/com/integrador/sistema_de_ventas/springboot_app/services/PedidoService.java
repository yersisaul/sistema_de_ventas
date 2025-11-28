package com.integrador.sistema_de_ventas.springboot_app.services;

import com.integrador.sistema_de_ventas.springboot_app.models.Pedido;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface PedidoService {
    Pedido crearPedido(Pedido pedido);
    Optional<Pedido> obtenerPedidoPorId(Long id);
    List<Pedido> obtenerPedidosPorCliente(Long clienteId);
    List<Pedido> obtenerTodosPedidos();
    List<Pedido> obtenerPedidosPorEstado(String estado);
    List<Pedido> obtenerPedidosPorFecha(LocalDateTime fechaInicio, LocalDateTime fechaFin);
    Pedido actualizarPedido(Long id, Pedido pedido);
    Pedido actualizarEstadoPedido(Long id, String nuevoEstado);
    void cancelarPedido(Long id);
}
