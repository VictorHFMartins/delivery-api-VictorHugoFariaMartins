package com.deliverytech.delivery.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Schema(description = "Dados enviados para cadastrar uma cidade")
public record CidadeRequest(

        @Schema(description = "Nome da cidade", example = "São Paulo")
        @NotBlank(message = "O nome da cidade é obrigatório")
        @Size(min = 3, max = 120, message = "O nome da cidade não pode ser menor que 3 carácteres nem exceder o limite de 120 caracteres")
        String nome,

        @Schema(description = "ID do estado da cidade", example = "1")
        @NotNull(message = "EstadoId é obrigatório")
        Long estadoId
) {}
