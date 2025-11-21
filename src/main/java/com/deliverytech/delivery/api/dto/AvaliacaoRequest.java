package com.deliverytech.delivery.api.dto;

import com.deliverytech.delivery.domain.enums.NotaAvaliacao;

public record AvaliacaoRequest(
        Long clienteId,
        Long restauranteId,
        String comentario,
        NotaAvaliacao nota
        ) {

}
