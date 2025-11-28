package com.integrador.sistema_de_ventas.springboot_app.repository;

import com.integrador.sistema_de_ventas.springboot_app.models.Pago;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface PagoRepository extends JpaRepository<Pago, Long> {
    List<Pago> findByPedidoId(Long pedidoId);
    List<Pago> findByEstadoPago(String estadoPago);
    Optional<Pago> findByReferenciaPago(String referenciaPago);
    
    @Query("SELECT p FROM Pago p WHERE p.pedido.id = :pedidoId AND p.estadoPago = 'COMPLETADO'")
    List<Pago> findPagosCompletadosByPedido(@Param("pedidoId") Long pedidoId);
}
