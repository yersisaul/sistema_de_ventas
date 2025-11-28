package com.integrador.sistema_de_ventas.springboot_app.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "reseña", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"cliente_id", "producto_id"})
})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Reseña {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "cliente_id", nullable = false)
    private Usuario cliente;
    
    @ManyToOne
    @JoinColumn(name = "producto_id", nullable = false)
    private Producto producto;
    
    @Column(nullable = false)
    private Integer calificacion;
    
    @Column(columnDefinition = "TEXT")
    private String comentario;
    
    @Column(nullable = false, updatable = false)
    private LocalDateTime fechaCreacion = LocalDateTime.now();
    
    @Column
    private Boolean aprobada = false;
}
