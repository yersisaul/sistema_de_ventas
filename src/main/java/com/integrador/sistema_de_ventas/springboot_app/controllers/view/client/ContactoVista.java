package com.integrador.sistema_de_ventas.springboot_app.controllers.view.client;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/client")
public class ContactoVista {
    
    @GetMapping("/contactanos")
    public String contactanos() {
        return "client/Contactanos";
    }
}
