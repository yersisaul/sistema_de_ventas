package com.integrador.sistema_de_ventas.springboot_app.services.impl;

import com.integrador.sistema_de_ventas.springboot_app.models.Envio;
import com.integrador.sistema_de_ventas.springboot_app.repository.EnvioRepository;
import com.integrador.sistema_de_ventas.springboot_app.services.EnvioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class EnvioServiceImpl implements EnvioService {
    
    @Autowired
    private EnvioRepository envioRepository;
    
    @Override
    public Envio crearEnvio(Envio envio) {
        envio.setFechaCreacion(LocalDateTime.now());
        envio.setFechaActualizacion(LocalDateTime.now());
        envio.setEstado("PENDIENTE");
        return envioRepository.save(envio);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Optional<Envio> obtenerEnvioPorId(Long id) {
        return envioRepository.findById(id);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Envio> obtenerEnviosPorPedido(Long pedidoId) {
        return envioRepository.findByPedidoId(pedidoId);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Envio> obtenerEnviosPorEstado(String estado) {
        return envioRepository.findEnviosPorEstado(estado);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Envio> obtenerEnviosPorEmpleado(Long empleadoId) {
        return envioRepository.findByUsuarioEmpleadoId(empleadoId);
    }
    
    @Override
    public Envio actualizarEnvio(Long id, Envio envioActualizado) {
        Envio envio = envioRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Envío no encontrado"));
        
        envio.setDireccion(envioActualizado.getDireccion());
        envio.setContacto(envioActualizado.getContacto());
        envio.setTransportista(envioActualizado.getTransportista());
        envio.setNumeroGuia(envioActualizado.getNumeroGuia());
        envio.setFechaActualizacion(LocalDateTime.now());
        
        return envioRepository.save(envio);
    }
    
    @Override
    public Envio actualizarEstadoEnvio(Long id, String nuevoEstado) {
        Envio envio = envioRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Envío no encontrado"));
        
        envio.setEstado(nuevoEstado);
        envio.setFechaActualizacion(LocalDateTime.now());
        
        return envioRepository.save(envio);
    }
    
    @Override
    public Envio asignarEmpleado(Long envioId, Long empleadoId) {
        Envio envio = envioRepository.findById(envioId)
            .orElseThrow(() -> new RuntimeException("Envío no encontrado"));
        
        // Aquí se asume que el empleado existe, en producción validar
        envio.setFechaActualizacion(LocalDateTime.now());
        
        return envioRepository.save(envio);
    }
}
