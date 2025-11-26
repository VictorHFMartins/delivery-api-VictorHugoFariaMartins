package com.deliverytech.delivery.api.dto;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Schema(description = "Dados enviados para cadastrar ou atualizar um usuario")
public record AdministradorRequest(

        @Schema(description = "Nome completo do usuario", example = "Victor Martins")
        @NotBlank(message = "O nome é obrigatório")
        @Size(min = 3, max = 120, message = "O nome do usuario não pode ser menor que 3 carácteres nem exceder o limite de 120 caracteres")
        String nome,

        @Schema(description = "E-mail do usuario", example = "victor@email.com")
        @NotBlank(message = "Email é obrigatório")
        @Email(message = "E-mail inválido")
        String email,

        @Schema(description = "Lista de telefones do usuario")
        @Valid
        @NotNull(message = "A lista de telefones é obrigatória")
        List<TelefoneRequest> telefones,

        @Schema(description = "Endereço completo do usuario")
        @Valid
        @NotNull(message = "O endereço é obrigatório")
        EnderecoRequest endereco
) {}
