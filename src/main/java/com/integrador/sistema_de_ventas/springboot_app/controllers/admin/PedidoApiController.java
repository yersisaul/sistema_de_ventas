package com.integrador.sistema_de_ventas.springboot_app.controllers.admin;

import com.integrador.sistema_de_ventas.springboot_app.models.DetallePedido;
import com.integrador.sistema_de_ventas.springboot_app.repository.DetallePedidoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/pedidos")
public class PedidoApiController {

    @Autowired
    private DetallePedidoRepository detalleRepository;

    @GetMapping("/{id}/detalles")
    public ResponseEntity<List<Map<String, Object>>> obtenerDetalles(@PathVariable Long id) {
        // Buscar detalles por ID de pedido
        List<DetallePedido> detalles = detalleRepository.findAll().stream()
                .filter(d -> d.getPedido().getId().equals(id)) // Filtro simple (mejor usar Query en Repo)
                .collect(Collectors.toList());

        // Transformar a JSON simple para el JS
        List<Map<String, Object>> respuesta = detalles.stream().map(d -> {
            Map<String, Object> map = new HashMap<>();
            map.put("sku", d.getVarianteProducto().getProducto().getSku());
            map.put("nombre", d.getVarianteProducto().getProducto().getNombre());
            map.put("cantidad", d.getCantidad());
            map.put("precio", d.getPrecioUnitario());
            map.put("subtotal", d.getSubtotal());
            return map;
        }).collect(Collectors.toList());

        return ResponseEntity.ok(respuesta);
    }
}