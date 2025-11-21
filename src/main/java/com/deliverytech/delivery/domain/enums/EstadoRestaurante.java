package com.deliverytech.delivery.domain.enums;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Status operacional do restaurante")
public enum EstadoRestaurante {
    ABERTO,
    FECHADO,
    MANUTENCAO
}
