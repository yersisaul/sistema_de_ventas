package com.integrador.sistema_de_ventas.springboot_app.dto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductoCreateDTO {
    private String sku;
    private String nombre;
    private String descripcion;
    private Long categoriaId; // Solo el ID de la categoría
    private Boolean activo = true;

}
