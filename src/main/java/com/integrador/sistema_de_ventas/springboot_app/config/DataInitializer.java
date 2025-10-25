package com.integrador.sistema_de_ventas.springboot_app.config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.boot.CommandLineRunner;
import com.integrador.sistema_de_ventas.springboot_app.models.Usuario;
import com.integrador.sistema_de_ventas.springboot_app.repository.UsuarioRepository;

@Component
public class DataInitializer implements CommandLineRunner {
    @Autowired
    private UsuarioRepository usuarioRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Override
    public void run(String... args) throws Exception {
        inicializarUsuarioAdmin();
    }
    
    private void inicializarUsuarioAdmin() {
        if (usuarioRepository.findByCorreo("admin@mambo.com").isEmpty()) { // Verificar si ya existe un usuario admin
            Usuario admin = new Usuario();
            admin.setNombreCompleto("Administrador del Sistema");
            admin.setCorreo("admin@mambo.com");
            admin.setContrasena(passwordEncoder.encode("admin123")); // Contraseña encriptada
            admin.setRol("ADMIN");
            admin.setEstado(true);
            
            usuarioRepository.save(admin);
            System.out.println("Usuario administrador creado exitosamente");
            System.out.println("Email: admin@mambo.com");
            System.out.println("Contraseña: admin123");
        } else {
            System.out.println("ℹUsuario administrador ya existe en la base de datos");
        }
        
        if (usuarioRepository.findByCorreo("cliente1@mambo.com").isEmpty()) { // Opcional: Crear un usuario cliente de prueba
            Usuario cliente = new Usuario();
            cliente.setNombreCompleto("Cliente de Prueba");
            cliente.setCorreo("cliente1@mambo.com");
            cliente.setContrasena(passwordEncoder.encode("cliente123"));
            cliente.setRol("CLIENTE");
            cliente.setEstado(true);
            
            usuarioRepository.save(cliente);
            System.out.println("Usuario cliente creado exitosamente");
        }
    }
}
