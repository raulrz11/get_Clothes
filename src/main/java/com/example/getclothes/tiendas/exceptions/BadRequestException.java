package com.example.getclothes.tiendas.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class BadRequestException extends TiendaException{
    public BadRequestException(String mensaje) {
        super(mensaje);
    }
}
