package com.integrador.sistema_de_ventas.springboot_app.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@Entity
@Table(name = "usuario")
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class Usuario {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "tipo_identificacion", nullable = false, length = 3)
    private String tipoIdentificacion;
    
    @JsonProperty("n_identificacion")
    @Column(name = "n_identificacion", nullable = false, unique = true, length = 12)
    private String nIdentificacion;
    
    @Column(nullable = false, length = 100)
    private String nombres;
    
    @Column(nullable = false, length = 100)
    private String apellidos;
    
    @Column(nullable = false, length = 12)
    private String telefono;
    
    @Column(nullable = false, unique = true, length = 150)
    private String correo;
    
    @Column(nullable = false, length = 300)
    private String direccion;
    
    @Column(nullable = false, length = 255)
    private String contrasena;
    
    @Column(nullable = false, length = 20)
    private String rol = "CLIENTE";
    
    @Column(nullable = false)
    private Boolean estado = true;
    
    @Column
    private Integer puntosFidelizacion = 0;
    
    @Column
    private Boolean eliminado = false;
    
    @Column(nullable = false, updatable = false)
    private LocalDateTime fechaRegistro = LocalDateTime.now();
    
    @Column(nullable = false)
    private LocalDateTime fechaActualizacion = LocalDateTime.now();
    
    @OneToMany(mappedBy = "cliente", cascade = CascadeType.ALL)
    private List<Pedido> pedidos;
    
    @OneToMany(mappedBy = "cliente", cascade = CascadeType.ALL)
    private List<Reseña> reseñas;
    
    @OneToMany(mappedBy = "usuarioEmpleado", cascade = CascadeType.ALL)
    private List<Envio> envios;
}
