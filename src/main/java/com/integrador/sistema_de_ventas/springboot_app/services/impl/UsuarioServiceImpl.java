package com.integrador.sistema_de_ventas.springboot_app.services.impl;
<<<<<<< HEAD
import com.integrador.sistema_de_ventas.springboot_app.exception.BadRequestException;
import com.integrador.sistema_de_ventas.springboot_app.exception.ResourceNotFoundException;
import com.integrador.sistema_de_ventas.springboot_app.dto.UsuarioCreateDTO;
=======

import com.integrador.sistema_de_ventas.springboot_app.dto.ClienteDTO;
>>>>>>> c7f0a7c3c255bc5c87d2f6cfd406cbbc01c662ae
import com.integrador.sistema_de_ventas.springboot_app.models.Usuario;
import com.integrador.sistema_de_ventas.springboot_app.repository.UsuarioRepository;
import com.integrador.sistema_de_ventas.springboot_app.services.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import com.integrador.sistema_de_ventas.springboot_app.dto.UsuarioResponseDTO;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.stream.Collectors; 
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class UsuarioServiceImpl implements UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public Usuario crearUsuario(Usuario usuario) {
        if (usuarioRepository.findByCorreo(usuario.getCorreo()).isPresent()) {
            throw new BadRequestException("El correo ya está registrado");
        }
        if (usuarioRepository.findBynIdentificacion(usuario.getNIdentificacion()).isPresent()) {
            throw new BadRequestException("La identificación ya está registrada");
        }
        usuario.setContrasena(passwordEncoder.encode(usuario.getContrasena()));
        if (usuario.getRol() == null) usuario.setRol("CLIENTE"); 
        if (usuario.getEstado() == null) usuario.setEstado(true);
        if (usuario.getEliminado() == null) usuario.setEliminado(false);
        if (usuario.getPuntosFidelizacion() == null) usuario.setPuntosFidelizacion(0);
        usuario.setFechaRegistro(LocalDateTime.now());
        usuario.setFechaActualizacion(LocalDateTime.now());
        usuario.setEliminado(false);
        return usuarioRepository.save(usuario);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Usuario> obtenerUsuarioPorId(Long id) {
        return usuarioRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Usuario> obtenerUsuarioPorCorreo(String correo) {
        return usuarioRepository.findByCorreo(correo);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Usuario> obtenerUsuarioPorIdentificacion(String nIdentificacion) {
        return usuarioRepository.findBynIdentificacion(nIdentificacion);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Usuario> obtenerTodosLosUsuarios() {
        return usuarioRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Usuario> obtenerUsuariosPorRol(String rol) {
        return usuarioRepository.findActiveByRol(rol);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Usuario> obtenerUsuariosActivos() {
        return usuarioRepository.findByEstadoAndEliminado(true, false);
    }

    @Override
    public Usuario actualizarUsuario(Long id, Usuario usuarioActualizado) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        usuario.setNombres(usuarioActualizado.getNombres());
        usuario.setApellidos(usuarioActualizado.getApellidos());
        usuario.setTelefono(usuarioActualizado.getTelefono());
        usuario.setDireccion(usuarioActualizado.getDireccion());
        usuario.setFechaActualizacion(LocalDateTime.now());

        return usuarioRepository.save(usuario);
    }

    @Override
    public void desactivarUsuario(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        usuario.setEstado(false);
        usuario.setFechaActualizacion(LocalDateTime.now());
        usuarioRepository.save(usuario);
    }

    @Override
    public void eliminarUsuario(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        usuario.setEliminado(true);
        usuario.setFechaActualizacion(LocalDateTime.now());
        usuarioRepository.save(usuario);
    }

    @Override
    @Transactional(readOnly = true)
    public Boolean validarCredenciales(String nIdentificacion, String contrasena) {
        Optional<Usuario> usuario = usuarioRepository.findBynIdentificacion(nIdentificacion);

        if (usuario.isPresent() && usuario.get().getEstado() && !usuario.get().getEliminado()) {
            return passwordEncoder.matches(contrasena, usuario.get().getContrasena());
        }
        return false;
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<ClienteDTO> obtenerClientesDTO() {
        // Obtener clientes activos usando el método del repositorio
        List<Usuario> clientes = usuarioRepository.findByRolAndEstadoAndEliminadoFalse("CLIENTE", true);
        
        // Convertir a ClienteDTO usando el constructor
        return clientes.stream()
                .map(ClienteDTO::new)
                .collect(Collectors.toList());
    }
}
