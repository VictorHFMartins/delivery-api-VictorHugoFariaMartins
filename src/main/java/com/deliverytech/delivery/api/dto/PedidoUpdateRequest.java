package com.deliverytech.delivery.api.dto;

import java.util.List;

import org.hibernate.validator.constraints.Length;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

@Schema(name = "PedidoUpdateRequest", description = "Dados para atualizar um pedido.")
public record PedidoUpdateRequest(

        @Schema(description = "Itens do pedido")
        @NotNull(message = "Deve haver ao menos um item no pedido.")
        @Valid
        List<ItemPedidoRequest> itens,

        @Schema(description = "Observações do pedido", example = "Adicionar molho extra")
        @Length(max = 250)
        String observacoes
) {}
