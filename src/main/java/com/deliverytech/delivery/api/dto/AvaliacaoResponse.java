package com.deliverytech.delivery.api.dto;

import java.time.LocalDateTime;

import com.deliverytech.delivery.domain.enums.NotaAvaliacao;
import com.deliverytech.delivery.domain.model.Avaliacao;

public record AvaliacaoResponse(
        Long id,
        NotaAvaliacao nota,
        String comentario,
        String respostaRestaurante,
        LocalDateTime dataAvaliacao,
        LocalDateTime ultimaAtualizacao,
        Long clienteId,
        String clienteNome,
        Long restauranteId,
        String restauranteNome
        ) {

    public static AvaliacaoResponse of(Avaliacao a) {
        return new AvaliacaoResponse(
                a.getId(),
                a.getNota(),
                a.getComentario(),
                a.getRespostaRestaurante(),
                a.getDataAvaliacao(),
                a.getUltimaAtualizacao(),
                a.getCliente().getId(),
                a.getCliente().getNome(),
                a.getRestaurante().getId(),
                a.getRestaurante().getNome());
    }
}
