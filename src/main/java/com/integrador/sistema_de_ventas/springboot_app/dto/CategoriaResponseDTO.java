package com.integrador.sistema_de_ventas.springboot_app.dto;

import com.integrador.sistema_de_ventas.springboot_app.models.Categoria;
import lombok.Data;

@Data
public class CategoriaResponseDTO {
    private String nombre;
    
    public static CategoriaResponseDTO fromCategoria(Categoria categoria) {
        if (categoria == null) {
            return null;
        }
        
        CategoriaResponseDTO dto = new CategoriaResponseDTO();
        dto.setNombre(categoria.getNombre());
        
        return dto;
    }
}
