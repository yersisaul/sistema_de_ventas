package com.integrador.sistema_de_ventas.springboot_app.services.impl;

import com.integrador.sistema_de_ventas.springboot_app.models.Categoria;
import com.integrador.sistema_de_ventas.springboot_app.models.Producto;
import com.integrador.sistema_de_ventas.springboot_app.dto.ProductoCreateDTO;
import com.integrador.sistema_de_ventas.springboot_app.dto.ProductoUpdateDTO;
import com.integrador.sistema_de_ventas.springboot_app.repository.ProductoRepository;
import com.integrador.sistema_de_ventas.springboot_app.repository.CategoriaRepository;
import com.integrador.sistema_de_ventas.springboot_app.services.ProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ProductoServiceImpl implements ProductoService {
    
    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private CategoriaRepository categoriaRepository;
    
    @Override
    public Producto crearProducto(ProductoCreateDTO productoDTO) {
        Producto producto = new Producto();
        producto.setNombre(productoDTO.getNombre());
        producto.setDescripcion(productoDTO.getDescripcion());
        producto.setFechaCreacion(LocalDateTime.now());
        producto.setFechaActualizacion(LocalDateTime.now());
        producto.setActivo(true);
        producto.setEliminado(false);
        
        if (productoDTO.getCategoriaId() != null) {
            Categoria categoria = categoriaRepository.findById(productoDTO.getCategoriaId())
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada con ID: " + productoDTO.getCategoriaId()));
            producto.setCategoria(categoria);
        }
        
        Producto savedProducto = productoRepository.save(producto);
        String skuGenerado = "PRD-" + String.format("%06d", savedProducto.getId());
        savedProducto.setSku(skuGenerado);
        return productoRepository.save(savedProducto);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Optional<Producto> obtenerProductoPorId(Long id) {
        return productoRepository.findById(id);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Optional<Producto> obtenerProductoPorSku(String sku) {
        return productoRepository.findBySku(sku);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Producto> obtenerTodosLosProductos() {
        return productoRepository.findAll();
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Producto> obtenerProductosActivos() {
        return productoRepository.findByActivoAndEliminado(true, false);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Producto> obtenerProductosPorCategoria(Long categoriaId) {
        return productoRepository.findByCategoriaActivos(categoriaId);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Producto> buscarProductosPorNombre(String nombre) {
        return productoRepository.searchByNombre(nombre);
    }
    
    @Override
    public Producto actualizarProducto(Long id, ProductoUpdateDTO productoActualizado) {
        Producto producto = productoRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
        
        producto.setNombre(productoActualizado.getNombre());
        producto.setDescripcion(productoActualizado.getDescripcion());
        producto.setFechaActualizacion(LocalDateTime.now());

        if (productoActualizado.getCategoriaId() != null) {
            Categoria categoria = categoriaRepository.findById(productoActualizado.getCategoriaId())
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada con ID: " + productoActualizado.getCategoriaId()));
            producto.setCategoria(categoria);
        }        
        return productoRepository.save(producto);
    }
    
    @Override
    public void desactivarProducto(Long id) {
        Producto producto = productoRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
        producto.setActivo(false);
        producto.setFechaActualizacion(LocalDateTime.now());
        productoRepository.save(producto);
    }
    
    @Override
    public void eliminarProducto(Long id) {
        Producto producto = productoRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
        producto.setEliminado(true);
        producto.setFechaActualizacion(LocalDateTime.now());
        productoRepository.save(producto);
    }
}
