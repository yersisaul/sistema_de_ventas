package com.integrador.sistema_de_ventas.springboot_app.controllers.admin;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.integrador.sistema_de_ventas.springboot_app.dto.CategoriaResponseDTO;
import com.integrador.sistema_de_ventas.springboot_app.dto.ProductoResponseDTO;
import com.integrador.sistema_de_ventas.springboot_app.models.Categoria;
import com.integrador.sistema_de_ventas.springboot_app.models.Producto;
import com.integrador.sistema_de_ventas.springboot_app.models.Usuario;
import com.integrador.sistema_de_ventas.springboot_app.services.CategoriaService;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;



@RestController
@RequestMapping("/api/admin/categorias")
@CrossOrigin(origins = "*")
public class CategoriaController {
    @Autowired
    private CategoriaService categoriaService;
    
    @PostMapping
    public ResponseEntity<?> crearCategoria(@RequestBody @Valid CategoriaResponseDTO categoriaResponseDTO) {
        try {
            Categoria nuevaCategoria = categoriaService.crearCategoria(categoriaResponseDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(CategoriaResponseDTO.fromCategoria(nuevaCategoria));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("Error al crear categoria: " + e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<?> obtenerTodasLasCategorias() {
        System.out.println("Llamada a obtenerTodasLasCategorias");
        try {
            List<Categoria> categorias = categoriaService.obtenerTodasLasCategorias();
            List<CategoriaResponseDTO> response = categorias.stream().map(CategoriaResponseDTO::fromCategoria).collect(Collectors.toList());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error: " + e.getMessage());
        }
    }   

}
