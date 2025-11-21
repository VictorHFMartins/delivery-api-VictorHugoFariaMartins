package com.deliverytech.delivery.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

@Schema(description = "Dados enviados para cadastrar um CEP")
public record CepRequest(

        @Schema(description = "CEP no formato 12345678 ou 12345-678", example = "04534011")
        @NotBlank(message = "CEP é obrigatório")
        @Pattern(regexp = "\\d{5}-?\\d{3}", message = "Formato de CEP inválido")
        String codigo,

        @Schema(description = "ID da cidade do CEP", example = "7")
        @NotNull(message = "CidadeId é obrigatório")
        Long cidadeId
) {}
