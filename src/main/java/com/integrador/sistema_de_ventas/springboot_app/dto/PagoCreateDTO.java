package com.integrador.sistema_de_ventas.springboot_app.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PagoCreateDTO {
    private Long pedidoId;
    private String metodoPago;
    private String referenciaPago;
    private String datosPago; // JSON en formato String
    private Double monto;

}
