package com.integrador.sistema_de_ventas.springboot_app.repository;

import com.integrador.sistema_de_ventas.springboot_app.models.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Long> {
    
    // --- Métodos Generales ---
    List<Pedido> findByClienteId(Long clienteId);
    List<Pedido> findByEstado(String estado);
    List<Pedido> findByClienteIdAndEstado(Long clienteId, String estado);
    
    @Query("SELECT p FROM Pedido p WHERE p.fecha BETWEEN :fechaInicio AND :fechaFin")
    List<Pedido> findPedidosPorFecha(@Param("fechaInicio") LocalDateTime fechaInicio, @Param("fechaFin") LocalDateTime fechaFin);
    
    @Query("SELECT p FROM Pedido p WHERE p.cliente.id = :clienteId ORDER BY p.fecha DESC")
    List<Pedido> findPedidosClienteOrdenados(@Param("clienteId") Long clienteId);

    // --- MÉTODOS PARA EL DASHBOARD ---

    // 1. Suma de ventas de HOY (CORREGIDO: Usamos CAST para evitar el error de tipos)
    @Query("SELECT COALESCE(SUM(p.total), 0) FROM Pedido p WHERE CAST(p.fecha AS date) = CURRENT_DATE AND p.estado = 'COMPLETADO'")
    BigDecimal sumarVentasDia();

    // 2. Obtener los 5 pedidos más recientes (Para la tabla del dashboard)
    List<Pedido> findTop5ByOrderByFechaDesc();

    // 3. Gráfico de Ventas Mensuales (Query Nativa de PostgreSQL)
    @Query(value = "SELECT COALESCE(SUM(p.total), 0) FROM pedido p WHERE EXTRACT(MONTH FROM p.fecha) = :mes AND EXTRACT(YEAR FROM p.fecha) = EXTRACT(YEAR FROM CURRENT_DATE) AND p.estado = 'COMPLETADO'", nativeQuery = true)
    BigDecimal obtenerVentasPorMes(@Param("mes") int mes);
}