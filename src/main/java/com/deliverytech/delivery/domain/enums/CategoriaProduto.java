package com.deliverytech.delivery.domain.enums;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Categorias de produtos comercializados pelos restaurantes")
public enum CategoriaProduto {

    BEBIDAS,
    COMIDAS,
    SOBREMESAS
}
