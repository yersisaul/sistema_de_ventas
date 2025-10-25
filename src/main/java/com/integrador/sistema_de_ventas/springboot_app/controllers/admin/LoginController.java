package com.integrador.sistema_de_ventas.springboot_app.controllers.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

// Muestra la vista de login para administradores y recibe las peticiones de login 
@Controller
public class LoginController
{
    @GetMapping("/admin/login")
    public String login() {
        return "admin/login";
    }
  
}
