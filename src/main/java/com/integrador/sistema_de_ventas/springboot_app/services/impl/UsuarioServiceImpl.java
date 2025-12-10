package com.integrador.sistema_de_ventas.springboot_app.services.impl;
import com.integrador.sistema_de_ventas.springboot_app.exception.BadRequestException;
import com.integrador.sistema_de_ventas.springboot_app.exception.ResourceNotFoundException;
import com.integrador.sistema_de_ventas.springboot_app.dto.UsuarioCreateDTO;
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

    private UsuarioResponseDTO mapToDTO(Usuario usuario) {
        if (usuario == null) {
            return null;
        }
        UsuarioResponseDTO dto = new UsuarioResponseDTO();
        dto.setIdUsuario(usuario.getId());
        dto.setTipoDocumento(usuario.getTipoIdentificacion());
        dto.setNumeroDocumento(usuario.getNIdentificacion());
        dto.setNombre(usuario.getNombres());
        
     
        String apellidosCompletos = usuario.getApellidos() != null ? usuario.getApellidos().trim() : "";
        String[] partes = apellidosCompletos.split("\\s+"); // Separar por uno o más espacios
        dto.setApellidoPaterno(partes.length >= 1 ? partes[0] : ""); // Primer apellido
        dto.setApellidoMaterno(partes.length >= 2 ? partes[partes.length - 1] : ""); // Último apellido
        
        dto.setTelefono(usuario.getTelefono());
        dto.setCorreo(usuario.getCorreo());
        dto.setDireccion(usuario.getDireccion());
     
        return dto;
    }

    @Override
    @Transactional(readOnly = true)
    public List<UsuarioResponseDTO> obtenerUsuariosPorRolDTO(String rol) {
        //  el método del repositorio que filtra por rol, estado activo y no eliminado
        return usuarioRepository.findActiveByRol(rol) 
                .stream()
                .map(this::mapToDTO) // Mapeamos cada Usuario a un UsuarioResponseDTO
                .collect(Collectors.toList());
    }

    @Override
    // También es buena práctica devolver el DTO en el GET individual desde el servicio
    @Transactional(readOnly = true)
    public UsuarioResponseDTO obtenerClienteDTO(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente no encontrado con ID: " + id));
        
        if (!"CLIENTE".equals(usuario.getRol())) {
            throw new ResourceNotFoundException("El ID no corresponde a un cliente");
        }
        
        return mapToDTO(usuario);
    }

    @Override
    public UsuarioResponseDTO crearCliente(UsuarioCreateDTO dto) {

        Usuario usuario = new Usuario();

        usuario.setTipoIdentificacion(dto.getTipoDocumento()); 
        usuario.setNIdentificacion(dto.getNumeroDocumento());
        usuario.setNombres(dto.getNombre()); // OK
         String apellidosCompletos = dto.getApellidoPaterno() + " " + (dto.getApellidoMaterno() != null ? dto.getApellidoMaterno() : "");
        usuario.setApellidos(apellidosCompletos.trim()); // un solo campo
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

        //Usuario guardado = usuarioRepository.save(usuario);
        Usuario guardado = crearUsuario(usuario); 

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
                .orElseThrow(() -> new ResourceNotFoundException("Cliente no encontrado"));

        usuario.setTipoIdentificacion(dto.getTipoDocumento());
        usuario.setNIdentificacion(dto.getNumeroDocumento());
        usuario.setNombres(dto.getNombre());
         String apellidosCompletos = dto.getApellidoPaterno() + " " + (dto.getApellidoMaterno() != null ? dto.getApellidoMaterno() : "");
         usuario.setApellidos(apellidosCompletos.trim());
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
