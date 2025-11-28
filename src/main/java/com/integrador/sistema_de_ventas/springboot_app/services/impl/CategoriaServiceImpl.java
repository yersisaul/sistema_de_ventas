package com.integrador.sistema_de_ventas.springboot_app.services.impl;

import com.integrador.sistema_de_ventas.springboot_app.dto.CategoriaResponseDTO;
import com.integrador.sistema_de_ventas.springboot_app.models.Categoria;
import com.integrador.sistema_de_ventas.springboot_app.repository.CategoriaRepository;
import com.integrador.sistema_de_ventas.springboot_app.services.CategoriaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class CategoriaServiceImpl implements CategoriaService {
    @Autowired
    private CategoriaRepository categoriaRepository;
    
    @Override
    public Categoria crearCategoria(CategoriaResponseDTO categoriaDto) {
        Categoria nuevaCategoria = new Categoria();
        nuevaCategoria.setNombre(categoriaDto.getNombre());
        nuevaCategoria.setDescripcion(categoriaDto.getDescripcion());
        return categoriaRepository.save(nuevaCategoria);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Categoria> obtenerTodasLasCategorias() {
        System.out.println("Llamada a obtenerTodasLasCategorias");
        return categoriaRepository.findAll();
    }

}
