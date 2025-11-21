package com.deliverytech.delivery.domain.enums;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Categorias gastronômicas dos restaurantes")
public enum CategoriaRestaurante {
    BRASILEIRO,
    ITALIANO,
    FRANCES,
    CONTEMPORANEO
}
