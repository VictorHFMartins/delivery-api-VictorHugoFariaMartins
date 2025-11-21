package com.deliverytech.delivery.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "Dados enviados para cadastrar um estado")
public record EstadoRequest(
        
        @Schema(description = "Nome do estado", example = "São Paulo")
        @NotBlank(message = "O nome é obrigatório")
        @Size(min = 3, max = 120, message = "O nome não pode ser menor que 3 carácteres nem exceder o limite de 120 caracteres")
        String nome,
        
        @Schema(description = "UF do estado", example = "SP")
        @NotBlank(message = "UF é obrigatória")
        @Size(min = 2, max = 2, message = "O uf deve contar 2 caracteres")
        String uf
        ) {

}
