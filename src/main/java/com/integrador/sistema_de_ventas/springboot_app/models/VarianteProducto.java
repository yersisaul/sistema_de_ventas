package com.integrador.sistema_de_ventas.springboot_app.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "variante_producto", uniqueConstraints = {
@UniqueConstraint(columnNames = {"producto_id", "talla"})
})

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VarianteProducto {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "producto_id", nullable = false) // Un producto puede tener muchas variantes
    private Producto producto;
    
    @Column(nullable = false, length = 2)
    private String talla;
    
    @Column(nullable = false)
    private Integer stock = 0;
    
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal precio;
       
    @Column
    private Boolean activo = true;
    
    // Una lista de detalles de pedido asociados a la variante del producto
    @OneToMany(mappedBy = "varianteProducto", cascade = CascadeType.ALL)
    private List<DetallePedido> detallesPedido; 
}
