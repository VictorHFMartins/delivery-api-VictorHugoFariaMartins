package com.deliverytech.delivery.domain.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.deliverytech.delivery.domain.model.Produto;

@Repository
public interface ProdutoRepository extends JpaRepository<Produto, Long> {

    Optional<Produto> findByNome(String nome);

    boolean existsByNome(String nome);

    List<Produto> findByRestauranteId(Long restauranteId);

    Optional<Produto> findByNomeAndRestauranteId(String nome, Long restauranteId);

}
