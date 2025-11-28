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
            .authorizeHttpRequests(auth -> auth
                // URLs públicas - INCLUYENDO EL PREFIJO /vista
                .requestMatchers(
                    "/api/**",  // ← PERMITIR TODAS LAS RUTAS API
                    "/client/**",
                    "/", 
                    "/productos/**", 
                    "/css/**", 
                    "/img/**", 
                    "/js/**", 
                    "/static/**",
                    "/templates/**",
                    "/admin/login",
                    "/api/admin/auth/**",
                    "/api/client/**", 
                    "/api/client/auth/**",
                    "/v3/api-docs/**",
                    "/swagger-ui/**", 
                    "/swagger-ui.html",
                    "/swagger-resources/**",
                    "/webjars/**",
                    "/configuration/ui",
                    "/configuration/security"
                ).permitAll()
                // Rutas administrativas ACTUALIZADAS
                .requestMatchers("/admin/clientes", "/admin/dashboard", "/admin/inventario", "/admin/usuarios").hasRole("ADMIN")
                .requestMatchers("/admin/pedidos").hasAnyRole("ADMIN", "CLIENTE")
                .requestMatchers("/api/admin/**").hasRole("ADMIN")
                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage("/vista/admin/login")
                .loginProcessingUrl("/login")
                .successHandler(customSuccessHandler)
                .failureUrl("/vista/admin/login?error=true")
                .permitAll()
            )
            .logout(logout -> logout
                .logoutSuccessUrl("/vista/admin/login?logout=true")
                .permitAll()
            )
            .authenticationProvider(authenticationProvider())
            .csrf(csrf -> csrf.disable());

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