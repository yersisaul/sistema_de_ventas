package com.integrador.sistema_de_ventas.springboot_app.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import com.integrador.sistema_de_ventas.springboot_app.models.Pedido;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PedidoDTO {
    private Long id;
    private Long clienteId;
    private LocalDateTime fecha;
    private BigDecimal subtotal;
    private BigDecimal impuestos;
    private BigDecimal total;
    private BigDecimal costoEnvio;
    private String estado;
    private String direccionEnvio;
    private String telefonoContacto;
    private List<DetalleCarritoDTO> detalles;

    public PedidoDTO pedidoToDTO(Pedido pedido) {
        this.id = pedido.getId();
        this.clienteId = pedido.getCliente().getId();
        this.fecha = pedido.getFecha();
        this.subtotal = pedido.getSubtotal();
        this.impuestos = pedido.getImpuestos();
        this.total = pedido.getTotal();
        this.costoEnvio = pedido.getCostoEnvio();
        this.estado = pedido.getEstado();
        this.direccionEnvio = pedido.getDireccionEnvio();
        this.telefonoContacto = pedido.getTelefonoContacto();

        List<DetalleCarritoDTO> detalleDTOs = pedido.getDetalles().stream().map(detalle -> {
            DetalleCarritoDTO dto = new DetalleCarritoDTO();
            dto.setVarianteProductoId(detalle.getVarianteProducto().getId());
            dto.setCantidad(detalle.getCantidad());
            return dto;
        }).toList();
        this.detalles = detalleDTOs;

        return this;
    }
}
