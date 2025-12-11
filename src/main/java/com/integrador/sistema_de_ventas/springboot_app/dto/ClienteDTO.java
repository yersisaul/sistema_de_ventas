package com.integrador.sistema_de_ventas.springboot_app.dto;

import com.integrador.sistema_de_ventas.springboot_app.models.Usuario;

public class ClienteDTO {
    private Long id;
    private String nombre;
    private String apellidos;
    private String tipoIdentificacion;
    private String nIdentificacion;
    private String correo;
    private String telefono;
    private String direccion;

    // Constructor vacío
    public ClienteDTO() {}

    // Constructor con parámetros
    public ClienteDTO(Long id, String nombre, String apellidos, String tipoIdentificacion, 
                     String nIdentificacion, String correo, String telefono, String direccion) {
        this.id = id;
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.tipoIdentificacion = tipoIdentificacion;
        this.nIdentificacion = nIdentificacion;
        this.correo = correo;
        this.telefono = telefono;
        this.direccion = direccion;
    }
    public ClienteDTO(Usuario usuario) {
        this.id = usuario.getId();
        this.nombre = usuario.getNombres();
        this.apellidos = usuario.getApellidos();
        this.tipoIdentificacion = usuario.getTipoIdentificacion();
        this.nIdentificacion = usuario.getNIdentificacion();
        this.correo = usuario.getCorreo();
        this.telefono = usuario.getTelefono();
        this.direccion = usuario.getDireccion();
    }
    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getTipoIdentificacion() {
        return tipoIdentificacion;
    }

    public void setTipoIdentificacion(String tipoIdentificacion) {
        this.tipoIdentificacion = tipoIdentificacion;
    }

    public String getNIdentificacion() {
        return nIdentificacion;
    }

    public void setNIdentificacion(String nIdentificacion) {
        this.nIdentificacion = nIdentificacion;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    // Método auxiliar para obtener nombre completo
    public String getNombreCompleto() {
        return nombre + " " + apellidos;
    }

    // Método auxiliar para obtener documento formateado
    public String getDocumentoFormateado() {
        return tipoIdentificacion + ": " + nIdentificacion;
    }
}