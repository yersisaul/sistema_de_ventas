package com.integrador.sistema_de_ventas.springboot_app.services;

import com.integrador.sistema_de_ventas.springboot_app.models.Producto;
import com.integrador.sistema_de_ventas.springboot_app.dto.ProductoCreateDTO;
import com.integrador.sistema_de_ventas.springboot_app.dto.ProductoDTO;
import com.integrador.sistema_de_ventas.springboot_app.dto.ProductoUpdateDTO;

import java.util.List;
import java.util.Optional;

public interface ProductoService {
    Producto crearProducto(ProductoCreateDTO productoCreateDTO);
    Optional<Producto> obtenerProductoPorId(Long id);
    Optional<Producto> obtenerProductoPorSku(String sku);
    List<Producto> obtenerTodosLosProductos();
    List<Producto> obtenerProductosActivos();
    List<Producto> obtenerProductosPorCategoria(Long categoriaId);
    List<Producto> buscarProductosPorNombre(String nombre);
    Producto actualizarProducto(Long id, ProductoUpdateDTO producto);
    void desactivarProducto(Long id);
    void eliminarProducto(Long id);
    List<ProductoDTO> obtenerProductosParaVentas();
    List<ProductoDTO> buscarProductosParaVentas(String busqueda);
}
