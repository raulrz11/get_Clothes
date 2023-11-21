package com.example.getclothes.tiendas.repositories;

import com.example.getclothes.tiendas.models.Tienda;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface TiendaRepository extends JpaRepository<Tienda, UUID>, JpaSpecificationExecutor<Tienda> {

}
