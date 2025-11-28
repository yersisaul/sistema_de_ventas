package com.integrador.sistema_de_ventas.springboot_app.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DetalleCarritoDTO {
    private Long varianteProductoId;
    private String nombreProducto;
    private String talla;
    private String color;
    private Integer cantidad;
    private BigDecimal precioUnitario;
    private BigDecimal subtotal;
}
