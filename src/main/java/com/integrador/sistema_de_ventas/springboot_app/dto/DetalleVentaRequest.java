package com.integrador.sistema_de_ventas.springboot_app.dto;

import lombok.Data;

@Data
public class DetalleVentaRequest {
    private Long productoId;
    private Integer cantidad;
    private Double precioUnitario;
    private Double subtotal;
}