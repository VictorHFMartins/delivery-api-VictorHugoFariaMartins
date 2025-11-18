package com.deliverytech.delivery.domain.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.deliverytech.delivery.domain.model.Estado;

@Repository
public interface EstadoRepository extends JpaRepository<Estado, Long> {

    // Busca estados pelo nome
    List<Estado> findByNomeContainingIgnoreCase(String nome);

    // Busca por UF
    Optional<Estado> findByUfIgnoreCase(String Uf);

    // Verifica estado por uf
    boolean existsByUfIgnoreCase(String uf);
}
