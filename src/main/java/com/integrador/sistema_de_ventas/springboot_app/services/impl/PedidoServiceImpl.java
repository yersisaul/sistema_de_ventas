package com.integrador.sistema_de_ventas.springboot_app.services.impl;

import com.integrador.sistema_de_ventas.springboot_app.dto.PedidoCreateDTO;
import com.integrador.sistema_de_ventas.springboot_app.dto.PedidoDTO;
import com.integrador.sistema_de_ventas.springboot_app.models.DetallePedido;
import com.integrador.sistema_de_ventas.springboot_app.models.Pedido;
import com.integrador.sistema_de_ventas.springboot_app.models.Usuario;
import com.integrador.sistema_de_ventas.springboot_app.models.VarianteProducto;
import com.integrador.sistema_de_ventas.springboot_app.repository.DetallePedidoRepository;
import com.integrador.sistema_de_ventas.springboot_app.repository.PedidoRepository;
import com.integrador.sistema_de_ventas.springboot_app.repository.UsuarioRepository;
import com.integrador.sistema_de_ventas.springboot_app.repository.VarianteProductoRepository;
import com.integrador.sistema_de_ventas.springboot_app.services.PedidoService;

import org.aspectj.weaver.ast.Var;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class PedidoServiceImpl implements PedidoService {
    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private VarianteProductoRepository varianteProductoRepository;

    @Autowired
    private DetallePedidoRepository detallePedidoRepository;
    
    @Override
    public Pedido crearPedido(PedidoCreateDTO pedidoDTO) {
        Pedido pedido = new Pedido();
         Usuario cliente = usuarioRepository.findById(pedidoDTO.getClienteId())
            .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));
        pedido.setCliente(cliente);
        pedido.setFecha(LocalDateTime.now());
        pedido.setCostoEnvio(pedidoDTO.getCostoEnvio());
        pedido.setDireccionEnvio(pedidoDTO.getDireccionEnvio());
        pedido.setTelefonoContacto(pedidoDTO.getTelefonoContacto());
        pedido.setNotas(pedidoDTO.getNotas());

        pedido.setFechaActualizacion(LocalDateTime.now());
        pedido.setEstado("PENDIENTE");

        List<DetallePedido> detalles = new ArrayList<>();
        BigDecimal sub_total_pedido = BigDecimal.ZERO;
        for (var detalleDTO : pedidoDTO.getDetalles()) { // Iterar sobre los detalles del pedido
            DetallePedido detalle = new DetallePedido();
            detalle.setPedido(pedido);
            VarianteProducto variante_producto = varianteProductoRepository.findById(detalleDTO.getVarianteProductoId())
                .orElseThrow(() -> new RuntimeException("Variante de producto no encontrada"));
            detalle.setVarianteProducto(variante_producto);
            
            if (variante_producto.getStock() < detalleDTO.getCantidad()) {
                throw new RuntimeException("Stock insuficiente para la variante de producto ID: " + variante_producto.getId());
            }
            detalle.setCantidad(detalleDTO.getCantidad());
            detalle.setPrecioUnitario(variante_producto.getPrecio());
            detalle.setSubtotal(variante_producto.getPrecio().multiply(new BigDecimal(detalleDTO.getCantidad())));
            sub_total_pedido = sub_total_pedido.add(detalle.getSubtotal());
            detalles.add(detalle);
        }
        pedido.setDetalles(detalles);
        pedido.setSubtotal(sub_total_pedido.add(pedidoDTO.getCostoEnvio())); // Actualizar el subtotal del pedido
        pedido.setImpuestos(pedido.getSubtotal().multiply(new BigDecimal("0.18"))); // Calcular impuestos (18%)
        pedido.setTotal(sub_total_pedido.add(pedido.getImpuestos())); // Calcular el total del pedido
        Pedido pedido_guardado = pedidoRepository.save(pedido); // Guardar el pedido antes de agregar los detalles
        for (DetallePedido detalle : detalles){
            DetallePedido detalle_guardado = detallePedidoRepository.save(detalle); // Guardar cada detalle del pedido
        }
        return pedido_guardado;
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
