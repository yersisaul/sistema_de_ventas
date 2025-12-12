package com.integrador.sistema_de_ventas.springboot_app.controllers.admin;

import com.integrador.sistema_de_ventas.springboot_app.services.VentaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin/ventas")
public class VentaVistaController {

    @Autowired
    private VentaService ventaService;

    @GetMapping("") 
    public String listarVentas(Model model) {
        // CORREGIDO: Usamos "listaPedidos" para que coincida con tu HTML th:each
        model.addAttribute("listaPedidos", ventaService.obtenerTodasLasVentas());
        
        model.addAttribute("activePage", "ventas"); 
        return "admin/ventas"; 
    }

    @GetMapping("/nueva")
    public String nuevaVenta(Model model) {
        model.addAttribute("activePage", "ventas");
        return "admin/nuevaVenta";
    }
}