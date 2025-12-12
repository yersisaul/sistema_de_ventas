package com.integrador.sistema_de_ventas.springboot_app.controllers.view.admin;

import com.integrador.sistema_de_ventas.springboot_app.models.Usuario;
import com.integrador.sistema_de_ventas.springboot_app.services.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/clientes")
public class ListClienteVista {

    @Autowired
    private UsuarioService usuarioService;

    // 1. LISTAR (Carga la página HTML con la tabla)
    @GetMapping("")
    public String listarClientes(Model model) {
        // Obtenemos solo los usuarios con rol CLIENTE para mostrar en la tabla
        model.addAttribute("listaClientes", usuarioService.obtenerUsuariosPorRol(Usuario.Rol.CLIENTE));
        
        // Objeto vacío para el formulario del modal (necesario para th:object="${nuevoCliente}")
        model.addAttribute("nuevoCliente", new Usuario());
        
        // Variable para resaltar "Clientes" en el Navbar
        model.addAttribute("activePage", "clientes");
        
        return "admin/clientes"; // Busca: src/main/resources/templates/admin/clientes.html
    }

    // 2. GUARDAR (Sirve tanto para CREAR como para EDITAR)
    @PostMapping("/guardar")
    public String guardarCliente(@ModelAttribute("nuevoCliente") Usuario usuario) {
        try {
            // Forzamos el rol CLIENTE (seguridad por si intentan inyectar otro rol)
            usuario.setRol(Usuario.Rol.CLIENTE);
            
            // Lógica para saber si es Editar o Crear
            if (usuario.getId() != null && usuario.getId() > 0) {
                // EDITAR: El servicio se encarga de actualizar y manejar la contraseña
                usuarioService.actualizarUsuario(usuario.getId(), usuario);
            } else {
                // CREAR: El servicio crea uno nuevo
                usuarioService.crearUsuario(usuario);
            }
            return "redirect:/admin/clientes?exito=true";
        } catch (Exception e) {
            // Si falla (ej: correo duplicado), volvemos con un mensaje de error
            return "redirect:/admin/clientes?error=" + e.getMessage();
        }
    }

    // 3. ELIMINAR (Soft Delete)
    @GetMapping("/eliminar/{id}")
    public String eliminarCliente(@PathVariable Long id) {
        try {
            usuarioService.eliminarUsuario(id);
            return "redirect:/admin/clientes?eliminado=true";
        } catch (Exception e) {
            return "redirect:/admin/clientes?error=" + e.getMessage();
        }
    }
}