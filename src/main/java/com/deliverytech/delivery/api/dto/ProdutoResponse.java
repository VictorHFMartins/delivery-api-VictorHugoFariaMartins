package com.deliverytech.delivery.api.dto;

import java.math.BigDecimal;

import com.deliverytech.delivery.domain.enums.CategoriaProduto;
import com.deliverytech.delivery.domain.model.Produto;

public record ProdutoResponse(
        Long id,
        String nome,
        Long quantidade,
        BigDecimal preco,
        String descricao,
        CategoriaProduto categoriaProduto,
        boolean disponibilidade,
        Long restauranteId
        ) {

    public static ProdutoResponse of(Produto p) {
        return new ProdutoResponse(
                p.getId(),
                p.getNome(),
                p.getQuantidade(),
                p.getPreco(),
                p.getDescricao(),
                p.getCategoriaProduto(),
                p.isDisponibilidade(),
                p.getRestaurante().getId());
    }

}
