package com.integrador.sistema_de_ventas.springboot_app.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginResponse {
    private Long id;
    private String nombres;
    private String apellidos;
    
    @JsonProperty("n_identificacion")
    private String nIdentificacion;
    
    private String correo;
    private String rol;
    private String mensaje;
}