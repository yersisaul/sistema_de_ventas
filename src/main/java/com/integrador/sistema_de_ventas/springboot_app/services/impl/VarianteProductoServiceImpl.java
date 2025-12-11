package com.integrador.sistema_de_ventas.springboot_app.services.impl;

import com.integrador.sistema_de_ventas.springboot_app.dto.VarianteUpdateDTO;
import com.integrador.sistema_de_ventas.springboot_app.models.Producto;
import com.integrador.sistema_de_ventas.springboot_app.models.VarianteProducto;
import com.integrador.sistema_de_ventas.springboot_app.repository.ProductoRepository;
import com.integrador.sistema_de_ventas.springboot_app.repository.VarianteProductoRepository;
import com.integrador.sistema_de_ventas.springboot_app.services.VarianteProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class VarianteProductoServiceImpl implements VarianteProductoService {
    
    @Autowired
    private VarianteProductoRepository varianteRepository;

    @Autowired
    private ProductoRepository productoRepository;    
    
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
    public Optional<VarianteProducto> obtenerVariantePorProductoTalla(Long productoId, String talla) {
        return varianteRepository.findByProductoIdAndTalla(productoId, talla);
    }
    
    @Override
    public VarianteProducto actualizarVariante(Long id, VarianteUpdateDTO varianteActualizada) {
        VarianteProducto variante = varianteRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Variante no encontrada"));
        
        variante.setStock(varianteActualizada.getStock());
        variante.setPrecio(varianteActualizada.getPrecio());
        
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

    @Override
    public List<VarianteProducto> obtenerTodasLasVariantes() {
        return varianteRepository.findAll();
    }

    @Override
    public void agregarOActualizarStock(Long productoId, String talla, Integer cantidad, BigDecimal precio) {
        
        if (cantidad == null || cantidad <= 0) {
            return;
        }

        Optional<VarianteProducto> varianteOpt = varianteRepository.findByProductoIdAndTalla(productoId, talla);

        if (varianteOpt.isPresent()) {
            VarianteProducto variante = varianteOpt.get();
            
            Integer stockActual = variante.getStock();
            Integer nuevoStockTotal = stockActual + cantidad; //
            
            variante.setStock(nuevoStockTotal);
            
            if (precio != null && precio.compareTo(BigDecimal.ZERO) > 0) {
                variante.setPrecio(precio);
            }
            
            varianteRepository.save(variante);
            
        } else {

            Producto producto = productoRepository.findById(productoId)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado con ID: " + productoId));

            VarianteProducto nuevaVariante = new VarianteProducto();
            nuevaVariante.setProducto(producto);
            nuevaVariante.setTalla(talla);
            nuevaVariante.setStock(cantidad); 
            
            nuevaVariante.setPrecio(precio != null ? precio : BigDecimal.ZERO);
            nuevaVariante.setActivo(true);
            
            varianteRepository.save(nuevaVariante);
        }
    }
}
