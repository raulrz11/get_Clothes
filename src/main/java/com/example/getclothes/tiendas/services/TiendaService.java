package com.example.getclothes.tiendas.services;

import com.example.getclothes.tiendas.models.Tienda;
import com.example.getclothes.tiendas.models.TiendaDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;

public interface TiendaService {
    Tienda save(TiendaDto tiendaDto);
    Tienda update(UUID id, TiendaDto tiendaDto);
    Page<Tienda> findAll(Optional<String> ciudad, Optional<String> telefono, Optional<String> direccion, Pageable pageable);
    Tienda findById(UUID id);
    void deleteById(UUID id);
}
