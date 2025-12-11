package com.integrador.sistema_de_ventas.springboot_app.controllers.view.admin;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin/ventas")
public class VentasController {
    @GetMapping
    public String Venta(Model model) {
        model.addAttribute("activePage", "ventas");
        return "admin/ventas";
    }

    @GetMapping("/nueva")
    public String nuevaVenta(Model model) {
        model.addAttribute("activePage", "nueva");
        return "admin/nuevaVenta";
    }
}
