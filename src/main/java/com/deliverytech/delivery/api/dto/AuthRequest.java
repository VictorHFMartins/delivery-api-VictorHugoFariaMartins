package com.deliverytech.delivery.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Dados enviados para autenticação do usuário (login).")
public record AuthRequest(

        @Schema(description = "Apelido do usuario cadastrado no sistema.",
                example = "victor.mtech")
        @NotBlank(message = "Apelido é obrigatório")
        String apelido,

        @Schema(description = "Senha do usuario.", 
                example = "123456")
        @NotBlank(message = "Senha é obrigatória")
        String senha
) {}
