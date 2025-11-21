package com.deliverytech.delivery.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "DTO para resposta do restaurante a uma avaliação")
public record respostaAvaliacaoRequest(

        @Schema(description = "Texto da resposta do restaurante", example = "Obrigado pelo feedback!")
        @NotBlank(message = "A resposta não pode estar vazia")
        @Size(max = 250, message = "A resposta deve ter no máximo 250 caracteres")
        String resposta
) {}
