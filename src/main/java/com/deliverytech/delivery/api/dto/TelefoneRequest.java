package com.deliverytech.delivery.api.dto;

import com.deliverytech.delivery.domain.enums.TipoTelefone;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

@Schema(description = "Dados enviados para cadastrar um telefone")
public record TelefoneRequest(

        @Schema(description = "DDD do telefone", example = "11")
        @NotBlank(message = "O DDD é obrigatório")
        @Pattern(regexp = "\\d{2}", message = "O DDD deve ter 2 dígitos numéricos")
        String ddd,

        @Schema(description = "Número do telefone", example = "987654321")
        @NotBlank(message = "O número é obrigatório")
        @Pattern(regexp = "\\d{8,11}", message = "O número deve ter entre 8 e 11 dígitos")
        String numero,

        @Schema(description = "Tipo de telefone", example = "WHATSAPP")
        @NotNull(message = "O tipo do telefone é obrigatório")
        TipoTelefone tipoTelefone
) {}
