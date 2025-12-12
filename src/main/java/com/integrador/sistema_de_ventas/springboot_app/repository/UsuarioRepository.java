package com.integrador.sistema_de_ventas.springboot_app.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.integrador.sistema_de_ventas.springboot_app.models.Usuario;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    
    // Spring puede generar esto automáticamente sin problemas
    Optional<Usuario> findByCorreo(String correo);
    
    // --- SOLUCIÓN DEL ERROR ---
    // Usamos @Query para decirle a Spring explícitamente: 
    // "No adivines, busca la propiedad llamada 'nIdentificacion' en la clase Usuario"
    @Query("SELECT u FROM Usuario u WHERE u.nIdentificacion = :nIdentificacion")
    Optional<Usuario> findByNIdentificacion(@Param("nIdentificacion") String nIdentificacion);
    // --------------------------

    // Búsqueda por rol (usando el Enum)
    List<Usuario> findByRol(Usuario.Rol rol);
    
    // Búsqueda por estado
    List<Usuario> findByEstadoAndEliminado(Boolean estado, Boolean eliminado);
    
    // Búsqueda personalizada de activos por rol
    @Query("SELECT u FROM Usuario u WHERE u.rol = :rol AND u.estado = true AND u.eliminado = false")
    List<Usuario> findActiveByRol(@Param("rol") Usuario.Rol rol);

    // Búsqueda por Nombre, Apellido o DNI (Solo clientes)
@Query("SELECT u FROM Usuario u WHERE u.rol = 'CLIENTE' AND " +
       "(LOWER(u.nombres) LIKE LOWER(CONCAT('%', :term, '%')) OR " +
       "LOWER(u.apellidos) LIKE LOWER(CONCAT('%', :term, '%')) OR " +
       "u.nIdentificacion LIKE CONCAT('%', :term, '%'))")
List<Usuario> buscarClientes(@Param("term") String term);

// Contar clientes nuevos este mes
@Query("SELECT COUNT(u) FROM Usuario u WHERE u.rol = 'CLIENTE' AND MONTH(u.fechaRegistro) = MONTH(CURRENT_DATE) AND YEAR(u.fechaRegistro) = YEAR(CURRENT_DATE)")
Long contarNuevosClientesMes();
}