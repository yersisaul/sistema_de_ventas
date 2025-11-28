package com.integrador.sistema_de_ventas.springboot_app.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReseñaDTO {
    private Long id;
    private Long clienteId;
    private String nombreCliente;
    private Long productoId;
    private Integer calificacion;
    private String comentario;
    private LocalDateTime fechaCreacion;
    private Boolean aprobada;
}
