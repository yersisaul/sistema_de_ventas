package com.integrador.sistema_de_ventas.springboot_app.services.impl;

import com.integrador.sistema_de_ventas.springboot_app.models.Pago;
import com.integrador.sistema_de_ventas.springboot_app.repository.PagoRepository;
import com.integrador.sistema_de_ventas.springboot_app.services.PagoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class PagoServiceImpl implements PagoService {
    
    @Autowired
    private PagoRepository pagoRepository;
    
    @Override
    public Pago crearPago(Pago pago) {
        pago.setFechaCreacion(LocalDateTime.now());
        pago.setEstadoPago("PENDIENTE");
        return pagoRepository.save(pago);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Optional<Pago> obtenerPagoPorId(Long id) {
        return pagoRepository.findById(id);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Pago> obtenerPagosPorPedido(Long pedidoId) {
        return pagoRepository.findByPedidoId(pedidoId);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Pago> obtenerPagosPorEstado(String estado) {
        return pagoRepository.findByEstadoPago(estado);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Optional<Pago> obtenerPagoPorReferencia(String referencia) {
        return pagoRepository.findByReferenciaPago(referencia);
    }
    
    @Override
    public Pago actualizarEstadoPago(Long id, String nuevoEstado) {
        Pago pago = pagoRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Pago no encontrado"));
        
        pago.setEstadoPago(nuevoEstado);
        
        return pagoRepository.save(pago);
    }
    
    @Override
    public Pago confirmarPago(Long id) {
        Pago pago = pagoRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Pago no encontrado"));
        
        pago.setEstadoPago("COMPLETADO");
        pago.setFechaPago(LocalDateTime.now());
        
        return pagoRepository.save(pago);
    }
    
    @Override
    public void rechazarPago(Long id) {
        Pago pago = pagoRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Pago no encontrado"));
        
        pago.setEstadoPago("RECHAZADO");
        
        pagoRepository.save(pago);
    }
}
