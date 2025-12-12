package com.integrador.sistema_de_ventas.springboot_app.services.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.integrador.sistema_de_ventas.springboot_app.models.Usuario;
import com.integrador.sistema_de_ventas.springboot_app.repository.UsuarioRepository;
import com.integrador.sistema_de_ventas.springboot_app.services.UsuarioService;

@Service
@Transactional
public class UsuarioServiceImpl implements UsuarioService {
    
    @Autowired
    private UsuarioRepository usuarioRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder; 
    
    // --- CREAR ---
    @Override
    public Usuario crearUsuario(Usuario usuario) {
        // Validaciones
        if (usuarioRepository.findByCorreo(usuario.getCorreo()).isPresent()) {
            throw new RuntimeException("El correo ya está registrado");
        }
        if (usuarioRepository.findByNIdentificacion(usuario.getNIdentificacion()).isPresent()) {
            throw new RuntimeException("La identificación ya está registrada");
        }
        
        // Datos por defecto
        usuario.setContrasena(passwordEncoder.encode(usuario.getContrasena()));
        usuario.setEstado(true);
        usuario.setEliminado(false);
        usuario.setPuntosFidelizacion(0);
        
        return usuarioRepository.save(usuario); // INSERT en la BD
    }
    
    // --- LECTURAS ---
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
        return usuarioRepository.findByNIdentificacion(nIdentificacion);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Usuario> obtenerTodosLosUsuarios() {
        return usuarioRepository.findAll();
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Usuario> obtenerUsuariosPorRol(Usuario.Rol rol) {
        // Esto trae solo los que NO están eliminados (SELECT ... WHERE eliminado = false)
        return usuarioRepository.findActiveByRol(rol);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Usuario> obtenerUsuariosActivos() {
        return usuarioRepository.findByEstadoAndEliminado(true, false);
    }
    
    // --- ACTUALIZAR EN BD ---
    @Override
    public Usuario actualizarUsuario(Long id, Usuario usuarioActualizado) {
        Usuario usuario = usuarioRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        
        // 1. Actualizamos los campos en el objeto recuperado de la BD
        usuario.setTipoIdentificacion(usuarioActualizado.getTipoIdentificacion());
        usuario.setNIdentificacion(usuarioActualizado.getNIdentificacion());
        usuario.setNombres(usuarioActualizado.getNombres());
        usuario.setApellidos(usuarioActualizado.getApellidos());
        usuario.setTelefono(usuarioActualizado.getTelefono());
        usuario.setCorreo(usuarioActualizado.getCorreo());
        usuario.setDireccion(usuarioActualizado.getDireccion());
        
        // 2. Solo cambiamos contraseña si el usuario escribió una nueva
        if (usuarioActualizado.getContrasena() != null && !usuarioActualizado.getContrasena().isEmpty()) {
            usuario.setContrasena(passwordEncoder.encode(usuarioActualizado.getContrasena()));
        }

        usuario.setFechaActualizacion(LocalDateTime.now());
        
        return usuarioRepository.save(usuario); // UPDATE en la BD
    }
    
    @Override
    public void desactivarUsuario(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        usuario.setEstado(false);
        usuarioRepository.save(usuario);
    }
    
    // --- ELIMINAR EN BD (LÓGICO) ---
    @Override
    public void eliminarUsuario(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        
        // Marcamos como eliminado para que desaparezca de la lista
        // pero NO borramos la fila para no perder historial de ventas.
        usuario.setEliminado(true);
        usuario.setEstado(false); 
        
        usuarioRepository.save(usuario); // UPDATE usuario SET eliminado=true...
    }
    
    // --- VALIDACIÓN LOGIN ---
    @Override
    @Transactional(readOnly = true)
    public Boolean validarCredenciales(String nIdentificacion, String contrasena) {
        Optional<Usuario> usuario = usuarioRepository.findByNIdentificacion(nIdentificacion);

        if (usuario.isPresent() && usuario.get().getEstado() && !usuario.get().getEliminado()) {
            return passwordEncoder.matches(contrasena, usuario.get().getContrasena());
        }
        return false;
    }
}