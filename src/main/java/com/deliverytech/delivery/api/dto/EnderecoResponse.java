package com.deliverytech.delivery.api.dto;

import com.deliverytech.delivery.domain.enums.TipoLogradouro;
import com.deliverytech.delivery.domain.model.Endereco;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "EnderecoResponse", description = "Retorno completo dos dados de um endereço.")
public record EnderecoResponse(

        @Schema(description = "Identificador do endereço", example = "15")
        Long id,

        @Schema(description = "Tipo do logradouro", example = "RUA")
        TipoLogradouro tipoLogradouro,

        @Schema(description = "Nome do logradouro", example = "Avenida Brasil")
        String logradouro,

        @Schema(description = "Número", example = "456")
        String numero,

        @Schema(description = "Bairro", example = "Centro")
        String bairro,

        @Schema(description = "Complemento", example = "Casa 2")
        String complemento,

        @Schema(description = "CEP associado ao endereço")
        CepResponse cep
) {
    public static EnderecoResponse of(Endereco e) {
        return new EnderecoResponse(
                e.getId(),
                e.getTipoLogradouro(),
                e.getLogradouro(),
                e.getNumero(),
                e.getBairro(),
                e.getComplemento(),
                CepResponse.of(e.getCep())
        );
    }
}
