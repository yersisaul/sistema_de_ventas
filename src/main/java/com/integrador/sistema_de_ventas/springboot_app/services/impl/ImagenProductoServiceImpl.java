package com.integrador.sistema_de_ventas.springboot_app.services.impl;

import com.integrador.sistema_de_ventas.springboot_app.models.ImagenProducto;
import com.integrador.sistema_de_ventas.springboot_app.repository.ImagenProductoRepository;
import com.integrador.sistema_de_ventas.springboot_app.services.ImagenProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ImagenProductoServiceImpl implements ImagenProductoService {
    
    @Autowired
    private ImagenProductoRepository imagenRepository;
    
    @Override
    public ImagenProducto crearImagen(ImagenProducto imagen) {
        imagen.setFechaCreacion(LocalDateTime.now());
        return imagenRepository.save(imagen);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Optional<ImagenProducto> obtenerImagenPorId(Long id) {
        return imagenRepository.findById(id);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<ImagenProducto> obtenerImagenesPorProducto(Long productoId) {
        return imagenRepository.findByProductoId(productoId);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<ImagenProducto> obtenerImagenesPorVariante(Long varianteId) {
        return imagenRepository.findByVarianteOrdenadas(varianteId);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Optional<ImagenProducto> obtenerImagenPrincipal(Long productoId) {
        return imagenRepository.findImagenPrincipalByProducto(productoId);
    }
    
    @Override
    public ImagenProducto actualizarImagen(Long id, ImagenProducto imagenActualizada) {
        ImagenProducto imagen = imagenRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Imagen no encontrada"));
        
        imagen.setUrl(imagenActualizada.getUrl());
        imagen.setOrden(imagenActualizada.getOrden());
        
        return imagenRepository.save(imagen);
    }
    
    @Override
    public void establecerImagenPrincipal(Long imagenId) {
        ImagenProducto imagen = imagenRepository.findById(imagenId)
            .orElseThrow(() -> new RuntimeException("Imagen no encontrada"));
        
        // Desactivar imagen principal anterior si existe
        if (imagen.getProducto() != null) {
            imagenRepository.findImagenPrincipalByProducto(imagen.getProducto().getId())
                .ifPresent(img -> {
                    img.setEsPrincipal(false);
                    imagenRepository.save(img);
                });
        }
        
        imagen.setEsPrincipal(true);
        imagenRepository.save(imagen);
    }
    
    @Override
    public void eliminarImagen(Long id) {
        imagenRepository.deleteById(id);
    }
}
