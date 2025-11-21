package com.deliverytech.delivery.api.dto;

import com.deliverytech.delivery.domain.enums.CategoriaRestaurante;
import com.deliverytech.delivery.domain.model.Restaurante;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Resumo com média de avaliações de um restaurante")
public record RestauranteAvaliacoesResponse(

        @Schema(description = "ID do restaurante", example = "3")
        Long id,

        @Schema(description = "Nome do restaurante", example = "Espaço Oriental")
        String nome,

        @Schema(description = "Categoria gastronômica", example = "ORIENTAL")
        CategoriaRestaurante classe,

        @Schema(description = "Média das avaliações", example = "4.3")
        Double nota
) {

    public static RestauranteAvaliacoesResponse of(Restaurante r) {

        return new RestauranteAvaliacoesResponse(
                r.getId(),
                r.getNome(),
                r.getCategoria(),
                r.getNotaAvaliacao());
    }

}
