package com.integrador.sistema_de_ventas.springboot_app.repository;

import com.integrador.sistema_de_ventas.springboot_app.models.ImagenProducto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface ImagenProductoRepository extends JpaRepository<ImagenProducto, Long> {
    List<ImagenProducto> findByProductoId(Long productoId);
    List<ImagenProducto> findByVarianteProductoId(Long varianteProductoId);
    
    @Query("SELECT i FROM ImagenProducto i WHERE i.producto.id = :productoId AND i.esPrincipal = true")
    Optional<ImagenProducto> findImagenPrincipalByProducto(@Param("productoId") Long productoId);
    
    @Query("SELECT i FROM ImagenProducto i WHERE i.varianteProducto.id = :varianteId ORDER BY i.orden ASC")
    List<ImagenProducto> findByVarianteOrdenadas(@Param("varianteId") Long varianteId);
}
