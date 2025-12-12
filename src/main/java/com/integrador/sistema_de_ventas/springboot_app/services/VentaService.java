package com.integrador.sistema_de_ventas.springboot_app.services;

import com.integrador.sistema_de_ventas.springboot_app.dto.DetalleVentaRequest;
import com.integrador.sistema_de_ventas.springboot_app.dto.VentaRequest;
import com.integrador.sistema_de_ventas.springboot_app.models.*;
import com.integrador.sistema_de_ventas.springboot_app.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort; // IMPORTANTE: Agregado
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List; // IMPORTANTE: Agregado

@Service
@Transactional
public class VentaService {

    @Autowired private PedidoRepository pedidoRepository;
    @Autowired private DetallePedidoRepository detallePedidoRepository;
    @Autowired private PagoRepository pagoRepository;
    @Autowired private UsuarioRepository usuarioRepository;
    @Autowired private VarianteProductoRepository varianteRepository;

    // --- 1. REGISTRAR VENTA (Lo que ya tenías) ---
    public void registrarVenta(VentaRequest request) {
        // 1. Obtener Cliente
        Usuario cliente;
        if (request.getClienteId() != null) {
            cliente = usuarioRepository.findById(request.getClienteId())
                    .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));
        } else {
            // BUSCAMOS UN CLIENTE GENÉRICO SI NO SE SELECCIONA UNO
            // Asegúrate de que este correo exista en tu BD o cámbialo por uno que sí exista
            cliente = usuarioRepository.findByCorreo("cliente1@mambo.com")
                    .orElseThrow(() -> new RuntimeException("No hay cliente genérico configurado en la BD"));
        }

        // 2. Crear Cabecera (Pedido)
        Pedido pedido = new Pedido();
        pedido.setCliente(cliente);
        pedido.setTotal(BigDecimal.valueOf(request.getTotal()));
        // Evitamos división por cero y manejamos nulos si es necesario
        double total = request.getTotal() != null ? request.getTotal() : 0.0;
        pedido.setSubtotal(BigDecimal.valueOf(total / 1.18)); 
        pedido.setImpuestos(pedido.getTotal().subtract(pedido.getSubtotal()));
        pedido.setEstado("COMPLETADO");
        pedido.setDireccionEnvio("Venta en Tienda");
        pedido.setTelefonoContacto(cliente.getTelefono());
        
        pedido = pedidoRepository.save(pedido);

        // 3. Guardar Detalles y Restar Stock
        if (request.getProductos() != null) {
            for (DetalleVentaRequest item : request.getProductos()) {
                VarianteProducto variante = varianteRepository.findById(item.getProductoId())
                        .orElseThrow(() -> new RuntimeException("Producto/Variante no encontrada con ID: " + item.getProductoId()));

                if (variante.getStock() < item.getCantidad()) {
                    throw new RuntimeException("Stock insuficiente para: " + variante.getProducto().getNombre());
                }

                // Restar Stock
                variante.setStock(variante.getStock() - item.getCantidad());
                varianteRepository.save(variante);

                // Crear Detalle
                DetallePedido detalle = new DetallePedido();
                detalle.setPedido(pedido);
                detalle.setVarianteProducto(variante);
                detalle.setCantidad(item.getCantidad());
                detalle.setPrecioUnitario(BigDecimal.valueOf(item.getPrecioUnitario()));
                detalle.setSubtotal(BigDecimal.valueOf(item.getSubtotal()));
                
                detallePedidoRepository.save(detalle);
            }
        }

        // 4. Registrar Pago
        Pago pago = new Pago();
        pago.setPedido(pedido);
        pago.setMonto(pedido.getTotal());
        pago.setMetodoPago(request.getMetodoPago());
        pago.setEstadoPago("PAGADO");
        
        pagoRepository.save(pago);
    }

    // --- 2. LISTAR VENTAS (ESTO ES LO QUE FALTABA) ---
    @Transactional(readOnly = true)
    public List<Pedido> obtenerTodasLasVentas() {
        // Devuelve los pedidos ordenados: el más reciente primero
        return pedidoRepository.findAll(Sort.by(Sort.Direction.DESC, "fecha"));
    }
}