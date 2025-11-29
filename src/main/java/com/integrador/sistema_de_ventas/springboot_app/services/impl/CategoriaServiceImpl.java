package com.integrador.sistema_de_ventas.springboot_app.services.impl;

import com.integrador.sistema_de_ventas.springboot_app.dto.CategoriaResponseDTO;
import com.integrador.sistema_de_ventas.springboot_app.models.Categoria;
import com.integrador.sistema_de_ventas.springboot_app.repository.CategoriaRepository;
import com.integrador.sistema_de_ventas.springboot_app.repository.ProductoRepository;
import com.integrador.sistema_de_ventas.springboot_app.services.CategoriaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@Transactional
public class CategoriaServiceImpl implements CategoriaService {
    @Autowired
    private CategoriaRepository categoriaRepository;

    @Autowired
    private ProductoRepository productoRepository;

    @Override
    public Categoria crearCategoria(CategoriaResponseDTO categoriaDto) {
        Categoria nuevaCategoria = new Categoria();
        nuevaCategoria.setNombre(categoriaDto.getNombre());
        return categoriaRepository.save(nuevaCategoria);
    }

    @Override
    public Categoria editarCategoria(Long id, CategoriaResponseDTO dto) {
        Categoria categoria = categoriaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));

        categoria.setNombre(dto.getNombre());

        return categoriaRepository.save(categoria);
    }

    // ELIMINAR CATEGORIA
    @Override
    public void eliminarCategoria(Long id) {
        long asociados = productoRepository.countByCategoriaId(id);

        if (asociados > 0) {
            throw new RuntimeException("No se puede eliminar la categoría porque tiene productos asociados.");
        }

        categoriaRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Categoria> obtenerTodasLasCategorias() {
        System.out.println("Llamada a obtenerTodasLasCategorias");
        return categoriaRepository.findAll();
    }

}
