package com.deliverytech.delivery.api.dto;

import java.math.BigDecimal;
import java.util.List;

import com.deliverytech.delivery.domain.enums.StatusPedido;
import com.deliverytech.delivery.domain.model.Pedido;

public record PedidoResponse(
        Long id,
        List<ItemPedidoResponse> itens,
        ClienteResponse cliente,
        RestauranteResponse restaurante,
        StatusPedido statusPedido,
        BigDecimal valorTotal,
        String observacoes
        ) {

    public static PedidoResponse of(Pedido p) {
        List<ItemPedidoResponse> itens = p.getItens().stream()
                .map(ItemPedidoResponse::of)
                .toList();

        return new PedidoResponse(
                p.getId(),
                itens,
                ClienteResponse.of(p.getCliente()),
                RestauranteResponse.of(p.getRestaurante()),
                p.getStatusPedido(),
                p.getValorTotal(),
                p.getObservacoes());
    }
}
