package com.integrador.sistema_de_ventas.springboot_app.services.impl;

import com.integrador.sistema_de_ventas.springboot_app.dto.UsuarioCreateDTO;
import com.integrador.sistema_de_ventas.springboot_app.models.Usuario;
import com.integrador.sistema_de_ventas.springboot_app.repository.UsuarioRepository;
import com.integrador.sistema_de_ventas.springboot_app.services.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import com.integrador.sistema_de_ventas.springboot_app.dto.UsuarioResponseDTO;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

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
            throw new RuntimeException("El correo ya está registrado");
        }
        if (usuarioRepository.findBynIdentificacion(usuario.getNIdentificacion()).isPresent()) {
            throw new RuntimeException("La identificación ya está registrada");
        }
        usuario.setContrasena(passwordEncoder.encode(usuario.getContrasena()));
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
    public UsuarioResponseDTO crearCliente(UsuarioCreateDTO dto) {

        Usuario usuario = new Usuario();

        usuario.setTipoIdentificacion(dto.getTipoDocumento()); // OK
        usuario.setNIdentificacion(dto.getNumeroDocumento()); // OK
        usuario.setNombres(dto.getNombre()); // OK
        usuario.setApellidos(dto.getApellidoPaterno() + " " + dto.getApellidoMaterno()); // un solo campo
        usuario.setTelefono(dto.getTelefono());
        usuario.setCorreo(dto.getCorreo());
        usuario.setDireccion(dto.getDireccion());
        usuario.setContrasena(dto.getContraseña()); // TEXTO PLANO

        usuario.setRol("CLIENTE"); // por defecto
        usuario.setEstado(true);
        usuario.setPuntosFidelizacion(0);
        usuario.setEliminado(false);
        usuario.setFechaRegistro(LocalDateTime.now());
        usuario.setFechaActualizacion(LocalDateTime.now());

        Usuario guardado = usuarioRepository.save(usuario);

        UsuarioResponseDTO response = new UsuarioResponseDTO();
        response.setIdUsuario(guardado.getId());
        response.setTipoDocumento(guardado.getTipoIdentificacion());
        response.setNumeroDocumento(guardado.getNIdentificacion());
        response.setNombre(guardado.getNombres());
        response.setApellidoPaterno(guardado.getApellidos()); // va completo
        response.setTelefono(guardado.getTelefono());
        response.setCorreo(guardado.getCorreo());
        response.setDireccion(guardado.getDireccion());

        return response;
    }

    @Override
    public UsuarioResponseDTO actualizarCliente(Long id, UsuarioCreateDTO dto) {

        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));

        usuario.setTipoIdentificacion(dto.getTipoDocumento());
        usuario.setNIdentificacion(dto.getNumeroDocumento());
        usuario.setNombres(dto.getNombre());
        usuario.setApellidos(dto.getApellidoPaterno() + " " + dto.getApellidoMaterno());
        usuario.setTelefono(dto.getTelefono());
        usuario.setCorreo(dto.getCorreo());
        usuario.setDireccion(dto.getDireccion());
        usuario.setFechaActualizacion(LocalDateTime.now());

        Usuario actualizado = usuarioRepository.save(usuario);

        UsuarioResponseDTO response = new UsuarioResponseDTO();
        response.setIdUsuario(actualizado.getId());
        response.setTipoDocumento(actualizado.getTipoIdentificacion());
        response.setNumeroDocumento(actualizado.getNIdentificacion());
        response.setNombre(actualizado.getNombres());
        response.setApellidoPaterno(actualizado.getApellidos());
        response.setTelefono(actualizado.getTelefono());
        response.setCorreo(actualizado.getCorreo());
        response.setDireccion(actualizado.getDireccion());

        return response;
    }

}
