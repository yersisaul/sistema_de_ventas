package com.integrador.sistema_de_ventas.springboot_app.services;

import com.integrador.sistema_de_ventas.springboot_app.models.VarianteProducto;
import java.util.List;
import java.util.Optional;

public interface VarianteProductoService {
    VarianteProducto crearVariante(VarianteProducto variante);
    Optional<VarianteProducto> obtenerVariantePorId(Long id);
    List<VarianteProducto> obtenerVariantesPorProducto(Long productoId);
    List<VarianteProducto> obtenerVariantesConStock(Long productoId);
    List<VarianteProducto> obtenerVariantesStockBajo();
    Optional<VarianteProducto> obtenerVariantePorProductoTallaColor(Long productoId, String talla, String color);
    VarianteProducto actualizarVariante(Long id, VarianteProducto variante);
    void actualizarStock(Long varianteId, Integer cantidad);
    void desactivarVariante(Long id);
}
