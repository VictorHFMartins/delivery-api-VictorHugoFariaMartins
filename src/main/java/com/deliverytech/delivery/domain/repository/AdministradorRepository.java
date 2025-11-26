package com.deliverytech.delivery.domain.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.deliverytech.delivery.domain.model.Administrador;

@Repository
public interface AdministradorRepository extends JpaRepository<Administrador, Long> {

    boolean existsByEmail(String email);

    List<Administrador> findByStatusTrue();

}
