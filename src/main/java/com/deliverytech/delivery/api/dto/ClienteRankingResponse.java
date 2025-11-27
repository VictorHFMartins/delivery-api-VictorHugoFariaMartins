package com.deliverytech.delivery.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record ClienteRankingResponse(
    @Schema(description = "Id do cliente", example = "5")
    Long clienteId,

    @Schema(description = "Nome do cliente", example = "Joao Silva")
    String nome,

    @Schema(description = "Email do cliente", example = "joao@email.com")
    String email,

    @Schema(description = "Total de pedidos realizados pelo cliente", example = "10")
    Long totalPedidos
) {}