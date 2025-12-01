package com.integrador.sistema_de_ventas.springboot_app.controllers.view.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import com.integrador.sistema_de_ventas.springboot_app.services.impl.ProductoServiceImpl;

@Controller
@RequestMapping("/client/productos")
public class ProductosVista {
    @Autowired
    ProductoServiceImpl productoServiceImpl;

    @GetMapping
    public String productos(Model model) {
        model.addAttribute("listaProductos", productoServiceImpl.obtenerTodosLosProductos());
        return "client/Produtos";
    }


}
