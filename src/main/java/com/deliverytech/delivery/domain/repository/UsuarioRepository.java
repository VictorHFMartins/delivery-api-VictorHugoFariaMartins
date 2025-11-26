package com.deliverytech.delivery.domain.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.deliverytech.delivery.domain.model.Usuario;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    Optional<Usuario> findByApelido(String apelido);

    boolean existsByApelido(String apelido);

    boolean existsByEmail(String email);
}
