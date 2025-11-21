package com.deliverytech.delivery.domain.enums;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Tipos de logradouros aceitos em endereços")
public enum TipoLogradouro {
    RUA,
    AVENIDA,
    TRAVESSA,
    ALAMEDA,
    PRACA
}
