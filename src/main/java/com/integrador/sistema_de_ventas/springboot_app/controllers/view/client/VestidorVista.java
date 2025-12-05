package com.integrador.sistema_de_ventas.springboot_app.controllers.view.client;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.integrador.sistema_de_ventas.springboot_app.dto.ProductoDTO;
import com.integrador.sistema_de_ventas.springboot_app.services.impl.CategoriaServiceImpl;
import com.integrador.sistema_de_ventas.springboot_app.services.impl.ProductoServiceImpl;

@Controller
@RequestMapping("/client")
public class VestidorVista {
    @Autowired
    ProductoServiceImpl productoServiceImpl;

    @Autowired
    CategoriaServiceImpl categoriaServiceImpl;

    @GetMapping("/vestidor")
    public String vestidor(Model model){
        List<ProductoDTO> listaProductos = productoServiceImpl.obtenerProductosParaCatalogo();
        model.addAttribute("listaProductos", listaProductos);
        model.addAttribute("listaCategorias", categoriaServiceImpl.obtenerTodasLasCategorias());
        return "client/Vestidor";
    }

    
}
