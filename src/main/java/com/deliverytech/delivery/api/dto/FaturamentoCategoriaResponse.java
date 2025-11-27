package com.deliverytech.delivery.api.dto;

import java.math.BigDecimal;

import com.deliverytech.delivery.domain.enums.CategoriaProduto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Dados de faturamento por categoria de produtos")
public record FaturamentoCategoriaResponse(
        @Schema(description = "Categoria do restaurante", example = "ITALIANO")
        CategoriaProduto categoria,
        
        @Schema(description = "Faturamento total da categoria", example = "15000.75")
        BigDecimal faturamentoTotal
        ) {

}
