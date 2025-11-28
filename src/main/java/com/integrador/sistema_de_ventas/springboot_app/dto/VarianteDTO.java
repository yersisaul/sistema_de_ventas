package com.integrador.sistema_de_ventas.springboot_app.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VarianteDTO {
    private Long id;
    private String talla;
    private String color;
    private Integer stock;
    private BigDecimal precio;
    private BigDecimal precioDescuento;
    private Boolean disponible;
}
