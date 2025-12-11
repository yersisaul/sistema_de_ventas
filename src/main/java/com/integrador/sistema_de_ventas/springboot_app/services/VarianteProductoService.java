package com.integrador.sistema_de_ventas.springboot_app.services;

import com.integrador.sistema_de_ventas.springboot_app.dto.VarianteUpdateDTO;
import com.integrador.sistema_de_ventas.springboot_app.models.VarianteProducto;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface VarianteProductoService {
    VarianteProducto crearVariante(VarianteProducto variante);
    Optional<VarianteProducto> obtenerVariantePorId(Long id);
    List<VarianteProducto> obtenerVariantesPorProducto(Long productoId);
    List<VarianteProducto> obtenerVariantesConStock(Long productoId);
    List<VarianteProducto> obtenerVariantesStockBajo();
    Optional<VarianteProducto> obtenerVariantePorProductoTalla(Long productoId, String talla);
    VarianteProducto actualizarVariante(Long id, VarianteUpdateDTO variante);
    void actualizarStock(Long varianteId, Integer cantidad);
    void desactivarVariante(Long id);
    List<VarianteProducto> obtenerTodasLasVariantes();
    void agregarOActualizarStock(Long productoId, String talla, Integer cantidad, BigDecimal precio);
}
