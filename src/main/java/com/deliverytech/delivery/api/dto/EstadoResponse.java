package com.deliverytech.delivery.api.dto;

import com.deliverytech.delivery.domain.model.Estado;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "EstadoResponse", description = "Retorno dos dados de um estado.")
public record EstadoResponse(

        @Schema(description = "Identificador do estado", example = "1")
        long id,

        @Schema(description = "Nome do estado", example = "São Paulo")
        String nome,

        @Schema(description = "UF do estado", example = "SP")
        String uf
) {
    public static EstadoResponse of(Estado e) {
        return new EstadoResponse(
                e.getId(),
                e.getNome(),
                e.getUf()
        );
    }
}
