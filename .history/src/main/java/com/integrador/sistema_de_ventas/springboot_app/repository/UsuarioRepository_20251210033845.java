package com.integrador.sistema_de_ventas.springboot_app.repository;

import com.integrador.sistema_de_ventas.springboot_app.models.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByCorreo(String correo);
    Optional<Usuario> findBynIdentificacion(String nIdentificacion);

    List<Usuario> findByRol(String rol);
    List<Usuario> findByEstadoAndEliminado(Boolean estado, Boolean eliminado);
    
    @Query("SELECT u FROM Usuario u WHERE u.rol = :rol AND u.estado = true AND u.eliminado = false")
    List<Usuario> findActiveByRol(@Param("rol") String rol);
}
