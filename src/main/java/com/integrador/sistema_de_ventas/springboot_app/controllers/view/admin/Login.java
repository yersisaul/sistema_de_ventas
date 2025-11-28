package com.integrador.sistema_de_ventas.springboot_app.controllers.view.admin;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class Login {
     @GetMapping("/login")
    public String login() {
        return "admin/login";
    }
}
