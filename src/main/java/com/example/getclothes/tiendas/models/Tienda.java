package com.example.getclothes.tiendas.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UuidGenerator;

import java.util.List;
import java.util.UUID;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@Table(name = "tiendas")
public class Tienda {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @Column(name = "ciudad")
    @NotBlank(message = "La ciudad no puede estar vacia")
    private String ciudad;
    @Column(name = "direccion")
    @NotBlank(message = "La direccion no puede estar vacia")
    private String direccion;
    @Column(name = "telefono")
    @NotBlank(message = "El telefono no puede estar vacio")
    private String telefono;

}
