package com.integrador.sistema_de_ventas.springboot_app.dto;

import lombok.Data;

@Data
public class UsuarioCreateDTO {
    private String tipoIdentificacion; 
    private String nIdentificacion;   
    private String nombres;   
    private String apellidos;          
    private String telefono;
    private String correo;
    private String direccion;
    private String contrasena;
}
