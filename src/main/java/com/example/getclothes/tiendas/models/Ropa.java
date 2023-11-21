package com.example.getclothes.tiendas.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class Ropa {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @Column
    private String nombre;
    @Column
    private String marca;

    @Column
    private String descripcion;

    @Column
    private String imagen;

    @Column
    private Integer stock;

    @Column(columnDefinition = "TIMESTAMP")
    private LocalDateTime fecha_created = LocalDateTime.now();

    @Column(columnDefinition = "TIMESTAMP")
    private LocalDateTime fecha_updated = LocalDateTime.now();

    @Column
    private Double precio;

    @Column
    private Double talla;
    //validaciones tienda y dto


}
