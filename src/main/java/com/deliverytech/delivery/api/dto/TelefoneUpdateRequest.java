package com.deliverytech.delivery.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record TelefoneUpdateRequest(
        @NotBlank(message = "O DDD é obrigatório")
        @Size(min = 2, max = 2, message = "O DDD deve ter exatamente 2 dígitos")
        String ddd,
 
        @NotBlank(message = "O numero é obrigatorio")
        @Pattern(regexp = "\\d{8,11}", message = "O número deve conter 8 ou 9 dígitos")
        String numero,
        
        @NotBlank(message = "Tipo de telefone é obrigatório")
        String tipoTelefone
        ){}
