package com.integrador.sistema_de_ventas.springboot_app.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.integrador.sistema_de_ventas.springboot_app.models.Usuario;
import com.integrador.sistema_de_ventas.springboot_app.repository.UsuarioRepository;

/**
 * Inicializa datos por defecto al arrancar la aplicación
 * Crea un usuario administrador y un usuario cliente de prueba
 */
//@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        try {
            inicializarUsuarioAdmin();
            inicializarUsuarioCliente();
        } catch (Exception e) {
            System.err.println("Error al inicializar datos: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void inicializarUsuarioAdmin() { //Inicializa el usuario administrador por defecto
        if (usuarioRepository.findByCorreo("admin1@mambo.com").isEmpty()) {
            Usuario admin = new Usuario();

            admin.setTipoIdentificacion("DNI");
            admin.setNIdentificacion("74055582");
            admin.setNombres("Administrador1");
            admin.setApellidos("Mambo");
            admin.setCorreo("admin1@mambo.com");
            admin.setContrasena(passwordEncoder.encode("admin123"));
            admin.setTelefono("997304509");
            admin.setDireccion("Av. la colectora, Ate");
            admin.setRol("ADMIN");
            admin.setEstado(true);

            usuarioRepository.save(admin);
            System.out.println("✓ Usuario administrador creado exitosamente");
            System.out.println("  Email: admin1@mambo.com");
            System.out.println("  Contraseña: admin123");
        } else {
            System.out.println("ℹ Usuario administrador ya existe en la base de datos");
        }
    }

    private void inicializarUsuarioCliente() { // Inicializa un usuario cliente de prueba
        if (usuarioRepository.findByCorreo("cliente1@mambo.com").isEmpty()) {
            Usuario cliente = new Usuario();

            cliente.setTipoIdentificacion("DNI");
            cliente.setNIdentificacion("23644040");
            cliente.setNombres("Cliente Prueba");
            cliente.setApellidos("Mambo");
            cliente.setCorreo("cliente1@mambo.com");
            cliente.setContrasena(passwordEncoder.encode("cliente123"));
            cliente.setTelefono("123456789");
            cliente.setDireccion("Carretera central");
            cliente.setRol("CLIENTE");
            cliente.setEstado(true);

            usuarioRepository.save(cliente);
            System.out.println("✓ Usuario cliente de prueba creado exitosamente");
            System.out.println("  Email: cliente1@mambo.com");
            System.out.println("  Contraseña: cliente123");
        } else {
            System.out.println("ℹ Usuario cliente de prueba ya existe en la base de datos");
        }
    }
}
