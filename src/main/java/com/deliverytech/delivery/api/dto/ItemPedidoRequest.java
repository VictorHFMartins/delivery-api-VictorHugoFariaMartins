package com.deliverytech.delivery.api.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record ItemPedidoRequest(
        @NotNull(message = "Id do produto é obrigatório")
        Long produtoId,
        
        @NotNull(message = "A quantidade do produto é obrigatória")
        @Min(value = 0)
        Long quantidade
        ) {

}
