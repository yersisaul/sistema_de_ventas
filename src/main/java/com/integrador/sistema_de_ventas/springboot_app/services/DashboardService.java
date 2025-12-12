package com.integrador.sistema_de_ventas.springboot_app.services;

import com.integrador.sistema_de_ventas.springboot_app.models.Pedido;
import com.integrador.sistema_de_ventas.springboot_app.repository.PedidoRepository;
import com.integrador.sistema_de_ventas.springboot_app.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class DashboardService {

    @Autowired private PedidoRepository pedidoRepository;
    @Autowired private UsuarioRepository usuarioRepository;

    public BigDecimal obtenerVentasDelDia() {
        // Asegúrate de tener esta consulta en PedidoRepository
        BigDecimal total = pedidoRepository.sumarVentasDia(); 
        return total != null ? total : BigDecimal.ZERO;
    }

    public Long obtenerClientesNuevos() {
        // Asegúrate de tener esta consulta en UsuarioRepository
        return usuarioRepository.contarNuevosClientesMes();
    }

    public List<Pedido> obtenerPedidosRecientes() {
        return pedidoRepository.findTop5ByOrderByFechaDesc();
    }

    public List<BigDecimal> obtenerDatosGrafico() {
        List<BigDecimal> ventas = new ArrayList<>();
        for (int i = 1; i <= 12; i++) {
            BigDecimal mes = pedidoRepository.obtenerVentasPorMes(i);
            ventas.add(mes != null ? mes : BigDecimal.ZERO);
        }
        return ventas;
    }
}