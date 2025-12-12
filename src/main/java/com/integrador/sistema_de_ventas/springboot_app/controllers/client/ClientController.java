package com.integrador.sistema_de_ventas.springboot_app.controllers.client;

import com.integrador.sistema_de_ventas.springboot_app.models.Pedido;
import com.integrador.sistema_de_ventas.springboot_app.models.Usuario;
import com.integrador.sistema_de_ventas.springboot_app.repository.PedidoRepository;
import com.integrador.sistema_de_ventas.springboot_app.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/client")
public class ClientController {

    @Autowired private UsuarioRepository usuarioRepository;
    @Autowired private PedidoRepository pedidoRepository;

    // Método auxiliar para obtener el usuario actual
    private Usuario getUsuarioLogueado() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String dni = auth.getName();
        return usuarioRepository.findByNIdentificacion(dni).orElse(null);
    }

    // 1. MIS PEDIDOS
    @GetMapping("/mispedidos")
    public String misPedidos(Model model) {
        Usuario usuario = getUsuarioLogueado();
        if (usuario != null) {
            // Usamos el repositorio para buscar SOLO los pedidos de este cliente
            List<Pedido> misPedidos = pedidoRepository.findPedidosClienteOrdenados(usuario.getId());
            model.addAttribute("listaPedidos", misPedidos);
            model.addAttribute("clienteNombre", usuario.getNombres());
        }
        model.addAttribute("activePage", "mispedidos");
        return "client/mispedidos"; // templates/client/mispedidos.html
    }

    // 2. CONFIGURACIÓN (PERFIL)
    @GetMapping("/configuracion")
    public String configuracion(Model model) {
        Usuario usuario = getUsuarioLogueado();
        model.addAttribute("usuario", usuario);
        model.addAttribute("activePage", "configuracion");
        return "client/configuracion"; // templates/client/configuracion.html
    }
    
    // 3. ACTUALIZAR DATOS (POST)
    @PostMapping("/actualizar-perfil")
    public String actualizarPerfil(Usuario usuarioEditado) {
        Usuario usuarioActual = getUsuarioLogueado();
        if(usuarioActual != null) {
            usuarioActual.setNombres(usuarioEditado.getNombres());
            usuarioActual.setApellidos(usuarioEditado.getApellidos());
            usuarioActual.setTelefono(usuarioEditado.getTelefono());
            usuarioActual.setDireccion(usuarioEditado.getDireccion());
            usuarioRepository.save(usuarioActual);
        }
        return "redirect:/client/configuracion?exito=true";
    }
}