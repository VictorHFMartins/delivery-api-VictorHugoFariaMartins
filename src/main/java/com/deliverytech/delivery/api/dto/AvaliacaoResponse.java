package com.deliverytech.delivery.api.dto;

import java.time.LocalDateTime;

import com.deliverytech.delivery.domain.enums.NotaAvaliacao;
import com.deliverytech.delivery.domain.model.Avaliacao;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Representação completa de uma avaliação retornada pela API")
public record AvaliacaoResponse(

        @Schema(description = "ID da avaliação", example = "45")
        Long id,

        @Schema(description = "Nota atribuída pelo cliente")
        NotaAvaliacao nota,

        @Schema(description = "Comentário do cliente", example = "Comida muito boa, entregaram rápido")
        String comentario,

        @Schema(description = "Resposta do restaurante", example = "Obrigado pelo feedback!")
        String respostaRestaurante,

        @Schema(description = "Momento em que a avaliação foi feita")
        LocalDateTime dataAvaliacao,

        @Schema(description = "Data da última modificação da avaliação")
        LocalDateTime ultimaAtualizacao,

        @Schema(description = "ID do cliente que fez a avaliação", example = "3")
        Long clienteId,

        @Schema(description = "Nome do cliente", example = "João Silva")
        String clienteNome,

        @Schema(description = "ID do restaurante avaliado", example = "8")
        Long restauranteId,

        @Schema(description = "Nome do restaurante", example = "Cantina da Vila")
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
                a.getRestaurante().getNome()
        );
    }
}
