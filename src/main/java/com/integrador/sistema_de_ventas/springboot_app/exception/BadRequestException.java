package com.integrador.sistema_de_ventas.springboot_app.exception;

public class BadRequestException extends RuntimeException {
    public BadRequestException(String mensaje) {
        super(mensaje);
    }
    
    public BadRequestException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
}
