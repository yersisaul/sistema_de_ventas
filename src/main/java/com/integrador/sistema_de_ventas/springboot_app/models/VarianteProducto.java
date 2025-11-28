package com.integrador.sistema_de_ventas.springboot_app.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "variante_producto", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"producto_id", "talla", "color"})
})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class VarianteProducto {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "producto_id", nullable = false)
    private Producto producto;
    
    @Column(nullable = false, length = 2)
    private String talla;
    
    @Column(length = 50)
    private String color;
    
    @Column(nullable = false)
    private Integer stock = 0;
    
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal precio;
    
    @Column(precision = 10, scale = 2)
    private BigDecimal precioDescuento;
    
    @Column(length = 100)
    private String skuVariante;
    
    @Column
    private Boolean activo = true;
    
    @OneToMany(mappedBy = "varianteProducto", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ImagenProducto> imagenes;
    
    @OneToMany(mappedBy = "varianteProducto", cascade = CascadeType.ALL)
    private List<DetallePedido> detallesPedido; 
}
