package com.integrador.sistema_de_ventas.springboot_app.config;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Set;

@Component
public class CustomSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        
        // Convertimos los roles a un Set de Strings para buscar fácil
        Set<String> roles = AuthorityUtils.authorityListToSet(authentication.getAuthorities());

        // 1. Si es ADMINISTRADOR (Coincide con tu Enum + prefijo ROLE_)
        if (roles.contains("ROLE_ADMINISTRADOR")) {
            response.sendRedirect("/admin/dashboard"); // O /admin/dashboard si lo creas luego
        } 
        // 2. Si es VENDEDOR
        else if (roles.contains("ROLE_VENDEDOR")) {
            response.sendRedirect("/admin/pedidos");
        }
        // 3. Si es CLIENTE
        else if (roles.contains("ROLE_CLIENTE")) {
            // CORREGIDO: El cliente va a la tienda, no al admin
            response.sendRedirect("/client/mispedidos"); 
        } 
        // 4. Default
        else {
            response.sendRedirect("/");
        }
    }
}