package com.integrador.sistema_de_ventas.springboot_app.services;

import com.integrador.sistema_de_ventas.springboot_app.dto.ClienteDTO;
import com.integrador.sistema_de_ventas.springboot_app.models.Usuario;
import java.util.List;
import java.util.Optional;

public interface UsuarioService {
    Usuario crearUsuario(Usuario usuario);
    Optional<Usuario> obtenerUsuarioPorId(Long id);
    Optional<Usuario> obtenerUsuarioPorCorreo(String correo);
    Optional<Usuario> obtenerUsuarioPorIdentificacion(String nIdentificacion);
    List<Usuario> obtenerTodosLosUsuarios();
    List<Usuario> obtenerUsuariosPorRol(String rol);
    List<Usuario> obtenerUsuariosActivos();
    Usuario actualizarUsuario(Long id, Usuario usuario);
    void desactivarUsuario(Long id);
    void eliminarUsuario(Long id);
    Boolean validarCredenciales(String nIdentificacion, String contrasena);
    
    /**
     * Obtener todos los clientes en formato DTO
     */
    List<ClienteDTO> obtenerClientesDTO();
}