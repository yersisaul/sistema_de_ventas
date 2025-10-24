package com.integrador.sistema_de_ventas.springboot_app.controllers.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class LoginController {
    @GetMapping("/admin/login")
    public String login() {
        return "admin/login";
    }
  
}
