package com.integrador.sistema_de_ventas.springboot_app.dto;

import lombok.Data;
import java.util.List;

@Data
public class VentaRequest {
    private Long clienteId; // Puede ser null si es cliente anónimo/nuevo
    private String tipoComprobante; // BOLETA o FACTURA
    private String metodoPago;
    private Double total;
    
    // Lista de productos que vienen del carrito JS
    private List<DetalleVentaRequest> productos;
    
    // Datos extra si es factura o cliente nuevo manual (opcional)
    private String numeroDocumento;
    private String nombreCliente;
    private String direccion;
}