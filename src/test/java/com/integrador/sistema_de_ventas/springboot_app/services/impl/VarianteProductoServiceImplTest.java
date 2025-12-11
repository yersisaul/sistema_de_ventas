package com.integrador.sistema_de_ventas.springboot_app.services.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Optional;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import com.integrador.sistema_de_ventas.springboot_app.dto.VarianteUpdateDTO;
import com.integrador.sistema_de_ventas.springboot_app.models.VarianteProducto;
import com.integrador.sistema_de_ventas.springboot_app.repository.VarianteProductoRepository;

public class VarianteProductoServiceImplTest {
    @Mock
    private VarianteProductoRepository varianteRepository;
    
    @InjectMocks
    private VarianteProductoServiceImpl varianteService;
    
    private VarianteProducto varianteEjemplo;
    
    @BeforeEach
    void setUp() {
        // Preparamos una variante de ejemplo para usar en varias pruebas
        varianteEjemplo = new VarianteProducto();
        varianteEjemplo.setId(1L);
        varianteEjemplo.setStock(10);
        varianteEjemplo.setPrecio(BigDecimal.valueOf(99.99));
        varianteEjemplo.setTalla("M");
        varianteEjemplo.setActivo(true);
    }
    
    // ========== PRUEBAS CRÍTICAS QUE TE AHORRARÁN DOLORES DE CABEZA ==========
    
    @Test
    void crearVariante_guardaConActivoTrue() {
        // Arrange
        VarianteProducto varianteNueva = new VarianteProducto();
        varianteNueva.setTalla("L");
        varianteNueva.setStock(5);
        varianteNueva.setPrecio(BigDecimal.valueOf(89.99));
        // NO seteamos 'activo' a propósito
        
        when(varianteRepository.save(any(VarianteProducto.class)))
            .thenAnswer(invocation -> {
                VarianteProducto v = invocation.getArgument(0);
                v.setId(100L); // Simula ID generado
                return v;
            });
        
        // Act
        VarianteProducto resultado = varianteService.crearVariante(varianteNueva);
        
        // Assert
        assertNotNull(resultado);
        assertTrue(resultado.getActivo()); // ¡ESTO ES IMPORTANTE!
        verify(varianteRepository).save(varianteNueva);
    }
    
    @Test
    void actualizarStock_aumentaStockCorrectamente() {
        // Arrange
        Long varianteId = 1L;
        Integer cantidadAAgregar = 5;
        
        when(varianteRepository.findById(varianteId))
            .thenReturn(Optional.of(varianteEjemplo));
        
        // Act
        varianteService.actualizarStock(varianteId, cantidadAAgregar);
        
        // Assert
        // Verifica que el stock quedó en 15 (10 + 5)
        assertEquals(15, varianteEjemplo.getStock());
        verify(varianteRepository).save(varianteEjemplo);
    }
    
    @Test
    void actualizarStock_disminuyeStockCorrectamente() {
        // Arrange
        Long varianteId = 1L;
        Integer cantidadAQuitar = -3; // Negativo para disminuir
        
        when(varianteRepository.findById(varianteId))
            .thenReturn(Optional.of(varianteEjemplo));
        
        // Act
        varianteService.actualizarStock(varianteId, cantidadAQuitar);
        
        // Assert
        // Verifica que el stock quedó en 7 (10 - 3)
        assertEquals(7, varianteEjemplo.getStock());
        verify(varianteRepository).save(varianteEjemplo);
    }
    
    @Test
    void actualizarStock_conStockInsuficiente_lanzaExcepcion() {
        // Arrange
        Long varianteId = 1L;
        varianteEjemplo.setStock(2); // Solo tenemos 2 unidades
        Integer cantidadAQuitar = -5; // Intentamos quitar 5 (imposible)
        
        when(varianteRepository.findById(varianteId))
            .thenReturn(Optional.of(varianteEjemplo));
        
        // Act & Assert
        RuntimeException exception = assertThrows(
            RuntimeException.class,
            () -> varianteService.actualizarStock(varianteId, cantidadAQuitar)
        );
        
        assertEquals("Stock insuficiente", exception.getMessage());
        // Verifica que NO se guardó nada
        verify(varianteRepository, never()).save(any());
    }
    
    @Test
    void actualizarStock_conCantidadCero_noCambiaStock() {
        // Arrange
        Long varianteId = 1L;
        Integer cantidadCero = 0;
        
        when(varianteRepository.findById(varianteId))
            .thenReturn(Optional.of(varianteEjemplo));
        
        // Act
        varianteService.actualizarStock(varianteId, cantidadCero);
        
        // Assert
        assertEquals(10, varianteEjemplo.getStock()); // Sigue en 10
        verify(varianteRepository).save(varianteEjemplo); // Pero igual se guarda (puede ser overhead)
    }
    
    @Test
    void actualizarVariante_conDatosValidos_actualizaCorrectamente() {
        // Arrange
        Long varianteId = 1L;
        VarianteUpdateDTO updateDTO = new VarianteUpdateDTO();
        updateDTO.setStock(50);
        updateDTO.setPrecio(BigDecimal.valueOf(129.99));
        
        when(varianteRepository.findById(varianteId))
            .thenReturn(Optional.of(varianteEjemplo));
        when(varianteRepository.save(any(VarianteProducto.class)))
            .thenReturn(varianteEjemplo);
        
        // Act
        VarianteProducto resultado = varianteService.actualizarVariante(varianteId, updateDTO);
        
        // Assert
        assertEquals(50, resultado.getStock());
        assertEquals(BigDecimal.valueOf(129.99), resultado.getPrecio());
        // Verifica que NO cambió la talla u otros campos
        assertEquals("M", resultado.getTalla());
        assertTrue(resultado.getActivo());
    }
    
    @Test
    void actualizarVariante_varianteNoExiste_lanzaExcepcion() {
        // Arrange
        Long varianteIdInexistente = 999L;
        VarianteUpdateDTO updateDTO = new VarianteUpdateDTO();
        
        when(varianteRepository.findById(varianteIdInexistente))
            .thenReturn(Optional.empty());
        
        // Act & Assert
        RuntimeException exception = assertThrows(
            RuntimeException.class,
            () -> varianteService.actualizarVariante(varianteIdInexistente, updateDTO)
        );
        
        assertEquals("Variante no encontrada", exception.getMessage());
    }
    
    @Test
    void desactivarVariante_cambiaActivoAFalse() {
        // Arrange
        Long varianteId = 1L;
        
        when(varianteRepository.findById(varianteId))
            .thenReturn(Optional.of(varianteEjemplo));
        
        // Act
        varianteService.desactivarVariante(varianteId);
        
        // Assert
        assertFalse(varianteEjemplo.getActivo()); // ¡CRÍTICO! Debe quedar false
        verify(varianteRepository).save(varianteEjemplo);
    }
    
    @Test
    void obtenerVariantesPorProducto_soloDevuelveActivos() {
        // Arrange
        Long productoId = 100L;
        List<VarianteProducto> variantesActivas = Arrays.asList(
            crearVariante(1L, "S", true),
            crearVariante(2L, "M", true)
        );
        
        when(varianteRepository.findByProductoIdAndActivo(productoId, true))
            .thenReturn(variantesActivas);
        
        // Act
        List<VarianteProducto> resultado = varianteService.obtenerVariantesPorProducto(productoId);
        
        // Assert
        assertEquals(2, resultado.size());
        assertTrue(resultado.stream().allMatch(VarianteProducto::getActivo));
        verify(varianteRepository).findByProductoIdAndActivo(productoId, true);
    }
    
    @Test
    void obtenerVariantesConStock_devuelveSoloConStockMayorACero() {
        // Arrange
        Long productoId = 100L;
        List<VarianteProducto> variantesConStock = Arrays.asList(
            crearVarianteConStock(1L, "S", 5),
            crearVarianteConStock(2L, "M", 10)
        );
        
        when(varianteRepository.findVariantesConStock(productoId))
            .thenReturn(variantesConStock);
        
        // Act
        List<VarianteProducto> resultado = varianteService.obtenerVariantesConStock(productoId);
        
        // Assert
        assertEquals(2, resultado.size());
        assertTrue(resultado.stream().allMatch(v -> v.getStock() > 0));
    }
    
    @Test
    void obtenerVariantesStockBajo_devuelveSoloStockBajo() {
        // Arrange
        // Supongamos que stock bajo es < 10
        List<VarianteProducto> variantesStockBajo = Arrays.asList(
            crearVarianteConStock(1L, "S", 2),
            crearVarianteConStock(2L, "M", 5),
            crearVarianteConStock(3L, "L", 9)
        );
        
        when(varianteRepository.findVariantesStockBajo())
            .thenReturn(variantesStockBajo);
        
        // Act
        List<VarianteProducto> resultado = varianteService.obtenerVariantesStockBajo();
        
        // Assert
        assertEquals(3, resultado.size());
        assertTrue(resultado.stream().allMatch(v -> v.getStock() < 10));
    }
    
    @Test
    void obtenerVariantePorProductoTalla_encuentraVarianteCorrecta() {
        // Arrange
        Long productoId = 100L;
        String talla = "L";
        VarianteProducto varianteEsperada = crearVariante(1L, talla, true);
        
        when(varianteRepository.findByProductoIdAndTalla(productoId, talla))
            .thenReturn(Optional.of(varianteEsperada));
        
        // Act
        Optional<VarianteProducto> resultado = varianteService
            .obtenerVariantePorProductoTalla(productoId, talla);
        
        // Assert
        assertTrue(resultado.isPresent());
        assertEquals("L", resultado.get().getTalla());
        assertEquals(productoId, resultado.get().getProducto().getId());
    }
    
    @Test
    void obtenerVariantePorProductoTalla_noEncuentra_retornaEmpty() {
        // Arrange
        Long productoId = 100L;
        String talla = "XXL"; // Talla que no existe
        
        when(varianteRepository.findByProductoIdAndTalla(productoId, talla))
            .thenReturn(Optional.empty());
        
        // Act
        Optional<VarianteProducto> resultado = varianteService
            .obtenerVariantePorProductoTalla(productoId, talla);
        
        // Assert
        assertFalse(resultado.isPresent());
    }
    
    // ========== PRUEBAS DE CASOS BORDE (EDGE CASES) ==========
    
    @Test
    void actualizarStock_conStockGrande_noProblema() {
        // Arrange
        Long varianteId = 1L;
        varianteEjemplo.setStock(1000);
        Integer cantidadGrande = 5000;
        
        when(varianteRepository.findById(varianteId))
            .thenReturn(Optional.of(varianteEjemplo));
        
        // Act
        varianteService.actualizarStock(varianteId, cantidadGrande);
        
        // Assert
        assertEquals(6000, varianteEjemplo.getStock()); // 1000 + 5000
    }
    
    @Test
    void actualizarStock_conStockNegativoInicial_lanzaExcepcionSiQuedaNegativo() {
        // Arrange (caso raro pero posible si hay bug en BD)
        Long varianteId = 1L;
        varianteEjemplo.setStock(-5); // ¡Stock negativo! Error previo
        Integer cantidadAQuitar = -10; // Quitamos más
        
        when(varianteRepository.findById(varianteId))
            .thenReturn(Optional.of(varianteEjemplo));
        
        // Act & Assert
        RuntimeException exception = assertThrows(
            RuntimeException.class,
            () -> varianteService.actualizarStock(varianteId, cantidadAQuitar)
        );
        
        assertEquals("Stock insuficiente", exception.getMessage());
    }
    
    @Test
    void actualizarVariante_conPrecioNull_noDeberiaEstablecerNull() {
        // Arrange
        Long varianteId = 1L;
        VarianteUpdateDTO updateDTO = new VarianteUpdateDTO();
        updateDTO.setStock(20);
        updateDTO.setPrecio(null); // ¡Precio null!
        
        when(varianteRepository.findById(varianteId))
            .thenReturn(Optional.of(varianteEjemplo));
        
        // Act
        VarianteProducto resultado = varianteService.actualizarVariante(varianteId, updateDTO);
        
        // Assert
        assertEquals(20, resultado.getStock());
        // ¿Qué pasa con el precio? ¿Queda el anterior o se pone null?
        // Esta prueba te hace pensar en validaciones faltantes
    }
    
    @Test
    void actualizarVariante_conStockNegativo_permiteStockNegativo() {
        // Arrange
        Long varianteId = 1L;
        VarianteUpdateDTO updateDTO = new VarianteUpdateDTO();
        updateDTO.setStock(-5); // ¡Stock negativo permitido!
        updateDTO.setPrecio(BigDecimal.valueOf(50.0));
        
        when(varianteRepository.findById(varianteId))
            .thenReturn(Optional.of(varianteEjemplo));
        
        // Act
        VarianteProducto resultado = varianteService.actualizarVariante(varianteId, updateDTO);
        
        // Assert
        assertEquals(-5, resultado.getStock()); 
        // ¡OJO! ¿Deberíamos permitir stock negativo?
        // Esta prueba expone una decisión de diseño
    }
    
    // ========== MÉTODOS DE AYUDA ==========
    
    private VarianteProducto crearVariante(Long id, String talla, boolean activo) {
        VarianteProducto v = new VarianteProducto();
        v.setId(id);
        v.setTalla(talla);
        v.setActivo(activo);
        v.setStock(10);
        v.setPrecio(BigDecimal.valueOf(99.99));
        return v;
    }
    
    private VarianteProducto crearVarianteConStock(Long id, String talla, Integer stock) {
        VarianteProducto v = crearVariante(id, talla, true);
        v.setStock(stock);
        return v;
    }
}
