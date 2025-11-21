package com.deliverytech.delivery.api.dto;

import com.deliverytech.delivery.domain.model.Usuario;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "UsuarioResponse", description = "Informações básicas de um usuário do sistema.")
public record UsuarioResponse(
        
        @Schema(description = "Identificador único do usuário", example = "15")
        long id,
        
        @Schema(description = "Nome completo do usuário", example = "Victor Martins")
        String nome,
        
        @Schema(description = "E-mail cadastrado do usuário", example = "victor@example.com")
        String email
        ) {

    public static UsuarioResponse of(Usuario u) {
        if (u == null) {
            return null;
        }
        return new UsuarioResponse(
                u.getId(),
                u.getNome(),
                u.getEmail());
    }
}
