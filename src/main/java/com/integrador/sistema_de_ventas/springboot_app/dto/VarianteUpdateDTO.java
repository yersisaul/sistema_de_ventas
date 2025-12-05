package com.integrador.sistema_de_ventas.springboot_app.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VarianteUpdateDTO {
    private Integer stock;
    private BigDecimal precio;
}
