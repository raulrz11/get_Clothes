package com.example.getclothes.tiendas.websockets;

import java.util.UUID;

public record TiendaNotificacion(
        UUID id,
        String ciudad,
        String direccion,
        String telefono
) {
}
