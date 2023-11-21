package com.example.getclothes.tiendas.mapper;

import com.example.getclothes.tiendas.models.Tienda;
import com.example.getclothes.tiendas.models.TiendaDto;
import org.springframework.stereotype.Component;

@Component
public class TiendaMapper {
    public static Tienda convertirDTOaTienda(TiendaDto dto) {
        Tienda tienda = Tienda.builder()
                .ciudad(dto.getCiudad())
                .direccion(dto.getDireccion())
                .telefono(dto.getTelefono())
                .build();

        return tienda;
    }

    public static TiendaDto convertirTiendaaDTO(Tienda tienda) {
        TiendaDto dto = TiendaDto.builder()
                .ciudad(tienda.getCiudad())
                .direccion(tienda.getDireccion())
                .telefono(tienda.getTelefono())
                .build();
        return dto;
    }
}
