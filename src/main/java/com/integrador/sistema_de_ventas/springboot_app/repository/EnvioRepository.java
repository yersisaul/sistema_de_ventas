package com.integrador.sistema_de_ventas.springboot_app.repository;

import com.integrador.sistema_de_ventas.springboot_app.models.Envio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface EnvioRepository extends JpaRepository<Envio, Long> {
    List<Envio> findByPedidoId(Long pedidoId);
    List<Envio> findByEstado(String estado);
    List<Envio> findByUsuarioEmpleadoId(Long usuarioEmpleadoId);
    
    @Query("SELECT e FROM Envio e WHERE e.estado = :estado ORDER BY e.fechaCreacion DESC")
    List<Envio> findEnviosPorEstado(@Param("estado") String estado);
}
