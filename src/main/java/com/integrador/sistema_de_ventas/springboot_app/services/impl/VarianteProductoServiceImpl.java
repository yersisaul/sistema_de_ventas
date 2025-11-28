package com.integrador.sistema_de_ventas.springboot_app.services.impl;

import com.integrador.sistema_de_ventas.springboot_app.models.VarianteProducto;
import com.integrador.sistema_de_ventas.springboot_app.repository.VarianteProductoRepository;
import com.integrador.sistema_de_ventas.springboot_app.services.VarianteProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class VarianteProductoServiceImpl implements VarianteProductoService {
    
    @Autowired
    private VarianteProductoRepository varianteRepository;
    
    @Override
    public VarianteProducto crearVariante(VarianteProducto variante) {
        variante.setActivo(true);
        return varianteRepository.save(variante);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Optional<VarianteProducto> obtenerVariantePorId(Long id) {
        return varianteRepository.findById(id);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<VarianteProducto> obtenerVariantesPorProducto(Long productoId) {
        return varianteRepository.findByProductoIdAndActivo(productoId, true);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<VarianteProducto> obtenerVariantesConStock(Long productoId) {
        return varianteRepository.findVariantesConStock(productoId);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<VarianteProducto> obtenerVariantesStockBajo() {
        return varianteRepository.findVariantesStockBajo();
    }
    
    @Override
    @Transactional(readOnly = true)
    public Optional<VarianteProducto> obtenerVariantePorProductoTallaColor(Long productoId, String talla, String color) {
        return varianteRepository.findByProductoIdAndTallaAndColor(productoId, talla, color);
    }
    
    @Override
    public VarianteProducto actualizarVariante(Long id, VarianteProducto varianteActualizada) {
        VarianteProducto variante = varianteRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Variante no encontrada"));
        
        variante.setTalla(varianteActualizada.getTalla());
        variante.setColor(varianteActualizada.getColor());
        variante.setPrecio(varianteActualizada.getPrecio());
        variante.setPrecioDescuento(varianteActualizada.getPrecioDescuento());
        
        return varianteRepository.save(variante);
    }
    
    @Override
    public void actualizarStock(Long varianteId, Integer cantidad) {
        VarianteProducto variante = varianteRepository.findById(varianteId)
            .orElseThrow(() -> new RuntimeException("Variante no encontrada"));
        
        Integer nuevoStock = variante.getStock() + cantidad;
        if (nuevoStock < 0) {
            throw new RuntimeException("Stock insuficiente");
        }
        variante.setStock(nuevoStock);
        varianteRepository.save(variante);
    }
    
    @Override
    public void desactivarVariante(Long id) {
        VarianteProducto variante = varianteRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Variante no encontrada"));
        variante.setActivo(false);
        varianteRepository.save(variante);
    }
}
