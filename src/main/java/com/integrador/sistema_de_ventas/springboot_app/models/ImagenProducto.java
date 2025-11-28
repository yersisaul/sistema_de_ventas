package com.integrador.sistema_de_ventas.springboot_app.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "imagen_producto")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ImagenProducto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "variante_producto_id")
    private VarianteProducto varianteProducto;
    
    @ManyToOne
    @JoinColumn(name = "producto_id")
    private Producto producto;
    
    @Column(nullable = false, length = 500)
    private String url;
    
    @Column
    private Integer orden = 0;
    
    @Column
    private Boolean esPrincipal = false;
    
    @Column(nullable = false, updatable = false)
    private LocalDateTime fechaCreacion = LocalDateTime.now();
}
