package com.deliverytech.delivery.domain.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.deliverytech.delivery.api.dto.ItemPedidoResponse;
import com.deliverytech.delivery.domain.model.ItemPedido;

@Repository
public interface ItemPedidoRepository extends JpaRepository<ItemPedido, Long> {

    @Query("""
    SELECT new com.deliverytech.delivery.api.dto.ItemPedidoResponse(
        ip.produto.id,
        ip.produto.nome,
        ip.produto.preco,
        SUM(ip.quantidade)
    )
    FROM ItemPedido ip
    GROUP BY ip.produto.id, ip.produto.nome, ip.produto.preco
    ORDER BY SUM(ip.quantidade) DESC
    """)
    List<ItemPedidoResponse> findRankingProdutosMaisVendidos();

}
