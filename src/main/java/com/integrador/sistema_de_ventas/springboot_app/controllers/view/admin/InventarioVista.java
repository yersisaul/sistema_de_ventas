package com.integrador.sistema_de_ventas.springboot_app.controllers.view.admin;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class InventarioVista {
    
    @GetMapping("/inventario")
    public String inventario(Model model) {
        model.addAttribute("activePage", "inventario");
        return "admin/inventario";
    }
}
