package com.deliverytech.delivery.api.dto;

import java.math.BigDecimal;

import com.deliverytech.delivery.domain.model.ItemPedido;

public record ItemPedidoResponse(
        Long id,
        ProdutoResponse produto,
        Long quantidade,
        BigDecimal precoUnitario,
        BigDecimal subtotal
        ) {

    public static ItemPedidoResponse of(ItemPedido i) {
        return new ItemPedidoResponse(
                i.getId(),
                ProdutoResponse.of(i.getProduto()),
                i.getQuantidade(),
                i.getPrecoUnitario(),
                i.getSubtotal()
        );
    }
}
