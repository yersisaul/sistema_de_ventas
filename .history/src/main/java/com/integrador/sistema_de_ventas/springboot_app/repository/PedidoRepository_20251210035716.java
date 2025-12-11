package com.integrador.sistema_de_ventas.springboot_app.repository;

import com.integrador.sistema_de_ventas.springboot_app.models.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Long> {
    List<Pedido> findByClienteId(Long clienteId);
    List<Pedido> findByEstado(String estado);
    List<Pedido> findByClienteIdAndEstado(Long clienteId, String estado);
    
    @Query("SELECT p FROM Pedido p WHERE p.fecha BETWEEN :fechaInicio AND :fechaFin")
    List<Pedido> findPedidosPorFecha(@Param("fechaInicio") LocalDateTime fechaInicio, @Param("fechaFin") LocalDateTime fechaFin);
    
    @Query("SELECT p FROM Pedido p WHERE p.cliente.id = :clienteId ORDER BY p.fecha DESC")
    List<Pedido> findPedidosClienteOrdenados(@Param("clienteId") Long clienteId);
    /**
     * 🆕 Contar el total de pedidos en la base de datos
     * Usado para generar números correlativos de comprobantes
     */
    @Query("SELECT COUNT(p) FROM Pedido p")
    Long contarTotalPedidos();
    
    /**
     * 🆕 Obtener el último pedido (opcional, útil para debugging)
     */
    @Query("SELECT p FROM Pedido p ORDER BY p.fecha DESC, p.id DESC")
    List<Pedido> findUltimoPedido();
}
