package com.deliverytech.delivery.api.dto;

import com.deliverytech.delivery.domain.enums.TipoTelefone;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record TelefoneRequest(
        @NotBlank(message = "O DDD é obrigatório")
        @Size(min = 2, max = 2, message = "O DDD deve ter exatamente 2 dígitos")
        String ddd,
        
        @NotBlank(message = "O numero é obrigatorio")
        @Pattern(regexp = "\\d{8,11}", message = "O número deve conter 9 ou 10 dígitos")
        String numero,
        
        @NotNull(message = "O Tipo do telefone é obrigatório")
        TipoTelefone tipoTelefone) {

}
