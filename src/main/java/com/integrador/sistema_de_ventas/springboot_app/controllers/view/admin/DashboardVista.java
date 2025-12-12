package com.integrador.sistema_de_ventas.springboot_app.controllers.view.admin;

import com.integrador.sistema_de_ventas.springboot_app.models.Usuario;
import com.integrador.sistema_de_ventas.springboot_app.repository.UsuarioRepository;
import com.integrador.sistema_de_ventas.springboot_app.services.DashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class DashboardVista {

    @Autowired 
    private DashboardService dashboardService;
    
    @Autowired 
    private UsuarioRepository usuarioRepository;

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        // 1. Obtener nombre del Administrador logueado
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName(); // DNI
        
        Usuario admin = usuarioRepository.findByNIdentificacion(username)
                .orElse(new Usuario()); // Evita null pointer si no lo encuentra
        
        // Pasamos el nombre para el saludo "Hola Juan"
        model.addAttribute("adminNombre", admin.getNombres());

        // 2. Tarjetas Informativas (Ventas y Clientes)
        model.addAttribute("ventasDia", dashboardService.obtenerVentasDelDia());
        model.addAttribute("clientesNuevos", dashboardService.obtenerClientesNuevos());

        // 3. Tabla de Pedidos Recientes
        model.addAttribute("pedidosRecientes", dashboardService.obtenerPedidosRecientes());

        // 4. Datos para el Gráfico
        model.addAttribute("datosGrafico", dashboardService.obtenerDatosGrafico());

        // 5. Marcar página activa en el Navbar
        model.addAttribute("activePage", "dashboard");
        
        return "admin/dashboard"; // Renderiza el HTML
    }
}