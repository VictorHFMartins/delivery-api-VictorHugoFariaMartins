package com.deliverytech.delivery.api.dto;

import com.deliverytech.delivery.domain.enums.TipoLogradouro;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Schema(description = "Dados enviados para cadastrar ou atualizar um endereço")
public record EnderecoRequest(

        @Schema(description = "Nome do logradouro", example = "Rua das Laranjeiras")
        @NotBlank(message = "Nome do logradouro é obrigatório")
        @Size(min = 3, max = 120, message = "O nome do logradouro não pode ser menor que 3 carácteres nem exceder o limite de 120 caracteres")
        String logradouro,

        @Schema(description = "Tipo de logradouro", example = "RUA")
        @NotNull(message = "Tipo do logradouro é obrigatório")
        TipoLogradouro tipoLogradouro,

        @Schema(description = "Número do imóvel", example = "123A")
        @NotBlank(message = "Número é obrigatório")
        String numero,

        @Schema(description = "Complemento", example = "Apartamento 12")
        String complemento,

        @Schema(description = "Nome do bairro", example = "Jardim Paulista")
        @NotBlank(message = "Bairro é obrigatório")
        @Size(min = 3, max = 120, message = "O nome do bairro não pode ser menor que 3 carácteres nem exceder o limite de 120 caracteres")
        String bairro,

        @Schema(description = "Código do CEP (somente números)", example = "04534011")
        @NotBlank(message = "CEP é obrigatório")
        @Pattern(regexp = "\\d{8}", message = "CEP deve conter 8 dígitos numéricos")
        String cepCodigo,

        @Schema(description = "ID da cidade do endereço", example = "5")
        @NotNull(message = "CidadeId é obrigatório")
        Long cidadeId,

        @Schema(description = "Longitude geográfica", example = "-46.65345")
        @NotNull(message = "Longitude é obrigatória")
        Double longitude,

        @Schema(description = "Latitude geográfica", example = "-23.56443")
        @NotNull(message = "Latitude é obrigatória")
        Double latitude
) {}
