package com.integrador.sistema_de_ventas.springboot_app.dto;
import lombok.Data;

@Data
public class UsuarioCreateDTO {
    private String tipoDocumento;
    private String numeroDocumento;
    private String nombre;
    private String apellidoPaterno;
    private String apellidoMaterno;
    private String telefono;
    private String correo;
    private String direccion;
    private String contraseña; 
}
