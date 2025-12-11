package com.integrador.sistema_de_ventas.springboot_app.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "pedido")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Pedido {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "cliente_id", nullable = false)
    private Usuario cliente;
    
    @Column(nullable = false, updatable = false)
    private LocalDateTime fecha = LocalDateTime.now();
    
    @Column(precision = 10, scale = 2)
    private BigDecimal total;
    
    @Column(precision = 10, scale = 2)
    private BigDecimal subtotal;
    
    @Column(precision = 10, scale = 2)
    private BigDecimal impuestos = BigDecimal.ZERO;
    
    @Column(precision = 10, scale = 2)
    private BigDecimal costoEnvio = BigDecimal.ZERO;
    
    @Column(nullable = false, length = 20)
    private String estado = "PENDIENTE";
    
    @Column(nullable = false, length = 500)
    private String direccionEnvio;
    
    @Column(nullable = false, length = 12)
    private String telefonoContacto;
    
    @Column(columnDefinition = "TEXT")
    private String notas;
    
    @Column(nullable = false)
    private LocalDateTime fechaActualizacion = LocalDateTime.now();
    
    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<DetallePedido> detalles;
    
    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL)
    private List<Pago> pagos;
    
    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL)
    private List<Envio> envios;
    
    @Column(length = 20)
    private String tipoComprobante;

    @Column(length = 10)
    private String serieComprobante;

    @Column(length = 20)
    private String numeroComprobante;

    @Column(length = 30)
    private String numeroComprobanteCompleto;
}
