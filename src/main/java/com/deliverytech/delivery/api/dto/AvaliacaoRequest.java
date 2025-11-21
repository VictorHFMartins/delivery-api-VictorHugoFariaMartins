package com.deliverytech.delivery.api.dto;

import com.deliverytech.delivery.domain.enums.NotaAvaliacao;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Schema(description = "Dados necessários para registrar uma nova avaliação do cliente")
public record AvaliacaoRequest(

        @Schema(description = "ID do cliente que está avaliando", example = "12")
        @NotNull(message = "O ID do cliente é obrigatório")
        Long clienteId,

        @Schema(description = "ID do restaurante avaliado", example = "5")
        @NotNull(message = "O ID do restaurante é obrigatório")
        Long restauranteId,

        @Schema(description = "Comentário escrito pelo cliente", example = "Ótimo atendimento!")
        @Size(max = 250, message = "O comentário deve ter no máximo 250 caracteres")
        String comentario,

        @Schema(description = "Nota atribuída ao restaurante")
        @NotNull(message = "A nota é obrigatória")
        NotaAvaliacao nota
) {}
