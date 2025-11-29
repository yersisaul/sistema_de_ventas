package com.integrador.sistema_de_ventas.springboot_app.controllers.view.admin;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.integrador.sistema_de_ventas.springboot_app.models.Usuario;
import com.integrador.sistema_de_ventas.springboot_app.services.impl.UsuarioServiceImpl;
import org.springframework.web.bind.annotation.PostMapping;


@Controller
@RequestMapping("/admin/clientes")
public class ListClienteVista {
    private final UsuarioServiceImpl usuarioServiceImpl;

    public ListClienteVista( UsuarioServiceImpl usuarioServiceImpl){
        this.usuarioServiceImpl = usuarioServiceImpl;
    }

    @GetMapping
    public String clientes(Model model) {
        model.addAttribute("activePage", "clientes");
        model.addAttribute("nuevoCliente", new Usuario());
        model.addAttribute("listaClientes", usuarioServiceImpl.obtenerTodosLosUsuarios());
        return "admin/clientes";
    }

    @PostMapping("/guardar")
    public String crearCliente(@ModelAttribute("nuevoCliente") Usuario usuario, Model model){
        try{
            usuarioServiceImpl.crearUsuario(usuario);
            return "redirect:/admin/clientes";
        }catch(RuntimeException e){
            model.addAttribute("error", e.getMessage());
            model.addAttribute("activePage", "clientes");
            model.addAttribute("listaClientes", usuarioServiceImpl.obtenerTodosLosUsuarios());
            return "admin/clientes";
        }
    }
    
}
