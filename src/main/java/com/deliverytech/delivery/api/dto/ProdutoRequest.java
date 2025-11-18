package com.deliverytech.delivery.api.dto;

import java.math.BigDecimal;

import com.deliverytech.delivery.domain.enums.CategoriaProduto;

public record ProdutoRequest(
        String nome,
        Long quantidade,
        BigDecimal preco,
        String descricao,
        CategoriaProduto categoriaProduto,
        Long restauranteId
        ) {

}
