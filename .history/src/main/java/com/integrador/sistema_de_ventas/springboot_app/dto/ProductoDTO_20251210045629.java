package com.integrador.sistema_de_ventas.springboot_app.dto;

import com.integrador.sistema_de_ventas.springboot_app.models.Producto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductoDTO {
    private Long id;
    private String sku;
    private String nombre;
    private String descripcion;
    private CategoriaResponseDTO categoria;
    private String url_imagen;
    private List<VarianteDTO> variantes;
    private Double calificacionPromedio;
    private BigDecimal precioMinimo;
    private Integer totalReseñas;
}