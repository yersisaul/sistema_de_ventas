package com.integrador.sistema_de_ventas.springboot_app.repository;

import com.integrador.sistema_de_ventas.springboot_app.models.Reseña;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface ReseñaRepository extends JpaRepository<Reseña, Long> {
    List<Reseña> findByProductoId(Long productoId);
    List<Reseña> findByClienteId(Long clienteId);
    Optional<Reseña> findByClienteIdAndProductoId(Long clienteId, Long productoId);
    List<Reseña> findByProductoIdAndAprobada(Long productoId, Boolean aprobada);
    
    @Query("SELECT AVG(r.calificacion) FROM Reseña r WHERE r.producto.id = :productoId AND r.aprobada = true")
    Double findCalificacionPromedio(@Param("productoId") Long productoId);
    
    @Query("SELECT r FROM Reseña r WHERE r.aprobada = false ORDER BY r.fechaCreacion ASC")
    List<Reseña> findReseñasPendientesAprobacion();
}
