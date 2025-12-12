package com.integrador.sistema_de_ventas.springboot_app.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class SecurityConfig {
    
    private final CustomSuccessHandler customSuccessHandler;
    private final UserDetailsService userDetailsService;

    public SecurityConfig(UserDetailsService userDetailsService, CustomSuccessHandler customSuccessHandler) {
        this.userDetailsService = userDetailsService;
        this.customSuccessHandler = customSuccessHandler;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                // 1. RECURSOS PÚBLICOS (CSS, JS, IMÁGENES, API PÚBLICA)
                .requestMatchers(
                    "/api/**", 
                    "/client/**",
                    "/", 
                    "/uploads/**",
                    "/productos/**", 
                    "/css/**", 
                    "/Img/**", 
                    "/js/**", 
                    "/static/**",
                    "/templates/**",
                    "/admin/login",
                    "/api/admin/auth/**",
                    "/api/client/auth/**",
                    "/v3/api-docs/**",
                    "/swagger-ui/**", 
                    "/swagger-ui.html"
                ).permitAll()

                // 2. REGLAS DE ACCESO POR ROL (Aquí está el cambio clave)

                // A. SOLO ADMINISTRADOR (Gestión de Usuarios/Empleados)
                .requestMatchers("/admin/usuarios/**").hasRole("ADMINISTRADOR")

                // B. ADMINISTRADOR y VENDEDOR (Dashboard, Clientes, Ventas)
                .requestMatchers(
                    "/admin/dashboard", 
                    "/admin/clientes/**", 
                    "/admin/ventas/**" // Cubre lista y nueva venta
                ).hasAnyRole("ADMINISTRADOR", "VENDEDOR")

                // C. ADMINISTRADOR, VENDEDOR y CLIENTE (Pedidos e Inventario)
                .requestMatchers(
                    "/admin/pedidos/**", 
                    "/admin/inventario/**"
                ).hasAnyRole("ADMINISTRADOR", "VENDEDOR", "CLIENTE")

                // D. API PROTEGIDA (Opcional, ajusta según necesidad)
                .requestMatchers("/api/admin/**").hasAnyRole("ADMINISTRADOR", "VENDEDOR")

                // CUALQUIER OTRA RUTA REQUIERE LOGIN
                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage("/admin/login")
                .loginProcessingUrl("/login")
                .successHandler(customSuccessHandler)
                .failureUrl("/admin/login?error=true")
                .permitAll()
            )
            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/admin/login?logout=true")
                .permitAll()
            )
            .authenticationProvider(authenticationProvider());

        return http.build();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/api/**")
                    .allowedOrigins("*")
                    .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                    .allowedHeaders("*")
                    .allowCredentials(false)
                    .maxAge(3600);
            }
        };
    }
}