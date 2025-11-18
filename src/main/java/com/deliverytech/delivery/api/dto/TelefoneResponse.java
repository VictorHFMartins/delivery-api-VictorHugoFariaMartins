package com.deliverytech.delivery.api.dto;

import com.deliverytech.delivery.domain.enums.TipoTelefone;
import com.deliverytech.delivery.domain.enums.TipoUsuario;
import com.deliverytech.delivery.domain.model.Telefone;

public record TelefoneResponse(
        long id,
        String ddd,
        String numero,
        boolean ativo,
        TipoUsuario tipoUsuario,
        TipoTelefone tipoTelefone,
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
