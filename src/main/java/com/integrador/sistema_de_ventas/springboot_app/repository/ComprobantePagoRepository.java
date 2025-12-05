package com.integrador.sistema_de_ventas.springboot_app.repository;

import com.integrador.sistema_de_ventas.springboot_app.models.ComprobantePago;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ComprobantePagoRepository extends JpaRepository<ComprobantePago, Long> {
    // usar el objeto Pago en lugar de solo ID para JPA
    List<ComprobantePago> findByPagoId(Long pagoId);
      // Opcional: buscar por nombre de archivo
    ComprobantePago findByNombreArchivo(String nombreArchivo);

}

