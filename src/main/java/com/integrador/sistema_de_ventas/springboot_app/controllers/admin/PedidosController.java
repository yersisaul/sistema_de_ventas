package com.integrador.sistema_de_ventas.springboot_app.controllers.admin;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
@RequestMapping("/admin/pedidos")
public class PedidosController {
    
    @GetMapping
    public String pedidos(Model model){
        model.addAttribute("activePage", "pedidos");
        return "admin/pedidos";
    }
    
}
