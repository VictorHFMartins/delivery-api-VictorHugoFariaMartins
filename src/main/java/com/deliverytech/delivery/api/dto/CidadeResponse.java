package com.deliverytech.delivery.api.dto;

import com.deliverytech.delivery.domain.model.Cidade;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "CidadeResponse", description = "Retorno dos dados de uma cidade.")
public record CidadeResponse(

        @Schema(description = "Identificador da cidade", example = "5")
        Long id,

        @Schema(description = "Nome da cidade", example = "Campinas")
        String nome,

        @Schema(description = "Estado associado à cidade")
        EstadoResponse estado
) {
    public static CidadeResponse of(Cidade c) {
        return new CidadeResponse(
                c.getId(),
                c.getNome(),
                EstadoResponse.of(c.getEstado())
        );
    }
}
