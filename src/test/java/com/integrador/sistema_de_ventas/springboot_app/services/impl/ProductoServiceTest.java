package com.integrador.sistema_de_ventas.springboot_app.services.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

import com.integrador.sistema_de_ventas.springboot_app.dto.ProductoCreateDTO;
import com.integrador.sistema_de_ventas.springboot_app.dto.ProductoUpdateDTO;
import com.integrador.sistema_de_ventas.springboot_app.models.Categoria;
import com.integrador.sistema_de_ventas.springboot_app.models.Producto;
import com.integrador.sistema_de_ventas.springboot_app.repository.CategoriaRepository;
import com.integrador.sistema_de_ventas.springboot_app.repository.ProductoRepository;
import com.integrador.sistema_de_ventas.springboot_app.services.GuardadoImgService;
import com.integrador.sistema_de_ventas.springboot_app.services.VarianteProductoService;

@ExtendWith(MockitoExtension.class)
public class ProductoServiceTest {
   @Mock
    private ProductoRepository productoRepository;
    
    @Mock
    private CategoriaRepository categoriaRepository;
    
    @Mock
    private GuardadoImgService guardadoImgService;
    
    @Mock
    private VarianteProductoService varianteService;
    
    @InjectMocks
    private ProductoServiceImpl productoService; // Tu servicio real
    
    @Test
    void crearProducto_conCategoriaValida_guardaProductoConCategoria() {
        // 1. PREPARACIÓN (Arrange)
        ProductoCreateDTO dto = new ProductoCreateDTO();
        dto.setNombre("Camiseta Premium");
        dto.setDescripcion("Descripción test");
        dto.setCategoriaId(1L);
        
        Categoria categoriaMock = new Categoria();
        categoriaMock.setId(1L);
        
        when(categoriaRepository.findById(1L))
            .thenReturn(Optional.of(categoriaMock));
        
        when(productoRepository.save(any(Producto.class)))
            .thenAnswer(invocation -> {
                Producto p = invocation.getArgument(0);
                p.setId(100L); // Simula ID generado por BD
                return p;
            });
        
        // 2. EJECUCIÓN (Act)
        Producto resultado = productoService.crearProducto(dto);
        
        // 3. VERIFICACIÓN (Assert)
        assertNotNull(resultado);
        assertEquals("Camiseta Premium", resultado.getNombre());
        assertEquals(categoriaMock, resultado.getCategoria());
        assertTrue(resultado.getActivo());
        assertFalse(resultado.getEliminado());
        assertEquals("PRD-000100", resultado.getSku()); // ¡Esto es importante!
        
        verify(productoRepository, times(2)).save(any(Producto.class));
        verify(varianteService, times(4)).crearVariante(any());
    }
    
    @Test
    void crearProducto_conCategoriaInvalida_lanzaExcepcion() {
        // Arrange
        ProductoCreateDTO dto = new ProductoCreateDTO();
        dto.setCategoriaId(999L); // ID que no existe
        
        when(categoriaRepository.findById(999L))
            .thenReturn(Optional.empty());
        
        // Act & Assert
        RuntimeException exception = assertThrows(
            RuntimeException.class,
            () -> productoService.crearProducto(dto)
        );
        
        assertEquals("Categoría no encontrada con ID: 999", exception.getMessage());
    }
    
    @Test
    void actualizarProducto_conNuevaImagen_actualizaRuta() {
        // Arrange
        Long productoId = 1L;
        ProductoUpdateDTO updateDTO = new ProductoUpdateDTO();
        updateDTO.setNombre("Nombre Actualizado");
        
        MultipartFile imagenMock = mock(MultipartFile.class);
        when(imagenMock.isEmpty()).thenReturn(false);
        updateDTO.setImagen(imagenMock);
        
        Producto productoExistente = new Producto();
        productoExistente.setId(productoId);
        productoExistente.setImagen("ruta/vieja.jpg");
        
        when(productoRepository.findById(productoId))
            .thenReturn(Optional.of(productoExistente));
        when(guardadoImgService.guardarImagen(imagenMock))
            .thenReturn("ruta/nueva.jpg");
        
        // Act
        Producto resultado = productoService.actualizarProducto(productoId, updateDTO);
        
        // Assert
        assertEquals("ruta/nueva.jpg", resultado.getImagen());
        assertEquals("Nombre Actualizado", resultado.getNombre());
        assertNotNull(resultado.getFechaActualizacion());
    }
}
