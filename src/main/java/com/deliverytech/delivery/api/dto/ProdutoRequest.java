package com.deliverytech.delivery.api.dto;

import java.math.BigDecimal;

import com.deliverytech.delivery.domain.enums.CategoriaProduto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Schema(name = "ProdutoRequest", description = "Dados necessários para cadastrar ou atualizar um produto.")
public record ProdutoRequest(

        @Schema(description = "Nome do produto", example = "Pizza Calabresa")
        @NotBlank(message = "O nome é obrigatório")
        String nome,

        @Schema(description = "Quantidade do produto em estoque", example = "10")
        @NotNull(message = "A quantidade é obrigatória")
        @Min(value = 0, message = "A quantidade não pode ser negativa")
        Long quantidade,

        @Schema(description = "Preço do produto", example = "39.90")
        @NotNull(message = "O preço é obrigatório")
        BigDecimal preco,

        @Schema(description = "Descrição do produto", example = "Pizza de calabresa com queijo mussarela")
        String descricao,

        @Schema(description = "Categoria do produto", example = "COMIDAS")
        @NotNull(message = "A categoria é obrigatória")
        CategoriaProduto categoriaProduto,
        
        @Schema(description = "Id do restaurante a qual o produto pertence", example = "4")
        @NotNull(message = "A categoria é obrigatória")
        Long restauranteId
) {}
