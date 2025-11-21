package com.deliverytech.delivery.api.dto;

import com.deliverytech.delivery.domain.model.Cep;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "CepResponse", description = "Retorno dos dados de um CEP.")
public record CepResponse(

        @Schema(description = "Identificador do CEP", example = "10")
        long id,

        @Schema(description = "Código do CEP", example = "13045-789")
        String codigo,

        @Schema(description = "Cidade associada ao CEP")
        CidadeResponse cidade
) {
    public static CepResponse of(Cep c) {
        return new CepResponse(
                c.getId(),
                c.getCodigo(),
                CidadeResponse.of(c.getCidade())
        );
    }
}
