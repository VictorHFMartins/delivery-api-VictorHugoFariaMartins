package com.deliverytech.delivery.api.dto;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.hibernate.validator.constraints.Length;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

@Schema(name = "PedidoRequest", description = "Dados necessários para criar um pedido.")
public record PedidoRequest(

        @Schema(description = "ID do cliente que está realizando o pedido", example = "1")
        @NotNull(message = "Id do cliente é obrigatório.")
        Long clienteId,

        @Schema(description = "ID do restaurante que receberá o pedido", example = "4")
        @NotNull(message = "Id do restaurante é obrigatório.")
        Long restauranteId,

        @Schema(description = "Itens do pedido")
        @NotNull(message = "Deve haver ao menos um item no pedido.")
        @Valid
        List<ItemPedidoRequest> itens,

        @Schema(description = "Desconto aplicado ao pedido", example = "10.50")
        Optional<BigDecimal> desconto, 

        @Schema(description = "Observações adicionais do pedido", example = "Sem cebola, por favor")
        @Length(max = 250, message = "Limite máximo de 250 caracteres.")
        String observacoes
) {}
