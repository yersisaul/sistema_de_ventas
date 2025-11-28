package com.integrador.sistema_de_ventas.springboot_app.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PedidoDTO {
    private Long id;
    private Long clienteId;
    private LocalDateTime fecha;
    private BigDecimal total;
    private BigDecimal subtotal;
    private BigDecimal impuestos;
    private BigDecimal costoEnvio;
    private String estado;
    private String direccionEnvio;
    private String telefonoContacto;
    private List<DetalleCarritoDTO> detalles;
}
