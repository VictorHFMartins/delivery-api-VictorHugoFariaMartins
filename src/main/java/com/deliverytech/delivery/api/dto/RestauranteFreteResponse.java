package com.deliverytech.delivery.api.dto;

import java.math.BigDecimal;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Restaurante com distância e taxa de entrega calculada.")
public record RestauranteFreteResponse(
        @Schema(description = "ID do restaurante", example = "1")
        Long restauranteId,

        @Schema(description = "Nome do restaurante", example = "Ecully")
        String nome,

        @Schema(description = "Categoria do restaurante", example = "CONTEMPORANEO")
        String categoria,

        @Schema(description = "Distancia entre restaurante e cliente em kilometros", example = "2")
        BigDecimal distanciaKm,

        @Schema(description = "taxa da entrega do restaurante", example = "15.00")
        BigDecimal taxaEntrega
        ) {

}
