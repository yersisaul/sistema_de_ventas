package com.integrador.sistema_de_ventas.springboot_app.controllers.view.admin;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.integrador.sistema_de_ventas.springboot_app.dto.CategoriaResponseDTO;
import com.integrador.sistema_de_ventas.springboot_app.dto.ProductoCreateDTO;
import com.integrador.sistema_de_ventas.springboot_app.dto.ProductoUpdateDTO;
import com.integrador.sistema_de_ventas.springboot_app.models.Categoria;
import com.integrador.sistema_de_ventas.springboot_app.models.Producto;
import com.integrador.sistema_de_ventas.springboot_app.services.impl.CategoriaServiceImpl;
import com.integrador.sistema_de_ventas.springboot_app.services.impl.ProductoServiceImpl;
import com.integrador.sistema_de_ventas.springboot_app.services.impl.VarianteProductoServiceImpl;

@Controller
@RequestMapping("/admin/inventario")
public class InventarioVista {

    @Autowired
    ProductoServiceImpl productoServiceImpl;

    @Autowired
    CategoriaServiceImpl categoriaServiceImpl;

    @Autowired
    VarianteProductoServiceImpl varianteService;

    @GetMapping
    public String inventario(Model model) {
        model.addAttribute("nuevoProducto", new ProductoCreateDTO());
        model.addAttribute("nuevaCategoria", new Categoria());
        model.addAttribute("listaProductos", productoServiceImpl.obtenerProductosActivos());
        model.addAttribute("listaCategorias", categoriaServiceImpl.obtenerTodasLasCategorias());
        model.addAttribute("activePage", "inventario");
        return "admin/inventario";
    }

    @PostMapping("/categoria")
    public String crearCategoria(@ModelAttribute("nuevaCategoria") CategoriaResponseDTO categoria, Model model) {
        try {
            categoriaServiceImpl.crearCategoria(categoria);
            return "redirect:/admin/inventario";
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("activePage", "inventario");
            model.addAttribute("listaCategorias", categoriaServiceImpl.obtenerTodasLasCategorias());
            model.addAttribute("listaProductos", productoServiceImpl.obtenerProductosActivos());
            return "redirect:/admin/inventario";
        }
    }

    @PostMapping("/categoria/eliminar/{id}")
    public String eliminarCategoria(@PathVariable Long id) {
        categoriaServiceImpl.eliminarCategoria(id);
        return "redirect:/admin/inventario";
    }

    @PostMapping("/categoria/editar/{id}")
    @ResponseBody
    public String editarCategoria(@PathVariable Long id, @RequestBody CategoriaResponseDTO dto) {
        categoriaServiceImpl.editarCategoria(id, dto);
        return "ok";
    }

    @PostMapping("/nuevo_producto")
    public String crearProducto(@ModelAttribute("nuevoProducto") ProductoCreateDTO producto, Model model) {
        try {
            productoServiceImpl.crearProducto(producto);
            return "redirect:/admin/inventario";
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("listaProductos", productoServiceImpl.obtenerProductosActivos());
            model.addAttribute("listaCategorias", categoriaServiceImpl.obtenerTodasLasCategorias());
            model.addAttribute("nuevoProducto", producto);
            return "redirect:admin/inventario";
        }

    }

     @GetMapping("/editar/{id}")
    public String mostrarEditarProducto(@PathVariable Long id, Model model) {
        Producto producto = productoServiceImpl.obtenerProductoPorId(id)
            .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
        
        // Convertir a DTO para edición
        ProductoUpdateDTO productoDTO = new ProductoUpdateDTO();
        productoDTO.setNombre(producto.getNombre());
        productoDTO.setDescripcion(producto.getDescripcion());
        productoDTO.setCategoriaId(producto.getCategoria() != null ? producto.getCategoria().getId() : null);
        
        model.addAttribute("producto", productoDTO);
        model.addAttribute("productoId", id);
        model.addAttribute("listaCategorias", categoriaServiceImpl.obtenerTodasLasCategorias());
        return "admin/editar-producto"; // Crear esta vista
    }

    @PostMapping("/editar/{id}")
    public String editarProducto(@PathVariable Long id, 
                                @ModelAttribute("producto") ProductoUpdateDTO productoDTO) {
        productoServiceImpl.actualizarProducto(id, productoDTO);
        return "redirect:/admin/inventario";
    }

    @PostMapping("/eliminar/{id}")
    public String eliminarProducto(@PathVariable Long id) {
        productoServiceImpl.eliminarProducto(id);
        return "redirect:/admin/inventario";
    }

    @PostMapping("/agregar-stock")
    public String agregarStock(
            @RequestParam("productoId") Long productoId,

            @RequestParam(value = "stockS", required = false, defaultValue = "0") Integer stockS,
            @RequestParam(value = "precioS", required = false) BigDecimal precioS,

            @RequestParam(value = "stockM", required = false, defaultValue = "0") Integer stockM,
            @RequestParam(value = "precioM", required = false) BigDecimal precioM,

            @RequestParam(value = "stockL", required = false, defaultValue = "0") Integer stockL,
            @RequestParam(value = "precioL", required = false) BigDecimal precioL,

            @RequestParam(value = "stockXL", required = false, defaultValue = "0") Integer stockXL,
            @RequestParam(value = "precioXL", required = false) BigDecimal precioXL,

            RedirectAttributes flash) {
        try {

            varianteService.agregarOActualizarStock(productoId, "S", stockS, precioS);
            varianteService.agregarOActualizarStock(productoId, "M", stockM, precioM);
            varianteService.agregarOActualizarStock(productoId, "L", stockL, precioL);
            varianteService.agregarOActualizarStock(productoId, "XL", stockXL, precioXL);

            flash.addFlashAttribute("success", "Stock actualizado correctamente");

        } catch (Exception e) {
            flash.addFlashAttribute("error", "Error al actualizar stock: " + e.getMessage());
        }

        return "redirect:/admin/inventario";
    }
}
