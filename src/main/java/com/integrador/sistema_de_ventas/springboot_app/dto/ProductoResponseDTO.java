package com.integrador.sistema_de_ventas.springboot_app.dto;
import com.integrador.sistema_de_ventas.springboot_app.models.Producto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductoResponseDTO {
    private Long id;
    private String sku;
    private String nombre;
    private String descripcion;
    private CategoriaBasicDTO categoria;
    private Boolean activo;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaActualizacion;
    
    // Convertir desde la entidad
    public static ProductoResponseDTO fromProducto(Producto producto) {
        ProductoResponseDTO dto = new ProductoResponseDTO();
        dto.setId(producto.getId());
        dto.setSku(producto.getSku());
        dto.setNombre(producto.getNombre());
        dto.setDescripcion(producto.getDescripcion());
        dto.setActivo(producto.getActivo());
        dto.setFechaCreacion(producto.getFechaCreacion());
        dto.setFechaActualizacion(producto.getFechaActualizacion());
        
        if (producto.getCategoria() != null) {
            dto.setCategoria(new CategoriaBasicDTO(
                producto.getCategoria().getId(),
                producto.getCategoria().getNombre()
            ));
        }
        
        return dto;
    }
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CategoriaBasicDTO {
        private Long id;
        private String nombre;
    }
}
