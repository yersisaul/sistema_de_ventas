package com.integrador.sistema_de_ventas.springboot_app.config;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.integrador.sistema_de_ventas.springboot_app.models.Usuario;
import com.integrador.sistema_de_ventas.springboot_app.repository.UsuarioRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;

    public CustomUserDetailsService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String nIdentificacion) throws UsernameNotFoundException {
        // CORREGIDO: findByNIdentificacion (La N debe ser mayúscula como en el Repository)
        Usuario usuario = usuarioRepository.findByNIdentificacion(nIdentificacion)
            .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado con identificación: " + nIdentificacion));

        Collection<GrantedAuthority> authorities = new ArrayList<>();
        
        if (usuario.getRol() != null) {
            // CORREGIDO: Usamos .name() para obtener el String del Enum ("ADMINISTRADOR", "CLIENTE", etc.)
            // Spring Security espera roles con el prefijo "ROLE_"
            authorities.add(new SimpleGrantedAuthority("ROLE_" + usuario.getRol().name()));
        } else {
            authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
        }

        return User.builder()
            .username(usuario.getNIdentificacion())
            .password(usuario.getContrasena())
            .authorities(authorities)
            .accountExpired(false)
            .accountLocked(false)
            .credentialsExpired(false)
            .disabled(!usuario.getEstado()) // Si estado es true, disabled es false
            .build();
    }
}