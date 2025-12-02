package com.integrador.sistema_de_ventas.springboot_app.services;

import com.integrador.sistema_de_ventas.springboot_app.dto.CategoriaResponseDTO;
import com.integrador.sistema_de_ventas.springboot_app.models.Categoria;
import java.util.List;

// Servicio para la gestión de categorías
public interface CategoriaService {
    Categoria crearCategoria(CategoriaResponseDTO categoriaResponseDTO);
    List<Categoria> obtenerTodasLasCategorias();
    Categoria editarCategoria(Long id, CategoriaResponseDTO categoriaResponseDTO);
    void eliminarCategoria(Long id);
}
