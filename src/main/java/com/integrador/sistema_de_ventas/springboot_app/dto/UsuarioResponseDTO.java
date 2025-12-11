package com.integrador.sistema_de_ventas.springboot_app.dto;

import com.integrador.sistema_de_ventas.springboot_app.models.Usuario;
import lombok.Data;

@Data
public class UsuarioResponseDTO {

    private Long idUsuario;
    private String tipoIdentificacion;
    private String nIdentificacion;
    private String nombres;
    private String apellidos;
    private String telefono;
    private String correo;
    private String direccion;

    public UsuarioResponseDTO(Usuario usuario) {
        this.idUsuario = usuario.getId();
        this.tipoIdentificacion = usuario.getTipoIdentificacion();
        this.nIdentificacion = usuario.getNIdentificacion();
        this.nombres = usuario.getNombres();
        this.apellidos = usuario.getApellidos();
        this.telefono = usuario.getTelefono();
        this.correo = usuario.getCorreo();
        this.direccion = usuario.getDireccion();
    }
}
