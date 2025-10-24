package com.integrador.sistema_de_ventas.springboot_app.controllers.admin;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
@RequestMapping("/admin/inventario")
public class InventarioController {
    @GetMapping
    public String inventario(Model model) {
        model.addAttribute("activePage", "inventario");
        return "admin/inventario";
    }
}
