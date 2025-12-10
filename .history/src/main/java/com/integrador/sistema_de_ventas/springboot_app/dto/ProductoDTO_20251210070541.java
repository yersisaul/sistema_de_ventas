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
    private BigDecimal precio;
    private Integer stock;
    
    // Métodos auxiliares para el modal de ventas
    public String getStockBadgeClass() {
        if (stock == null || stock == 0) return "stock-agotado";
        if (stock < 10) return "stock-bajo";
        return "stock-bueno";
    }

    public String getStockTexto() {
        if (stock == null || stock == 0) return "Agotado";
        if (stock < 10) return "Stock bajo: " + stock;
        return "Stock: " + stock;
    }
    
    public String getCategoriaNombre() {
        return categoria != null ? categoria.getNombre() : "Sin categoría";
    }
}