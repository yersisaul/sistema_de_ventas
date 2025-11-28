package com.integrador.sistema_de_ventas.springboot_app.exception;

public class UnauthorizedException extends RuntimeException {
    public UnauthorizedException(String mensaje) {
        super(mensaje);
    }
    
    public UnauthorizedException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
}
