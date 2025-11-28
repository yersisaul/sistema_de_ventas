package com.integrador.sistema_de_ventas.springboot_app.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/vista")
public class ViewController {
    // Admin Views
    @GetMapping("/admin/login")
    public String adminLogin() {
        return "admin/login";
    }

    @GetMapping("/admin/dashboard")
    public String adminDashboard() {
        return "admin/dashboard";
    }

    @GetMapping("/admin/clientes")
    public String adminClientes() {
        return "admin/clientes";
    }

    @GetMapping("/admin/usuarios")
    public String adminUsuarios() {
        return "admin/usuarios";
    }

    @GetMapping("/admin/inventario")
    public String adminInventario() {
        return "admin/inventario";
    }

    @GetMapping("/admin/pedidos")
    public String adminPedidos() {
        return "admin/pedidos";
    }

    // Client Views
    @GetMapping("/client/home")
    public String clientHome() {
        return "client/Home";
    }

    @GetMapping("/client/productos")
    public String clientProductos() {
        return "client/Productos";
    }

    @GetMapping("/client/contactanos")
    public String clientContactanos() {
        return "client/Contactanos";
    }

    @GetMapping("/client/vestidor")
    public String clientVestidor() {
        return "client/Vestidor";
    }

    // Home
    @GetMapping("/")
    public String home() {
        return "client/Home";
    }
}
