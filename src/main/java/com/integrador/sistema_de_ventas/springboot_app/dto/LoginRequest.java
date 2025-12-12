package com.integrador.sistema_de_ventas.springboot_app.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class LoginRequest {
    @JsonProperty("n_identificacion") // Para que coincida con el JSON del frontend
    private String nIdentificacion;
    private String contrasena;
}