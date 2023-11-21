package com.example.getclothes.tiendas.models;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class TiendaDto {
    @NotBlank(message = "La ciudad no puede estar vacia")
    String ciudad;
    @NotBlank(message = "La direccion no puede estar vacia")
    String direccion;
    @NotBlank(message = "El telefono no puede estar vacio")
    String telefono;
}
