package com.integrador.sistema_de_ventas.springboot_app.controllers.view.admin;

import com.integrador.sistema_de_ventas.springboot_app.services.VentaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class PedidosVista {

    @Autowired
    private VentaService ventaService;
    
    @GetMapping("/pedidos")
    public String pedidos(Model model) {
        // 1. Traemos los pedidos de la BD
        model.addAttribute("listaPedidos", ventaService.obtenerTodasLasVentas());
        
        // 2. Marcamos la página activa para el CSS del navbar
        model.addAttribute("activePage", "pedidos");
        
        return "admin/pedidos"; // Renderiza el HTML
    }
}