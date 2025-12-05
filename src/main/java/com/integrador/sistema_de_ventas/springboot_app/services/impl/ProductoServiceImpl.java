package com.integrador.sistema_de_ventas.springboot_app.services.impl;

import com.integrador.sistema_de_ventas.springboot_app.models.Categoria;
import com.integrador.sistema_de_ventas.springboot_app.models.Producto;
import com.integrador.sistema_de_ventas.springboot_app.models.VarianteProducto;
import com.integrador.sistema_de_ventas.springboot_app.dto.ProductoCreateDTO;
import com.integrador.sistema_de_ventas.springboot_app.dto.ProductoUpdateDTO;
import com.integrador.sistema_de_ventas.springboot_app.repository.ProductoRepository;
import com.integrador.sistema_de_ventas.springboot_app.repository.CategoriaRepository;
import com.integrador.sistema_de_ventas.springboot_app.services.GuardadoImgService;
import com.integrador.sistema_de_ventas.springboot_app.services.ProductoService;
import com.integrador.sistema_de_ventas.springboot_app.services.VarianteProductoService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
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

    @Autowired
    private GuardadoImgService guardadoImgService;

    @Autowired
    private VarianteProductoService varianteService;
    
    @Override
    public Producto crearProducto(ProductoCreateDTO productoDTO) {
        Producto producto = new Producto();
        producto.setNombre(productoDTO.getNombre());
        producto.setDescripcion(productoDTO.getDescripcion());
        producto.setImagen(productoDTO.getImagen() != null ? productoDTO.getImagen().getOriginalFilename() : null);
        producto.setFechaCreacion(LocalDateTime.now());
        producto.setFechaActualizacion(LocalDateTime.now());
        producto.setActivo(true);
        producto.setEliminado(false);

        MultipartFile imagen = productoDTO.getImagen();
        if (productoDTO.getCategoriaId() != null) {
            Categoria categoria = categoriaRepository.findById(productoDTO.getCategoriaId())
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada con ID: " + productoDTO.getCategoriaId()));
            producto.setCategoria(categoria);
        }
        if (imagen != null && !imagen.isEmpty()) { // Usamos tu servicio para guardar el archivo en el disco
            String rutaImagen = guardadoImgService.guardarImagen(imagen); // Guardamos la ruta (ej: /uploads/productos/foto.jpg) en la base de datos
            producto.setImagen(rutaImagen); 
        }
        
        Producto savedProducto = productoRepository.save(producto);
        String skuGenerado = "PRD-" + String.format("%06d", savedProducto.getId());
        savedProducto.setSku(skuGenerado);

        // Creat un registro del nuevo producto por talla en la tabla variante_producto con valores por defecto
        for (String talla : new String[]{"S", "M", "L", "XL"})
        {
            VarianteProducto variante = new VarianteProducto();
            variante.setProducto(savedProducto);
            variante.setTalla(talla);
            variante.setStock(0);
            variante.setPrecio(BigDecimal.valueOf(0.00));
            variante.setActivo(true);
            VarianteProducto nuevaVariante = varianteService.crearVariante(variante);
        }
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
        MultipartFile nuevaImagen = productoActualizado.getImagen();


        if (nuevaImagen != null && !nuevaImagen.isEmpty()) {
            String rutaImagen = guardadoImgService.guardarImagen(nuevaImagen);
            producto.setImagen(rutaImagen);
        }

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
