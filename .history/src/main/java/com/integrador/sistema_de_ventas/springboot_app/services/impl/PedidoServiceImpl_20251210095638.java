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
import com.integrador.sistema_de_ventas.springboot_app.services.ComprobanteGeneratorService;
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

    @Autowired
    private ComprobanteGeneratorService comprobanteGenerator;
    
    @Override
    public Pedido crearPedido(PedidoCreateDTO pedidoDTO) {
        Pedido pedido = new Pedido();
        
        // Cliente
        Usuario cliente = usuarioRepository.findById(pedidoDTO.getClienteId())
            .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));
        pedido.setCliente(cliente);
        
        // Fecha y estado
        pedido.setFecha(LocalDateTime.now());
        pedido.setFechaActualizacion(LocalDateTime.now());
        pedido.setEstado("PENDIENTE");
        
        // Datos adicionales
        pedido.setCostoEnvio(pedidoDTO.getCostoEnvio());
        pedido.setDireccionEnvio(pedidoDTO.getDireccionEnvio());
        pedido.setTelefonoContacto(pedidoDTO.getTelefonoContacto());
        pedido.setNotas(pedidoDTO.getNotas());
        
        pedido.setSubtotal(pedidoDTO.getSubtotal());
        pedido.setImpuestos(pedidoDTO.getImpuestos());
        pedido.setTotal(pedidoDTO.getTotal());
        
        // Tipo de comprobante
        String tipoComprobante = pedidoDTO.getTipoComprobante() != null ? 
            pedidoDTO.getTipoComprobante() : "BOLETA";
        String serie = tipoComprobante.equals("BOLETA") ? "B001" : "F001";
        
        // Generar número correlativo
        String numero = comprobanteGenerator.generarSiguienteNumero(tipoComprobante, serie);
        String numeroCompleto = serie + "-" + numero;
        
        // Asignar comprobante al pedido
        pedido.setTipoComprobante(tipoComprobante);
        pedido.setSerieComprobante(serie);
        pedido.setNumeroComprobante(numero);
        pedido.setNumeroComprobanteCompleto(numeroCompleto);
        
        System.out.println("📄 Comprobante: " + numeroCompleto);
        
        // Guardar pedido primero
        Pedido pedidoGuardado = pedidoRepository.save(pedido);
        
        // Crear y guardar detalles
        List<DetallePedido> detalles = new ArrayList<>();
        
        for (var detalleDTO : pedidoDTO.getDetalles()) { 
            DetallePedido detalle = new DetallePedido();
            detalle.setPedido(pedidoGuardado);
            
            // Obtener variante
            VarianteProducto varianteProducto = varianteProductoRepository
                .findById(detalleDTO.getVarianteProductoId())
                .orElseThrow(() -> new RuntimeException("Variante de producto no encontrada"));
            
            // Validar stock
            if (varianteProducto.getStock() < detalleDTO.getCantidad()) {
                throw new RuntimeException("Stock insuficiente para la variante de producto ID: " + 
                    varianteProducto.getId());
            }
            
            detalle.setVarianteProducto(varianteProducto);
            detalle.setCantidad(detalleDTO.getCantidad());
            detalle.setPrecioUnitario(varianteProducto.getPrecio());
            detalle.setSubtotal(varianteProducto.getPrecio()
                .multiply(new BigDecimal(detalleDTO.getCantidad())));
            
            // Actualizar stock de la variante
            varianteProducto.setStock(varianteProducto.getStock() - detalleDTO.getCantidad());
            varianteProductoRepository.save(varianteProducto);
            
            detalles.add(detalle);
        }
        
        // Guardar todos los detalles
        detallePedidoRepository.saveAll(detalles);
        
        // Asociar detalles al pedido
        pedidoGuardado.setDetalles(detalles);
        
        return pedidoGuardado;
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
