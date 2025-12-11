package com.integrador.sistema_de_ventas.springboot_app.dto;

import com.integrador.sistema_de_ventas.springboot_app.models.Usuario;

public class ClienteDTO {

    private Long id;
    private String nombres;
    private String apellidos;
    private String tipoIdentificacion;
    private String nIdentificacion;
    private String correo;
    private String telefono;
    private String direccion;
    private Boolean estado;
    private Integer puntosFidelizacion;
    private String rol;

    // Constructor vacío
    public ClienteDTO() {}

    // Constructor con parámetros
    public ClienteDTO(Long id, String nombres, String apellidos, String tipoIdentificacion,
                      String nIdentificacion, String correo, String telefono,
                      String direccion, Boolean estado, Integer puntosFidelizacion, String rol) {

        this.id = id;
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.tipoIdentificacion = tipoIdentificacion;
        this.nIdentificacion = nIdentificacion;
        this.correo = correo;
        this.telefono = telefono;
        this.direccion = direccion;
        this.estado = estado;
        this.puntosFidelizacion = puntosFidelizacion;
        this.rol = rol;
    }

    // Constructor desde entidad Usuario
    public ClienteDTO(Usuario usuario) {
        this.id = usuario.getId();
        this.nombres = usuario.getNombres();
        this.apellidos = usuario.getApellidos();
        this.tipoIdentificacion = usuario.getTipoIdentificacion();
        this.nIdentificacion = usuario.getNIdentificacion();
        this.correo = usuario.getCorreo();
        this.telefono = usuario.getTelefono();
        this.direccion = usuario.getDireccion();
        this.estado = usuario.getEstado();
        this.puntosFidelizacion = usuario.getPuntosFidelizacion();
        this.rol = usuario.getRol();
    }

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNombres() { return nombres; }
    public void setNombres(String nombres) { this.nombres = nombres; }

    public String getApellidos() { return apellidos; }
    public void setApellidos(String apellidos) { this.apellidos = apellidos; }

    public String getTipoIdentificacion() { return tipoIdentificacion; }
    public void setTipoIdentificacion(String tipoIdentificacion) { this.tipoIdentificacion = tipoIdentificacion; }

    public String getNIdentificacion() { return nIdentificacion; }
    public void setNIdentificacion(String nIdentificacion) { this.nIdentificacion = nIdentificacion; }

    public String getCorreo() { return correo; }
    public void setCorreo(String correo) { this.correo = correo; }

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }

    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }

    public Boolean getEstado() { return estado; }
    public void setEstado(Boolean estado) { this.estado = estado; }

    public Integer getPuntosFidelizacion() { return puntosFidelizacion; }
    public void setPuntosFidelizacion(Integer puntosFidelizacion) { this.puntosFidelizacion = puntosFidelizacion; }

    public String getRol() { return rol; }
    public void setRol(String rol) { this.rol = rol; }

    // Método auxiliar para nombre completo
    public String getNombreCompleto() {
        return nombres + " " + apellidos;
    }

    // Método auxiliar para documento formateado
    public String getDocumentoFormateado() {
        return tipoIdentificacion + ": " + nIdentificacion;
    }
}
