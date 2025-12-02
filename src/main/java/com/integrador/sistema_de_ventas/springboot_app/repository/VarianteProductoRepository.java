package com.integrador.sistema_de_ventas.springboot_app.repository;

import com.integrador.sistema_de_ventas.springboot_app.models.VarianteProducto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface VarianteProductoRepository extends JpaRepository<VarianteProducto, Long> {
    List<VarianteProducto> findByProductoId(Long productoId);
    List<VarianteProducto> findByProductoIdAndActivo(Long productoId, Boolean activo);
    Optional<VarianteProducto> findByProductoIdAndTalla(Long productoId, String talla);

    
    @Query("SELECT v FROM VarianteProducto v WHERE v.producto.id = :productoId AND v.stock > 0 AND v.activo = true")
    List<VarianteProducto> findVariantesConStock(@Param("productoId") Long productoId);
    
    @Query("SELECT v FROM VarianteProducto v WHERE v.stock < 10 AND v.activo = true")
    List<VarianteProducto> findVariantesStockBajo();

}
