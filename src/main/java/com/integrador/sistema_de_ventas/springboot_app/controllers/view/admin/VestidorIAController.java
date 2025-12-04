package com.integrador.sistema_de_ventas.springboot_app.controllers.view.admin;

import java.nio.file.Paths;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import org.springframework.core.io.FileSystemResource;
import java.io.File;
import java.nio.file.Path;
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

}
