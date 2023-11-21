package com.example.getclothes.tiendas.services;

import com.example.getclothes.tiendas.config.WebSocketConfig;
import com.example.getclothes.tiendas.config.WebSocketHandler;
import com.example.getclothes.tiendas.exceptions.NotFoundException;
import com.example.getclothes.tiendas.mapper.NotificacionMapper;
import com.example.getclothes.tiendas.mapper.TiendaMapper;
import com.example.getclothes.tiendas.models.Tienda;
import com.example.getclothes.tiendas.models.TiendaDto;
import com.example.getclothes.tiendas.repositories.TiendaRepository;
import com.example.getclothes.tiendas.websockets.Notificacion;
import com.example.getclothes.tiendas.websockets.TiendaNotificacion;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@CacheConfig(cacheNames = {"tiendas"})
public class TiendaServiceImpl implements TiendaService{
    private final TiendaRepository repository;
    private final TiendaMapper mapper;
    private final NotificacionMapper notificacionMapper;
    private final WebSocketConfig webSocketConfig;
    private final ObjectMapper objectMapper;
    private WebSocketHandler webSocketHandler;

    @Autowired
    public TiendaServiceImpl(TiendaRepository repository, TiendaMapper mapper, NotificacionMapper notificacionMapper, WebSocketConfig webSocketConfig) {
        this.repository = repository;
        this.mapper = mapper;
        this.notificacionMapper = notificacionMapper;
        this.webSocketConfig = webSocketConfig;
        objectMapper = new ObjectMapper();
        webSocketHandler = webSocketConfig.webSocketTiendasHandler();
    }

    @Override
    @CachePut
    public Tienda save(TiendaDto tiendaDto) {
        Tienda tienda = mapper.convertirDTOaTienda(tiendaDto);
        onChange(Notificacion.Tipo.CREATE, tienda);
        return repository.save(tienda);
    }

    @Override
    @CachePut
    public Tienda update(UUID id, TiendaDto tiendaDto) {
        Tienda existingTienda = repository.findById(id).orElse(null);
        Tienda updatedTienda = mapper.convertirDTOaTienda(tiendaDto);

        if (existingTienda != null) {
            existingTienda.setCiudad(updatedTienda.getCiudad());
            existingTienda.setDireccion(updatedTienda.getDireccion());
            existingTienda.setTelefono(updatedTienda.getTelefono());
            onChange(Notificacion.Tipo.UPDATE, existingTienda);
            return repository.save(existingTienda);
        } else {
            throw new NotFoundException("La tienda con id " + id + " no existe");
        }
    }

    @Override
    public Page<Tienda> findAll(Optional<String> ciudad, Optional<String> telefono, Optional<String> direccion, Pageable pageable) {
        Specification<Tienda> specCiudad = (root, query, criteriaBuilder) ->
                ciudad.map(c -> criteriaBuilder.like(criteriaBuilder.lower(root.get("ciudad")), "%" + c.toLowerCase() + "%"))
                        .orElseGet(() -> criteriaBuilder.isTrue(criteriaBuilder.literal(true)));

        Specification<Tienda> specDireccion = (root, query, criteriaBuilder) ->
                direccion.map(d -> criteriaBuilder.like(criteriaBuilder.lower(root.get("direccion")), "%" + d.toLowerCase() + "%"))
                        .orElseGet(() -> criteriaBuilder.isTrue(criteriaBuilder.literal(true)));

        Specification<Tienda> specTelefono = (root, query, criteriaBuilder) ->
                telefono.map(t -> criteriaBuilder.like(criteriaBuilder.lower(root.get("telefono")), "%" + t.toLowerCase() + "%"))
                        .orElseGet(() -> criteriaBuilder.isTrue(criteriaBuilder.literal(true)));

        Specification<Tienda> criterios = Specification.where(specCiudad)
                .and(specTelefono).and(specDireccion);
        return repository.findAll(criterios, pageable);
    }

    @Override
    @Cacheable
    public Tienda findById(UUID id) {
        Tienda existingTienda = repository.findById(id).orElse(null);
        if (existingTienda != null){
            return existingTienda;
        }else {
            throw new NotFoundException("La tienda con id " + id + " no existe");
        }
    }

    @Override
    @CacheEvict
    public void deleteById(UUID id) {
        Tienda existingTienda = repository.findById(id).orElse(null);
        if (existingTienda != null){
            onChange(Notificacion.Tipo.DELETE, existingTienda);
            repository.deleteById(id);
        }else {
            throw new NotFoundException("La tienda con id " + id + " no existe");
        }
    }

    //Metodo que ante cualquier cambio C,U,D envia una notificacion
    void onChange(Notificacion.Tipo tipo, Tienda data) {

        if (webSocketHandler == null) {
            webSocketHandler = this.webSocketConfig.webSocketTiendasHandler();
        }

        try {
            Notificacion<TiendaNotificacion> notificacion = new Notificacion<>(
                    "Tiendas",
                    tipo,
                    notificacionMapper.convertirTiendaATiendaNotificacion(data),
                    LocalDateTime.now().toString()
            );

            String json = objectMapper.writeValueAsString((notificacion));

            Thread senderThread = new Thread(() -> {
                try {
                    webSocketHandler.sendMessage(json);
                } catch (Exception e) {
                }
            });
            senderThread.start();
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
