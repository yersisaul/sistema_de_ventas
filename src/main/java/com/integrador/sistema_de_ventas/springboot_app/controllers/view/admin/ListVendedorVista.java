package com.integrador.sistema_de_ventas.springboot_app.controllers.view.admin;

import com.integrador.sistema_de_ventas.springboot_app.models.Usuario;
import com.integrador.sistema_de_ventas.springboot_app.services.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/usuarios") // La ruta que usas en el navbar
public class ListVendedorVista {

    @Autowired
    private UsuarioService usuarioService;

    // 1. LISTAR VENDEDORES
    @GetMapping("")
    public String listarVendedores(Model model) {
        // Filtramos por rol VENDEDOR
        model.addAttribute("listaVendedores", usuarioService.obtenerUsuariosPorRol(Usuario.Rol.VENDEDOR));
        // Objeto vacío para el modal
        model.addAttribute("nuevoVendedor", new Usuario());
        // Para resaltar en el navbar
        model.addAttribute("activePage", "usuarios");
        
        return "admin/usuarios"; // Busca templates/admin/usuarios.html
    }

    // 2. GUARDAR (Crear o Editar)
    @PostMapping("/guardar")
    public String guardarVendedor(@ModelAttribute("nuevoVendedor") Usuario usuario) {
        try {
            // Forzamos el rol VENDEDOR
            usuario.setRol(Usuario.Rol.VENDEDOR);
            
            // Si tiene ID, actualizamos. Si no, creamos.
            if (usuario.getId() != null && usuario.getId() > 0) {
                usuarioService.actualizarUsuario(usuario.getId(), usuario);
            } else {
                usuarioService.crearUsuario(usuario);
            }
            return "redirect:/admin/usuarios?exito=true";
        } catch (Exception e) {
            return "redirect:/admin/usuarios?error=" + e.getMessage();
        }
    }

    // 3. ELIMINAR
    @GetMapping("/eliminar/{id}")
    public String eliminarVendedor(@PathVariable Long id) {
        try {
            usuarioService.eliminarUsuario(id);
            return "redirect:/admin/usuarios?eliminado=true";
        } catch (Exception e) {
            return "redirect:/admin/usuarios?error=" + e.getMessage();
        }
    }
}