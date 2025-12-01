package com.integrador.sistema_de_ventas.springboot_app.dto;
import org.springframework.web.multipart.MultipartFile;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class ProductoUpdateDTO {
    private String nombre;
    private String descripcion;
    private Long categoriaId;
    private Boolean activo;
    private MultipartFile imagen; 
}
