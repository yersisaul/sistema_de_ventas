package com.integrador.sistema_de_ventas.springboot_app.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "envio")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Envio {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "pedido_id", nullable = false)
    private Pedido pedido;
    
    @ManyToOne
    @JoinColumn(name = "usuario_empleado_id")
    private Usuario usuarioEmpleado;
    
    @Column(nullable = false, length = 500)
    private String direccion;
    
    @Column(nullable = false, length = 12)
    private String contacto;
    
    @Column(nullable = false, length = 20)
    private String estado = "PENDIENTE";
    
    @Column
    private LocalDate fechaEnvio;
    
    @Column(length = 100)
    private String numeroGuia;
    
    @Column(length = 100)
    private String transportista;
    
    @Column(nullable = false, updatable = false)
    private LocalDateTime fechaCreacion = LocalDateTime.now();
    
    @Column(nullable = false)
    private LocalDateTime fechaActualizacion = LocalDateTime.now();
}
