package com.integrador.sistema_de_ventas.springboot_app.controllers.client;

import com.integrador.sistema_de_ventas.springboot_app.models.Producto;
import com.integrador.sistema_de_ventas.springboot_app.services.CategoriaService;
import com.integrador.sistema_de_ventas.springboot_app.services.ProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/client/home")
@CrossOrigin(origins = "*")
public class HomeController {
    
    @Autowired
    private CategoriaService categoriaService;
    
    @Autowired
    private ProductoService productoService;
    
    @GetMapping("/productos-destacados")
    public ResponseEntity<?> obtenerProductosDestacados() {
        try {
            List<Producto> productos = productoService.obtenerProductosActivos();
            return ResponseEntity.ok(productos);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error: " + e.getMessage());
        }
    }
}
