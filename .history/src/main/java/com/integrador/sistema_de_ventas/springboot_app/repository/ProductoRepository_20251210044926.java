package com.integrador.sistema_de_ventas.springboot_app.repository;

import com.integrador.sistema_de_ventas.springboot_app.models.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {
    Optional<Producto> findBySku(String sku);
    List<Producto> findByActivoAndEliminado(Boolean activo, Boolean eliminado);
    List<Producto> findByCategoriaIdAndActivoAndEliminado(Long categoriaId, Boolean activo, Boolean eliminado);

    @Query("UPDATE Producto p SET p.sku = :sku WHERE p.id = :id")
    void actualizarSku(@Param("id") Long id, @Param("sku") String sku);

    @Query("SELECT p FROM Producto p WHERE p.nombre LIKE %:nombre% AND p.activo = true AND p.eliminado = false")
    List<Producto> searchByNombre(@Param("nombre") String nombre);
    
    @Query("SELECT p FROM Producto p WHERE p.categoria.id = :categoriaId AND p.activo = true AND p.eliminado = false")
    List<Producto> findByCategoriaActivos(@Param("categoriaId") Long categoriaId);
    long countByCategoriaId(Long categoriaId);

    @Query("SELECT p FROM Producto p WHERE p.activo = true AND p.eliminado = false " +
           "AND (LOWER(p.nombre) LIKE LOWER(CONCAT('%', :busqueda, '%')) " +
           "OR LOWER(p.descripcion) LIKE LOWER(CONCAT('%', :busqueda, '%')) " +
           "OR LOWER(p.sku) LIKE LOWER(CONCAT('%', :busqueda, '%')))")
    List<Producto> buscarProductosVentas(@Param("busqueda") String busqueda);
    
}
