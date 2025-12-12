package com.integrador.sistema_de_ventas.springboot_app.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import java.time.LocalDateTime;
import java.util.List;

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

    // JsonProperty es opcional si usas SnakeCaseStrategy, pero no hace daño
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

    // AQUI ESTA LA MAGIA: Mapeamos el Enum al String de la BD
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Rol rol; 

    @Column(nullable = false)
    private Boolean estado = true;

    @Column
    private Integer puntosFidelizacion = 0;

    @Column
    private Boolean eliminado = false;

    @Column(nullable = false, updatable = false)
    private LocalDateTime fechaRegistro;

    @Column(nullable = false)
    private LocalDateTime fechaActualizacion;

    // RELACIONES (Usamos @JsonIgnore para evitar bucles infinitos al serializar)
    
    @OneToMany(mappedBy = "cliente", cascade = CascadeType.ALL)
    @JsonIgnore 
    @ToString.Exclude
    private List<Pedido> pedidos;

    @OneToMany(mappedBy = "cliente", cascade = CascadeType.ALL)
    @JsonIgnore
    @ToString.Exclude
    private List<Reseña> reseñas;

    @OneToMany(mappedBy = "usuarioEmpleado", cascade = CascadeType.ALL)
    @JsonIgnore
    @ToString.Exclude
    private List<Envio> envios;

    // EVENTOS DEL CICLO DE VIDA JPA
    @PrePersist
    protected void onCreate() {
        fechaRegistro = LocalDateTime.now();
        fechaActualizacion = LocalDateTime.now();
        if (estado == null) estado = true;
        if (eliminado == null) eliminado = false;
        if (puntosFidelizacion == null) puntosFidelizacion = 0;
    }

    @PreUpdate
    protected void onUpdate() {
        fechaActualizacion = LocalDateTime.now();
    }
    
    // DEFINICIÓN DE ROLES DENTRO DE LA CLASE O EN ARCHIVO APARTE
    public enum Rol {
        ADMINISTRADOR,
        CLIENTE,
        VENDEDOR
    }
}