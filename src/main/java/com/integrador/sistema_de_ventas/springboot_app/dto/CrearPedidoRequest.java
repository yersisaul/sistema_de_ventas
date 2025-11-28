package com.integrador.sistema_de_ventas.springboot_app.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CrearPedidoRequest {
    private Long clienteId;
    private String direccionEnvio;
    private String telefonoContacto;
    private String notas;
    private BigDecimal subtotal;
    private BigDecimal impuestos;
    private BigDecimal costoEnvio;
    private BigDecimal total;
}
