package com.integrador.sistema_de_ventas.springboot_app.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "producto")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Producto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(unique = true, length = 10)
    private String sku;
    
    @Column(nullable = false, length = 200)
    private String nombre;
    
    @Column(columnDefinition = "TEXT")
    private String descripcion;
    
    @ManyToOne // Una categoria puede tener muchos productos
    @JoinColumn(name = "categoria_id")
    private Categoria categoria;

    @Column(nullable = false, length = 500)
    private String url_imagen;
    
    @Column
    private Boolean activo = true;
    
    @Column
    private Boolean eliminado = false;
    
    @Column(nullable = false, updatable = false)
    private LocalDateTime fechaCreacion = LocalDateTime.now();
    
    @Column(nullable = false)
    private LocalDateTime fechaActualizacion = LocalDateTime.now();
    
    @OneToMany(mappedBy = "producto", cascade = CascadeType.ALL, fetch = FetchType.LAZY) // Una lista de variantes asociadas al producto
    private List<VarianteProducto> variantes;
    
    @OneToMany(mappedBy = "producto", cascade = CascadeType.ALL) // Una lista de reseñas asociadas al producto
    private List<Reseña> reseñas;
}
