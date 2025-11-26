package com.deliverytech.delivery.domain.repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.deliverytech.delivery.domain.enums.StatusPedido;
import com.deliverytech.delivery.domain.model.Pedido;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Long> {

    List<Pedido> findByClienteId(Long clienteId);

    List<Pedido> findByRestauranteId(Long restauranteId);

    List<Pedido> findTop10ByOrderByDataPedidoDesc();

    List<Pedido> findByDataPedidoBetween(LocalDateTime inicio, LocalDateTime fim);

    @Query("""
        SELECT SUM(p.valorTotal)
        FROM Pedido p
        WHERE p.restaurante.id = :restauranteId
    """)
    BigDecimal totalVendasPorRestaurante(@Param("restauranteId") Long restauranteId);

    @Query("""
        SELECT p
        FROM Pedido p
        WHERE p.valorTotal >= :valor
        ORDER BY p.valorTotal DESC
    """)
    List<Pedido> pedidosAcimaDe(@Param("valor") BigDecimal valor);

    @Query("""
        SELECT p
        FROM Pedido p
        WHERE p.dataPedido BETWEEN :inicio AND :fim
        AND (:status IS NULL OR p.statusPedido = :status)
        ORDER BY p.dataPedido DESC
    """)
    List<Pedido> relatorioPorPeriodoEStatus(
            @Param("inicio") LocalDateTime inicio,
            @Param("fim") LocalDateTime fim,
            @Param("status") StatusPedido status
    );
}
