package com.deliverytech.delivery.api.dto;

import com.deliverytech.delivery.domain.enums.CategoriaRestaurante;
import com.deliverytech.delivery.domain.model.Restaurante;

public record RestauranteAvaliacoesResponse(
        Long id,
        String nome,
        CategoriaRestaurante classe,
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
