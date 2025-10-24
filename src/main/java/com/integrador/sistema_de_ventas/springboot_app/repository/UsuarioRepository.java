package com.integrador.sistema_de_ventas.springboot_app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.integrador.sistema_de_ventas.springboot_app.models.Usuario;
import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByCorreo(String correo);
}


