package com.integrador.sistema_de_ventas.springboot_app.services.impl;

import com.integrador.sistema_de_ventas.springboot_app.models.Reseña;
import com.integrador.sistema_de_ventas.springboot_app.repository.ReseñaRepository;
import com.integrador.sistema_de_ventas.springboot_app.services.ReseñaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ReseñaServiceImpl implements ReseñaService {
    
    @Autowired
    private ReseñaRepository reseñaRepository;
    
    @Override
    public Reseña crearReseña(Reseña reseña) {
        if (reseña.getCalificacion() < 1 || reseña.getCalificacion() > 5) {
            throw new RuntimeException("La calificación debe estar entre 1 y 5");
        }
        
        Optional<Reseña> reseñaExistente = reseñaRepository.findByClienteIdAndProductoId(
            reseña.getCliente().getId(), 
            reseña.getProducto().getId()
        );
        
        if (reseñaExistente.isPresent()) {
            throw new RuntimeException("El cliente ya ha reseñado este producto");
        }
        
        reseña.setFechaCreacion(LocalDateTime.now());
        reseña.setAprobada(false);
        return reseñaRepository.save(reseña);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Optional<Reseña> obtenerReseñaPorId(Long id) {
        return reseñaRepository.findById(id);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Reseña> obtenerReseñasPorProducto(Long productoId) {
        return reseñaRepository.findByProductoId(productoId);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Reseña> obtenerReseñasAprobadas(Long productoId) {
        return reseñaRepository.findByProductoIdAndAprobada(productoId, true);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Reseña> obtenerReseñasPorCliente(Long clienteId) {
        return reseñaRepository.findByClienteId(clienteId);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Reseña> obtenerReseñasPendientesAprobacion() {
        return reseñaRepository.findReseñasPendientesAprobacion();
    }
    
    @Override
    @Transactional(readOnly = true)
    public Double obtenerCalificacionPromedio(Long productoId) {
        Double promedio = reseñaRepository.findCalificacionPromedio(productoId);
        return promedio != null ? promedio : 0.0;
    }
    
    @Override
    public Reseña actualizarReseña(Long id, Reseña reseñaActualizada) {
        Reseña reseña = reseñaRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Reseña no encontrada"));
        
        if (reseñaActualizada.getCalificacion() < 1 || reseñaActualizada.getCalificacion() > 5) {
            throw new RuntimeException("La calificación debe estar entre 1 y 5");
        }
        
        reseña.setCalificacion(reseñaActualizada.getCalificacion());
        reseña.setComentario(reseñaActualizada.getComentario());
        
        return reseñaRepository.save(reseña);
    }
    
    @Override
    public void aprobarReseña(Long id) {
        Reseña reseña = reseñaRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Reseña no encontrada"));
        reseña.setAprobada(true);
        reseñaRepository.save(reseña);
    }
    
    @Override
    public void rechazarReseña(Long id) {
        Reseña reseña = reseñaRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Reseña no encontrada"));
        reseñaRepository.delete(reseña);
    }
}
