package com.integrador.sistema_de_ventas.springboot_app.services;

import com.integrador.sistema_de_ventas.springboot_app.repository.PedidoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ComprobanteGeneratorService {
    
    @Autowired
    private PedidoRepository pedidoRepository;
    
    /**
     * Genera el siguiente número de comprobante correlativo
     * Este método debe ser llamado JUSTO ANTES de guardar el pedido
     */
    @Transactional
    public String generarSiguienteNumero(String tipo, String serie) {
        try {
            // Contar total de pedidos en la BD
            Long totalPedidos = pedidoRepository.contarTotalPedidos();
            
            // El siguiente número es: total + 1
            long siguienteNumero = (totalPedidos != null ? totalPedidos : 0L) + 1;
            
            // Formatear a 8 dígitos con ceros a la izquierda
            String numero = String.format("%08d", siguienteNumero);
            
            System.out.println("✅ Número generado: " + numero + " (Total pedidos: " + totalPedidos + ")");
            
            return numero;
            
        } catch (Exception e) {
            System.err.println("❌ Error al generar número de comprobante: " + e.getMessage());
            e.printStackTrace();
            
            // Fallback: usar timestamp
            long timestamp = System.currentTimeMillis() % 100000000;
            return String.format("%08d", timestamp);
        }
    }
    
    /**
     * Genera el comprobante completo con serie
     */
    public String generarComprobanteCompleto(String tipo, String serie) {
        String numero = generarSiguienteNumero(tipo, serie);
        return serie + "-" + numero;
    }
}