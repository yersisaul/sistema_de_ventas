package com.integrador.sistema_de_ventas.springboot_app.controllers.admin;

import com.integrador.sistema_de_ventas.springboot_app.dto.VentaRequest;
import com.integrador.sistema_de_ventas.springboot_app.models.Usuario;
import com.integrador.sistema_de_ventas.springboot_app.models.VarianteProducto;
import com.integrador.sistema_de_ventas.springboot_app.repository.UsuarioRepository;
import com.integrador.sistema_de_ventas.springboot_app.repository.VarianteProductoRepository;
import com.integrador.sistema_de_ventas.springboot_app.services.VentaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/ventas")
@CrossOrigin(origins = "*") // Permite peticiones desde el JS del navegador
public class VentaApiController {

    @Autowired
    private VentaService ventaService;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private VarianteProductoRepository varianteRepository;

    // --- 1. BUSCAR CLIENTES (Para el Modal) ---
    @GetMapping("/buscar-clientes")
    public ResponseEntity<List<Usuario>> buscarClientes(@RequestParam(defaultValue = "") String term) {
        // Si el término está vacío, devuelve los primeros 10 clientes, sino busca
        List<Usuario> clientes;
        if (term.isEmpty()) {
            clientes = usuarioRepository.findByRol(Usuario.Rol.CLIENTE).stream().limit(10).collect(Collectors.toList());
        } else {
            clientes = usuarioRepository.buscarClientes(term);
        }
        return ResponseEntity.ok(clientes);
    }

    // --- 2. BUSCAR PRODUCTOS (Para el Modal) ---
    @GetMapping("/buscar-productos")
    public ResponseEntity<List<Map<String, Object>>> buscarProductos(@RequestParam(defaultValue = "") String term) {
        // Buscamos las variantes (Tallas/Colores) que coincidan
        List<VarianteProducto> variantes = varianteRepository.buscarProductos(term);

        // Transformamos la respuesta a un JSON simple que entienda tu JS
        List<Map<String, Object>> respuesta = variantes.stream().map(v -> {
            Map<String, Object> map = new HashMap<>();
            map.put("id", v.getId()); // ID de la variante (importante para restar stock)
            map.put("nombre", v.getProducto().getNombre() + " - " + v.getTalla());
            map.put("sku", v.getProducto().getSku() != null ? v.getProducto().getSku() : "SKU-" + v.getId());
            map.put("precio", v.getPrecio());
            map.put("stock", v.getStock());
            map.put("categoria", v.getProducto().getCategoria() != null ? v.getProducto().getCategoria().getNombre() : "General");
            return map;
        }).collect(Collectors.toList());

        return ResponseEntity.ok(respuesta);
    }

    // --- 3. CREAR VENTA (Tu método solicitado) ---
    @PostMapping("/crear")
    public ResponseEntity<?> crearVenta(@RequestBody VentaRequest request) {
        try {
            // Validaciones básicas antes de llamar al servicio
            if (request.getProductos() == null || request.getProductos().isEmpty()) {
                return ResponseEntity.badRequest().body("Error: El carrito de compras está vacío.");
            }

            // Llamamos al servicio transaccional que creamos antes
            ventaService.registrarVenta(request);
            
            return ResponseEntity.ok("¡Venta registrada con éxito! El stock ha sido actualizado.");
            
        } catch (Exception e) {
            e.printStackTrace(); // Ver error en consola del servidor
            return ResponseEntity.badRequest().body("Error al procesar la venta: " + e.getMessage());
        }
    }
}