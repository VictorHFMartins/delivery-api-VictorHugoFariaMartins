package com.deliverytech.delivery.api.dto;

import java.math.BigDecimal;
import java.util.List;

import com.deliverytech.delivery.domain.enums.StatusPedido;
import com.deliverytech.delivery.domain.model.Pedido;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "PedidoResponse", description = "Retorno completo de um pedido.")
public record PedidoResponse(

        @Schema(description = "ID do pedido", example = "88")
        Long id,

        @Schema(description = "Itens do pedido")
        List<ItemPedidoResponse> itens,

        @Schema(description = "Dados do cliente")
        ClienteResponse cliente,

        @Schema(description = "Dados do restaurante")
        RestauranteResponse restaurante,

        @Schema(description = "Status do pedido", example = "PENDENTE")
        StatusPedido statusPedido,

        @Schema(description = "Valor total do pedido", example = "124.90")
        BigDecimal valorTotal,

        @Schema(description = "Observações do pedido", example = "Sem pimenta")
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
                p.getObservacoes()
        );
    }
}
