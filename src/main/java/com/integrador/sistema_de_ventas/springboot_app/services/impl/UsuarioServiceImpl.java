package com.integrador.sistema_de_ventas.springboot_app.services.impl;

import com.integrador.sistema_de_ventas.springboot_app.models.Usuario;
import com.integrador.sistema_de_ventas.springboot_app.repository.UsuarioRepository;
import com.integrador.sistema_de_ventas.springboot_app.services.UsuarioService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;

@Service
@Transactional
public class UsuarioServiceImpl implements UsuarioService // Implementa la interfaz UsuarioService
{
    
    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    public UserDetails loadUserByUsername(String correo) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepository.findByCorreo(correo)
            .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado con correo: " + correo));
        
        // Verificar si el usuario está activo
        if (!usuario.getEstado()) {
            throw new UsernameNotFoundException("Usuario desactivado: " + correo);
        }
        
        return new User(
            usuario.getCorreo(), 
            usuario.getContrasena(),
            Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + usuario.getRol()))
        );
    }
}