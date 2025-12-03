package com.integrador.sistema_de_ventas.springboot_app.controllers.view.admin;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.util.Base64;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import org.springframework.core.io.FileSystemResource;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import java.util.Collections;

@RestController
@RequestMapping("/api/vestidor/ia")
@CrossOrigin(origins = "*") // Para evitar problemas de CORS con tu frontend
public class VestidorIAController {

    // ⚠️ REEMPLAZA CON TU API KEY REAL
    private static final String API_KEY = "sk_live_931S1SbEIwPyJk4rXbkX6PU_KfuiK9714DH1Mv_AC8M";
    
    // URLs OFICIALES V1
    private static final String URL_GENERATE = "https://backend.miragic.ai/api/v1/virtual-try-on";
    private static final String URL_STATUS_BASE = "https://backend.miragic.ai/api/v1/virtual-try-on/";

    private static final long POLLING_INTERVAL = 3000; // 3 segundos
    private static final long MAX_WAIT_TIME = 60000;   // 60 segundos
    
    private final Logger log = LoggerFactory.getLogger(VestidorIAController.class);
    private final RestTemplate restTemplate = new RestTemplate();

    @PostMapping("/procesar")
    public ResponseEntity<?> procesarVestidor(
            @RequestParam("fotoCliente") MultipartFile fotoCliente,
            @RequestParam("productoUrl") String productoUrl,
            @RequestParam(value = "categoria", defaultValue = "upper_body") String categoria // upper_body, lower_body, dresses
    ) {
        try {
            log.info("==========================================");
            log.info("🚀 INICIO PROCESO VESTIDOR IA");
            log.info("📸 Cliente Archivo: {} ({} bytes)", fotoCliente.getOriginalFilename(), fotoCliente.getSize());
            log.info("👗 Producto URL: {}", productoUrl);
            log.info("🏷️ Categoría: {}", categoria);

            // 1. Iniciar el trabajo (POST Multipart)
            String jobId = iniciarTrabajo(fotoCliente, productoUrl, categoria);
            
            if (jobId == null) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("No se pudo obtener el ID del trabajo.");
            }

            // 2. Esperar el resultado (POLLING)
            String imagenFinalUrl = esperarResultado(jobId);

            log.info("✅ PROCESO TERMINADO CON ÉXITO");
            log.info("🔗 URL Resultado: {}", imagenFinalUrl);
            log.info("==========================================");

            // Devolvemos un JSON simple con la URL
            JSONObject respuesta = new JSONObject();
            respuesta.put("output", imagenFinalUrl);
            respuesta.put("status", "success");

            return ResponseEntity.ok(respuesta.toString());

        } catch (Exception e) {
            log.error("❌ ERROR CRÍTICO EN EL CONTROLADOR", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error en el servidor: " + e.getMessage());
        }
    }

   private String iniciarTrabajo(MultipartFile fotoCliente, String productoUrl, String categoria) throws Exception {
    log.info("--- Paso 1: Enviando solicitud a Miragic API ---");

    HttpHeaders headers = new HttpHeaders();
    headers.set("X-API-Key", API_KEY);
    headers.setContentType(MediaType.MULTIPART_FORM_DATA);

    MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();

    // A. Imagen del Cliente (igual que antes)
    ByteArrayResource clienteResource = new ByteArrayResource(fotoCliente.getBytes()) {
        @Override
        public String getFilename() { return fotoCliente.getOriginalFilename(); }
    };
    body.add("humanImage", clienteResource);

    // B. Imagen del Producto (CORREGIDO: Leer del disco local)
    // 1. Limpiamos la URL para obtener solo el nombre del archivo
    // Ejemplo entrada: "http://localhost:8080/uploads/productos/foto.jpg" o "/uploads/productos/foto.jpg"
    String nombreArchivo = productoUrl;
    if (nombreArchivo.contains("/uploads/productos/")) {
        // Cortamos todo lo que esté antes del nombre del archivo
        nombreArchivo = nombreArchivo.substring(nombreArchivo.lastIndexOf("/") + 1);
    }
    
    // 2. Construimos la ruta física en tu servidor (la misma que usas en GuardadoImgService)
    Path rutaArchivo = Paths.get("uploads/productos").resolve(nombreArchivo).toAbsolutePath();
    File archivoFisico = rutaArchivo.toFile();

    log.info("📂 Buscando archivo local en: {}", archivoFisico.toString());

    if (!archivoFisico.exists()) {
        throw new RuntimeException("No se encuentra la imagen del producto en el servidor: " + nombreArchivo);
    }

    // 3. Enviamos el archivo físico como recurso
    body.add("clothImage", new FileSystemResource(archivoFisico));

    // C. Categoría
    body.add("garmentType", categoria);

    // Enviar Petición
    HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);
    
    try {
        ResponseEntity<String> response = restTemplate.postForEntity(URL_GENERATE, requestEntity, String.class);
        
        log.info("📡 Respuesta Status: {}", response.getStatusCode());
        log.info("📦 Respuesta Body: {}", response.getBody());
        
        if (response.getStatusCode() == HttpStatus.OK || response.getStatusCode() == HttpStatus.CREATED|| 
    response.getStatusCode() == HttpStatus.ACCEPTED) {

            JSONObject jsonResponse = new JSONObject(response.getBody());

            if (jsonResponse.has("jobId")) {
                return jsonResponse.getString("jobId");
            } else if (jsonResponse.has("data")) {
                return jsonResponse.getJSONObject("data").getString("jobId");
            }
        }
    } catch (Exception e) {
        log.error("❌ Error conectando con Miragic", e);
        throw e;
    }
    
    throw new RuntimeException("No se recibió un Job ID válido");
}

    private String esperarResultado(String jobId) throws Exception {
        log.info("--- Paso 2: Esperando generación (Polling) para Job ID: {} ---", jobId);
        
        long tiempoInicio = System.currentTimeMillis();
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-API-Key", API_KEY);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        HttpEntity<String> entity = new HttpEntity<>(headers);

        while ((System.currentTimeMillis() - tiempoInicio) < MAX_WAIT_TIME) {
            try {
                String urlStatus = URL_STATUS_BASE + jobId;
                ResponseEntity<String> response = restTemplate.exchange(urlStatus, HttpMethod.GET, entity, String.class);
                
                JSONObject json = new JSONObject(response.getBody());
                String status = "";

                // Manejo de la estructura de respuesta (puede variar, así que protegemos)
                if (json.has("status")) {
                    status = json.getString("status");
                } else if (json.has("data")) {
                    status = json.getJSONObject("data").getString("status");
                }

                log.info("⏳ Estado actual: {}", status);

                if ("completed".equalsIgnoreCase(status) || "succeeded".equalsIgnoreCase(status)) {
                    // EXITO: Extraer URL
                    if (json.has("data") && json.getJSONObject("data").has("resultImagePath")) {
                         return json.getJSONObject("data").getString("resultImagePath");
                    }
                    // Fallback si la estructura es diferente
                    return json.getString("resultImage"); 
                } else if ("failed".equalsIgnoreCase(status)) {
                    throw new RuntimeException("La IA falló al procesar la imagen.");
                }

                Thread.sleep(POLLING_INTERVAL);

            } catch (Exception e) {
                log.warn("⚠️ Error leve durante el polling (reintentando...): {}", e.getMessage());
                Thread.sleep(POLLING_INTERVAL);
            }
        }

        throw new RuntimeException("Tiempo de espera agotado (Timeout)");
    }
}

