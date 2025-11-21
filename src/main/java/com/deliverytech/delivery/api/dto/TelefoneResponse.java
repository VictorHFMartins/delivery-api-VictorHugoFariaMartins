package com.deliverytech.delivery.api.dto;

import com.deliverytech.delivery.domain.enums.TipoTelefone;
import com.deliverytech.delivery.domain.enums.TipoUsuario;
import com.deliverytech.delivery.domain.model.Telefone;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "TelefoneResponse", description = "Retorno detalhado de um telefone cadastrado.")
public record TelefoneResponse(

        @Schema(description = "Identificador do telefone", example = "12")
        long id,

        @Schema(description = "DDD do telefone", example = "11")
        String ddd,

        @Schema(description = "Número do telefone formatado ou não", example = "998877665")
        String numero,

        @Schema(description = "Define se o telefone está ativo", example = "true")
        boolean ativo,

        @Schema(description = "Tipo de usuário dono do telefone", example = "CLIENTE")
        TipoUsuario tipoUsuario,

        @Schema(description = "Tipo do telefone", example = "CELULAR")
        TipoTelefone tipoTelefone,

        @Schema(description = "Dados do usuário relacionado ao telefone")
        UsuarioResponse usuario
) {

    public static TelefoneResponse of(Telefone t) {
        return new TelefoneResponse(
                t.getId(),
                t.getDdd(),
                t.getNumero(),
                t.isAtivo(),
                t.getTipoUsuario(),
                t.getTipoTelefone(),
                t.getUsuario() != null ? UsuarioResponse.of(t.getUsuario()) : null);
    }
}
