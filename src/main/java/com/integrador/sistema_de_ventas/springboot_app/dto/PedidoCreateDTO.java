package com.integrador.sistema_de_ventas.springboot_app.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PedidoCreateDTO {
    private List<DetalleCarritoDTO> detalles;
    private Long clienteId;
    private BigDecimal costoEnvio;
    private String direccionEnvio;
    private String telefonoContacto;
    private String notas;
    private String tipoComprobante;
    private String metodoPago;
}
