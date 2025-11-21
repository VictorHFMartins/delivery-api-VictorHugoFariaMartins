package com.deliverytech.delivery.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

@Schema(name = "ItemPedidoRequest", description = "Dados necessários para criar um item do pedido.")
public record ItemPedidoRequest(

        @Schema(description = "ID do produto", example = "12")
        @NotNull(message = "Id do produto é obrigatório")
        Long produtoId,

        @Schema(description = "Quantidade do produto", example = "2")
        @NotNull(message = "A quantidade do produto é obrigatória")
        @Min(value = 1, message = "A quantidade mínima é 1")
        Long quantidade
) {}
