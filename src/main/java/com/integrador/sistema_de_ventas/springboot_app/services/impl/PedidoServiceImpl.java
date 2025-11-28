package com.integrador.sistema_de_ventas.springboot_app.services.impl;

import com.integrador.sistema_de_ventas.springboot_app.models.Pedido;
import com.integrador.sistema_de_ventas.springboot_app.repository.PedidoRepository;
import com.integrador.sistema_de_ventas.springboot_app.services.PedidoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class PedidoServiceImpl implements PedidoService {
    
    @Autowired
    private PedidoRepository pedidoRepository;
    
    @Override
    public Pedido crearPedido(Pedido pedido) {
        pedido.setFecha(LocalDateTime.now());
        pedido.setFechaActualizacion(LocalDateTime.now());
        pedido.setEstado("PENDIENTE");
        return pedidoRepository.save(pedido);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Optional<Pedido> obtenerPedidoPorId(Long id) {
        return pedidoRepository.findById(id);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Pedido> obtenerPedidosPorCliente(Long clienteId) {
        return pedidoRepository.findPedidosClienteOrdenados(clienteId);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Pedido> obtenerTodosPedidos() {
        return pedidoRepository.findAll();
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Pedido> obtenerPedidosPorEstado(String estado) {
        return pedidoRepository.findByEstado(estado);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Pedido> obtenerPedidosPorFecha(LocalDateTime fechaInicio, LocalDateTime fechaFin) {
        return pedidoRepository.findPedidosPorFecha(fechaInicio, fechaFin);
    }
    
    @Override
    public Pedido actualizarPedido(Long id, Pedido pedidoActualizado) {
        Pedido pedido = pedidoRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Pedido no encontrado"));
        
        pedido.setDireccionEnvio(pedidoActualizado.getDireccionEnvio());
        pedido.setTelefonoContacto(pedidoActualizado.getTelefonoContacto());
        pedido.setNotas(pedidoActualizado.getNotas());
        pedido.setFechaActualizacion(LocalDateTime.now());
        
        return pedidoRepository.save(pedido);
    }
    
    @Override
    public Pedido actualizarEstadoPedido(Long id, String nuevoEstado) {
        Pedido pedido = pedidoRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Pedido no encontrado"));
        
        pedido.setEstado(nuevoEstado);
        pedido.setFechaActualizacion(LocalDateTime.now());
        
        return pedidoRepository.save(pedido);
    }
    
    @Override
    public void cancelarPedido(Long id) {
        Pedido pedido = pedidoRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Pedido no encontrado"));
        
        if (!pedido.getEstado().equals("PENDIENTE")) {
            throw new RuntimeException("Solo se pueden cancelar pedidos en estado PENDIENTE");
        }
        
        pedido.setEstado("CANCELADO");
        pedido.setFechaActualizacion(LocalDateTime.now());
        pedidoRepository.save(pedido);
    }
}
