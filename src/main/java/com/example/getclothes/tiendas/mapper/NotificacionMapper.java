package com.example.getclothes.tiendas.mapper;

import com.example.getclothes.tiendas.models.Tienda;
import com.example.getclothes.tiendas.websockets.TiendaNotificacion;
import org.springframework.stereotype.Component;

@Component
public class NotificacionMapper {
    public TiendaNotificacion convertirTiendaATiendaNotificacion(Tienda tienda){
        return new TiendaNotificacion(
                tienda.getId(), tienda.getCiudad(), tienda.getDireccion(), tienda.getTelefono()
        );
    }
}
