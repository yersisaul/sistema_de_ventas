package com.integrador.sistema_de_ventas.springboot_app.controllers.view.admin;

import com.integrador.sistema_de_ventas.springboot_app.dto.PedidoCreateDTO;
import com.integrador.sistema_de_ventas.springboot_app.models.Pedido;
import com.integrador.sistema_de_ventas.springboot_app.services.ComprobanteGeneratorService;
import com.integrador.sistema_de_ventas.springboot_app.services.ProductoService;
import com.integrador.sistema_de_ventas.springboot_app.services.UsuarioService;
import com.integrador.sistema_de_ventas.springboot_app.services.PedidoService;

import org.springframework.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Controller
@RequestMapping("/admin/ventas")
public class VentasController {
    
    @Autowired
    private UsuarioService usuarioService;
    
    @Autowired
    private ProductoService productoService;

    @Autowired
    private ComprobanteGeneratorService comprobanteGenerator;

    @Autowired
    private PedidoService pedidoService;
    

    @GetMapping
    public String Venta(Model model) {
        model.addAttribute("activePage", "ventas");
        model.addAttribute("productos", productoService.obtenerProductosParaVentas());
        return "admin/ventas";
    }

    @GetMapping("/nueva")
    public String nuevaVenta(Model model) {
        model.addAttribute("activePage", "nueva");
        model.addAttribute("clientes", usuarioService.obtenerClientesDTO());
        return "admin/nuevaVenta";
    }
    
    @GetMapping("/api/siguiente-numero")
    @ResponseBody
    public ResponseEntity<Map<String, String>> obtenerSiguienteNumero(
            @RequestParam String tipo,
            @RequestParam(defaultValue = "B001") String serie) {
        
        try {
            System.out.println("🔹 Solicitud de número de comprobante: " + tipo);
            
            // Validar tipo
            if (!tipo.equals("BOLETA") && !tipo.equals("FACTURA")) {
                Map<String, String> error = new HashMap<>();
                error.put("error", "Tipo inválido");
                return ResponseEntity.badRequest().body(error);
            }
            
            // Ajustar serie según tipo
            serie = tipo.equals("BOLETA") ? "B001" : "F001";
            
            // ⚠️ IMPORTANTE: Este número es solo una VISTA PREVIA
            // El número REAL se genera al guardar el pedido
            String numero = comprobanteGenerator.generarSiguienteNumero(tipo, serie);
            String numeroCompleto = serie + "-" + numero;
            
            Map<String, String> response = new HashMap<>();
            response.put("tipo", tipo);
            response.put("serie", serie);
            response.put("numero", numero);
            response.put("numeroCompleto", numeroCompleto);
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            System.err.println("❌ Error: " + e.getMessage());
            Map<String, String> error = new HashMap<>();
            error.put("error", "Error al generar número");
            return ResponseEntity.internalServerError().body(error);
        }
    }
    @PostMapping("/api/guardar")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> guardarVenta(@RequestBody PedidoCreateDTO pedidoDTO) {
        try {
            System.out.println("💾 Recibiendo datos de venta...");
            System.out.println("Cliente ID: " + pedidoDTO.getClienteId());
            System.out.println("Tipo comprobante: " + pedidoDTO.getTipoComprobante());
            System.out.println("Total productos: " + pedidoDTO.getDetalles().size());
            
            // Valores por defecto si vienen vacíos
            if (pedidoDTO.getDireccionEnvio() == null || pedidoDTO.getDireccionEnvio().isEmpty()) {
                pedidoDTO.setDireccionEnvio("Recojo en tienda");
            }
            
            if (pedidoDTO.getTelefonoContacto() == null || pedidoDTO.getTelefonoContacto().isEmpty()) {
                pedidoDTO.setTelefonoContacto("000000000");
            }
            
            if (pedidoDTO.getCostoEnvio() == null) {
                pedidoDTO.setCostoEnvio(BigDecimal.ZERO);
            }
            
            // Guardar el pedido usando el servicio existente
            Pedido pedidoGuardado = pedidoService.crearPedido(pedidoDTO);
            
            System.out.println("✅ Venta guardada con ID: " + pedidoGuardado.getId());
            System.out.println("📄 Comprobante: " + pedidoGuardado.getNumeroComprobanteCompleto());
            
            // Preparar respuesta
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("mensaje", "Venta registrada exitosamente");
            response.put("pedidoId", pedidoGuardado.getId());
            response.put("numeroComprobante", pedidoGuardado.getNumeroComprobanteCompleto());
            response.put("total", pedidoGuardado.getTotal());
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            System.err.println("❌ Error al guardar venta: " + e.getMessage());
            e.printStackTrace();
            
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("mensaje", "Error al guardar la venta: " + e.getMessage());
            
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    @GetMapping("/api/pedido/{id}")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> obtenerPedidoParaImprimir(@PathVariable Long id) {
        try {
            System.out.println("🖨️ Obteniendo datos del pedido ID: " + id);
            
            Optional<Pedido> pedidoOpt = pedidoService.obtenerPedidoPorId(id);
            
            if (pedidoOpt.isEmpty()) {
                Map<String, Object> error = new HashMap<>();
                error.put("success", false);
                error.put("mensaje", "Pedido no encontrado");
                return ResponseEntity.notFound().build();
            }
            
            Pedido pedido = pedidoOpt.get();
            
            // Preparar datos para el frontend
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            
            // Datos del pedido
            Map<String, Object> pedidoData = new HashMap<>();
            pedidoData.put("id", pedido.getId());
            pedidoData.put("numeroComprobante", pedido.getNumeroComprobanteCompleto());
            pedidoData.put("tipoComprobante", pedido.getTipoComprobante());
            pedidoData.put("fecha", pedido.getFecha().toString());
            pedidoData.put("subtotal", pedido.getSubtotal());
            pedidoData.put("impuestos", pedido.getImpuestos());
            pedidoData.put("total", pedido.getTotal());
            pedidoData.put("estado", pedido.getEstado());
            
            // Datos del cliente
            Map<String, Object> clienteData = new HashMap<>();
            clienteData.put("nombre", pedido.getCliente().getNombres() + " " + pedido.getCliente().getApellidos());
            clienteData.put("tipoDocumento", pedido.getCliente().getTipoIdentificacion());
            clienteData.put("numeroDocumento", pedido.getCliente().getNIdentificacion());
            clienteData.put("email", pedido.getCliente().getCorreo());
            clienteData.put("telefono", pedido.getTelefonoContacto());
            clienteData.put("direccion", pedido.getDireccionEnvio());
            pedidoData.put("cliente", clienteData);
            
            // Detalles de productos
            var detalles = pedido.getDetalles().stream().map(detalle -> {
                Map<String, Object> detalleMap = new HashMap<>();
                detalleMap.put("productoNombre", detalle.getVarianteProducto().getProducto().getNombre());
                detalleMap.put("talla", detalle.getVarianteProducto().getTalla());
                detalleMap.put("sku", detalle.getVarianteProducto().getProducto().getSku());
                detalleMap.put("cantidad", detalle.getCantidad());
                detalleMap.put("precioUnitario", detalle.getPrecioUnitario());
                detalleMap.put("subtotal", detalle.getSubtotal());
                return detalleMap;
            }).toList();
            
            pedidoData.put("detalles", detalles);
            response.put("pedido", pedidoData);
            
            System.out.println("✅ Datos del pedido obtenidos correctamente");
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            System.err.println("❌ Error al obtener pedido: " + e.getMessage());
            e.printStackTrace();
            
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("mensaje", "Error al obtener los datos del pedido");
            
            return ResponseEntity.internalServerError().body(error);
        }
    }
}