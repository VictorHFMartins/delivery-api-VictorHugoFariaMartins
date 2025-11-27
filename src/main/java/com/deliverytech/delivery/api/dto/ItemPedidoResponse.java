package com.deliverytech.delivery.api.dto;

import java.math.BigDecimal;

import com.deliverytech.delivery.domain.model.ItemPedido;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "ItemPedidoResponse", description = "Retorno dos itens do pedido.")
public record ItemPedidoResponse(
        @Schema(description = "ID do item", example = "55")
        Long id,
        
        @Schema(description = "Produto")
        String nome,
        
        @Schema(description = "Preço unitário", example = "19.90")
        BigDecimal precoUnitario,
        
        @Schema(description = "Quantidade", example = "3")
        Long quantidade
        ) {

    public static ItemPedidoResponse of(ItemPedido i) {
        return new ItemPedidoResponse(
                i.getId(),
                i.getProduto().getNome(),
                i.getPrecoUnitario(),
                i.getQuantidade()
        );
    }
}
