package com.deliverytech.delivery.api.dto;
import java.time.LocalTime;
import java.util.List;

import com.deliverytech.delivery.domain.enums.CategoriaRestaurante;
import com.deliverytech.delivery.domain.enums.EstadoRestaurante;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

@Schema(description = "Informações enviadas para criar ou atualizar um restaurante")
public record RestauranteRequest(

        @Schema(description = "Nome do restaurante", example = "Restaurante Sabor Caseiro")
        @NotBlank(message = "O nome é obrigatório")
        String nome,

        @Schema(description = "E-mail de contato", example = "contato@saborcaseiro.com")
        @NotBlank(message = "O email é obrigatório")
        @Email(message = "E-mail inválido")
        String email,

        @Schema(description = "Lista de telefones do restaurante")
        @Valid
        @NotNull(message = "A lista de telefones é obrigatória")
        List<TelefoneRequest> telefones,

        @Schema(description = "Endereço completo do restaurante")
        @Valid
        @NotNull(message = "O endereço é obrigatório")
        EnderecoRequest endereco,

        @Schema(description = "Número do CNPJ", example = "12345678000155")
        @NotBlank(message = "O CNPJ é obrigatório")
        @Pattern(regexp = "\\d{14}", message = "O CNPJ deve conter exatamente 14 dígitos numéricos")
        String cnpj,

        @Schema(description = "Categoria gastronômica", example = "BRASILEIRO")
        @NotNull(message = "A categoria é obrigatória")
        CategoriaRestaurante classe,

        @Schema(description = "Situação do restaurante", example = "ABERTO")
        @NotNull(message = "O estado do restaurante é obrigatório")
        EstadoRestaurante estado,

        @Schema(description = "Horário de abertura", example = "10:00")
        @NotNull(message = "O horário de abertura é obrigatório")
        LocalTime horarioAbertura,

        @Schema(description = "Horário de fechamento", example = "22:00")
        @NotNull(message = "O horário de fechamento é obrigatório")
        LocalTime horarioFechamento
) {}
