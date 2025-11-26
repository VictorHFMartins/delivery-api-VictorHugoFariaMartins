package com.deliverytech.delivery.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Resposta enviada após autenticação bem-sucedida.")
public record AuthResponse(

        @Schema(description = "Token JWT gerado para autenticação do usuário.", example = "eyJhbGciOiJIUzI1NiJ9...")
        String token,

        @Schema(description = "ID do usuário autenticado.", example = "1")
        Long id,

        @Schema(description = "Nome completo do usuario.", example = "Victor Hugo Martins")
        String nome,

        @Schema(description = "Apelido usado no login.", example = "victor.mtech")
        String apelido,

        @Schema(description = "Cargo do usuario.", example = "ADMINISTRADOR")
        String cargo,

        @Schema(description = "E-mail do usuario.", example = "victor@mtech.com")
        String email
) {}
