package com.deliverytech.delivery.domain.enums;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Tipo de telefone cadastrado pelo usuário")
public enum TipoTelefone {
    FIXO,
    CELULAR,
    WHATSAPP
}
