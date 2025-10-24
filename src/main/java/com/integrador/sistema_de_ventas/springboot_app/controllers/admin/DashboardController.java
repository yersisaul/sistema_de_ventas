package com.integrador.sistema_de_ventas.springboot_app.controllers.admin;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin/dashboard")
public class DashboardController {
    @GetMapping
    public String dashboard(Model model) {
        model.addAttribute("activePage", "dashboard");
        return "admin/dashboard";
    }
}
