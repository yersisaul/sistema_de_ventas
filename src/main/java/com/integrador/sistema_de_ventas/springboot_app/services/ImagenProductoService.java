package com.integrador.sistema_de_ventas.springboot_app.services;

import com.integrador.sistema_de_ventas.springboot_app.models.ImagenProducto;
import java.util.List;
import java.util.Optional;

public interface ImagenProductoService {
    ImagenProducto crearImagen(ImagenProducto imagen);
    Optional<ImagenProducto> obtenerImagenPorId(Long id);
    List<ImagenProducto> obtenerImagenesPorProducto(Long productoId);
    List<ImagenProducto> obtenerImagenesPorVariante(Long varianteId);
    Optional<ImagenProducto> obtenerImagenPrincipal(Long productoId);
    ImagenProducto actualizarImagen(Long id, ImagenProducto imagen);
    void establecerImagenPrincipal(Long imagenId);
    void eliminarImagen(Long id);
}
