package com.deliverytech.delivery.domain.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.deliverytech.delivery.domain.model.Avaliacao;

@Repository
public interface AvaliacaoRepository extends JpaRepository<Avaliacao, Long> {

    List<Avaliacao> findByRestauranteId(Long restauranteId);

    List<Avaliacao> findByRestauranteIdOrderByDataAvaliacaoDesc(Long restauranteId);

    List<Avaliacao> findByRestauranteIdOrderByNotaDesc(Long restauranteId);

    boolean existsByClienteIdAndRestauranteId(Long clienteId, Long restauranteId);

    List<Avaliacao> findByClienteId(Long clienteId);

    @Query("""
        SELECT AVG(
                CASE a.nota
                   WHEN com.deliverytech.delivery.domain.enums.NotaAvaliacao.PESSIMO   THEN 1
                   WHEN com.deliverytech.delivery.domain.enums.NotaAvaliacao.RUIM      THEN 2
                   WHEN com.deliverytech.delivery.domain.enums.NotaAvaliacao.REGULAR   THEN 3
                   WHEN com.deliverytech.delivery.domain.enums.NotaAvaliacao.BOM       THEN 4
                   WHEN com.deliverytech.delivery.domain.enums.NotaAvaliacao.OTIMO     THEN 5
                 END
              ) 
        FROM Avaliacao a 
        WHERE a.restaurante.id = :restauranteId
    """)
    Double calcularMediaPorRestaurante(Long restauranteId);

}
