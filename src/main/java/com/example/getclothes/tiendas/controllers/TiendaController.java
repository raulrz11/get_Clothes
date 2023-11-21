package com.example.getclothes.tiendas.controllers;

import com.example.getclothes.tiendas.mapper.TiendaMapper;
import com.example.getclothes.tiendas.models.Tienda;
import com.example.getclothes.tiendas.models.TiendaDto;
import com.example.getclothes.tiendas.services.TiendaServiceImpl;
import com.example.getclothes.tiendas.utils.PaginationLinks;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/getClothes")
public class TiendaController {
    private final TiendaServiceImpl tiendaService;
    private final PaginationLinks paginationLinks;
    private TiendaMapper mapper;

    @Autowired
    public TiendaController(TiendaServiceImpl tiendaService, PaginationLinks paginationLinks) {
        this.tiendaService = tiendaService;
        this.paginationLinks = paginationLinks;
    }

    @GetMapping("/tiendas")
    public ResponseEntity<Page<Tienda>> getAll(
            //Parametros para los criterios de busqueda
            @RequestParam(required = false) Optional<String> ciudad,
            @RequestParam(required = false) Optional<String> direccion,
            @RequestParam(required = false) Optional<String> telefono,
            //Parametros para la paginacion
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String direction,
            HttpServletRequest request
    ){
        Sort sort = direction.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        UriComponentsBuilder uri = UriComponentsBuilder.fromHttpUrl(request.getRequestURL().toString());
        Page<Tienda> pageResult = tiendaService.findAll(ciudad, telefono, direccion, PageRequest.of(page, size, sort));
        return ResponseEntity.ok()
                .header("link", paginationLinks.createLinkHeader(pageResult, uri))
                .body(pageResult);
    }

    @GetMapping("/tiendas/{id}")
    public ResponseEntity<Tienda> getById(@PathVariable UUID id){
        return ResponseEntity.ok(tiendaService.findById(id));
    }

    @PostMapping("/tiendas")
    public ResponseEntity<TiendaDto> create(@Valid @RequestBody TiendaDto newTienda){
        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.convertirTiendaaDTO(tiendaService.save(newTienda)));
    }

    @PutMapping("/tiendas/{id}")
    public ResponseEntity<TiendaDto> update(@PathVariable UUID id, @Valid @RequestBody TiendaDto updateTienda){
        return ResponseEntity.ok(mapper.convertirTiendaaDTO(tiendaService.update(id, updateTienda)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable UUID id){
        tiendaService.deleteById(id);
        return ResponseEntity.noContent().build();
    }


    //Metodo que permite visualizar los mensajes de las validaciones
    //Es siempre el mismo
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationExceptions(
            MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return errors;
    }
}
