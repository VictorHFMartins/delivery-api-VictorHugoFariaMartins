package com.deliverytech.delivery.api.dto;

import java.math.BigDecimal;

import com.deliverytech.delivery.domain.enums.CategoriaProduto;
import com.deliverytech.delivery.domain.model.Produto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "ProdutoResponse", description = "Retorno dos dados de um produto.")
public record ProdutoResponse(

        @Schema(description = "ID do produto", example = "15")
        Long id,

        @Schema(description = "Nome do produto", example = "X-Burger")
        String nome,

        @Schema(description = "Quantidade disponível no estoque", example = "12")
        Long quantidade,

        @Schema(description = "Preço do produto", example = "22.50")
        BigDecimal preco,

        @Schema(description = "Descrição do produto", example = "Hambúrguer artesanal com queijo cheddar")
        String descricao,

        @Schema(description = "Categoria do produto", example = "COMIDAS")
        CategoriaProduto categoriaProduto,

        @Schema(description = "Disponibilidade do produto", example = "true")
        boolean disponibilidade,

        @Schema(description = "ID do restaurante dono do produto", example = "3")
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
                p.getRestaurante().getId()
        );
    }
}

