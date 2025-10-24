package com.integrador.sistema_de_ventas.springboot_app.controllers.client;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
    @GetMapping("/")
    public String index() {
        return "client/index";
    }
}
