package com.deliverytech.delivery.api.dto;

import java.math.BigDecimal;

import com.deliverytech.delivery.domain.model.ItemPedido;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "ItemPedidoResponse", description = "Retorno dos itens do pedido.")
public record ItemPedidoResponse(

        @Schema(description = "ID do item", example = "55")
        Long id,

        @Schema(description = "Produto")
        ProdutoResponse produto,

        @Schema(description = "Quantidade", example = "3")
        Long quantidade,

        @Schema(description = "Preço unitário", example = "19.90")
        BigDecimal precoUnitario,

        @Schema(description = "Subtotal do item", example = "59.70")
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
