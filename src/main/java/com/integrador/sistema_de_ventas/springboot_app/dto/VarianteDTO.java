package com.integrador.sistema_de_ventas.springboot_app.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.integrador.sistema_de_ventas.springboot_app.models.VarianteProducto;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VarianteDTO {
    private Long id;
    private String talla;
    private Integer stock;
    private BigDecimal precio;

    // Convertir desde la entidad
    public static VarianteDTO fromVariante(VarianteProducto variante) {
        VarianteDTO dto = new VarianteDTO();
        dto.setId(variante.getId());
        dto.setTalla(variante.getTalla());
        dto.setStock(variante.getStock());
        dto.setPrecio(variante.getPrecio());
        return dto;
    }

}
