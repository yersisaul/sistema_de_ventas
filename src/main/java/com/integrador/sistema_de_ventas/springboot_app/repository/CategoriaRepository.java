package com.integrador.sistema_de_ventas.springboot_app.repository;

import com.integrador.sistema_de_ventas.springboot_app.models.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface CategoriaRepository extends JpaRepository<Categoria, Long> {
    List<Categoria> findByActivaAndEliminada(Boolean activa, Boolean eliminada);
    
    
}